package com.zaze.utils.cache;


import android.content.res.Configuration;
import android.os.SystemClock;

import com.zaze.utils.DescriptionUtil;
import com.zaze.utils.DeviceUtil;
import com.zaze.utils.ZStringUtil;
import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Description :
 * date : 2015-11-27 - 17:11
 *
 * @author : zaze
 * @version : 1.0
 */
class MemoryCache implements CacheFace, MemoryListener {

    private boolean cacheLog = false;

    /**
     * 缓存空间大小(根据一定规则计算)
     */
    private final long CACHE_SIZE_MAX;

    /**
     * 强制释放时 最小值
     */
    private final long PASSIVE_RELEASE;
    /**
     * 当前缓存空间使用大小
     */
    private long memoryCacheSize = 0;
    /**
     * 能放进缓存的数据最大值1024KB 太大的不放内存缓存
     * 大数据考虑改放文件缓存
     */
    private static final long CACHE_BLOCK_LENGTH = 1 << 20;

    private static final Object LOCK = new Object();

    private static final HashMap<String, Cache> cacheMap = new HashMap<>();

//    private static final HashMap<String, SoftReference<Cache>> cacheMap = new HashMap<>();

    private static volatile MemoryCache memoryCache;

    public static MemoryCache getInstance() {
        if (memoryCache == null) {
            synchronized (MemoryCache.class) {
                if (memoryCache == null) {
                    memoryCache = new MemoryCache();
                }
            }
        }
        return memoryCache;
    }

    private MemoryCache() {
        CACHE_SIZE_MAX = (long) (DeviceUtil.getRuntimeMaxMemory() * 0.3);
        PASSIVE_RELEASE = (long) (CACHE_SIZE_MAX * 0.4);
    }

    /**
     * 检查数据大小
     * 放入内存
     */
    private String dispatchMemoryCache(byte[] values) {
        if (values == null) {
            return "MemoryCache cache.getBytes is null";
        }
        if (values.length > CACHE_BLOCK_LENGTH) {
            return "MemoryCache cacheData is larger than CACHE_BLOCK_LENGTH " + CACHE_BLOCK_LENGTH;
        }
        passiveRelease(values.length);
        return "";
    }

    /**
     * 重算大小
     */
    private ArrayList<Cache> resetSize() {
        synchronized (LOCK) {
            ArrayList<Cache> caches = new ArrayList<>();
            long totalLength = 0L;
            for (String key : cacheMap.keySet()) {
                Cache cache = get(key);
                if (cache != null) {
                    totalLength += cache.getBytes().length;
                    caches.add(cache);
                }
            }
            // 重置大小
            memoryCacheSize = totalLength;
            return caches;
        }
    }

    @Override
    public void onTrimMemory(int level) {
        if (cacheLog) {
            ZLog.d(ZTag.TAG_MEMORY, "onTrimMemory : %s, 释放超时和无效的内存", level);
        }
        long currTime = System.currentTimeMillis();
        Map<String, Cache> tempMap;
        synchronized (LOCK) {
            tempMap = new HashMap<>(cacheMap);
        }
        for (String key : tempMap.keySet()) {
            Cache cache = tempMap.get(key);
            if (cache == null) {
                clearCache(key);
                continue;
            }
            // --------------------------------------------------
            byte[] bytes = cache.getBytes();
            if (bytes == null || bytes.length == 0) {
                clearCache(key);
                continue;
            }
            // --------------------------------------------------
            if (currTime >= cache.getLastTimeMillis() + cache.getKeepTime()) {
                clearCache(key);
                if (cacheLog) {
                    ZLog.d(ZTag.TAG_MEMORY, "onTrimMemory : " + cache.toString());
                }
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
    }

    @Override
    public void onLowMemory() {
        if (cacheLog) {
            ZLog.d(ZTag.TAG_MEMORY, "MemoryCache onLowMemory");
        }
        clearMemoryCache();
    }

    /**
     * 当将要超过最大缓存值时, 强制释放不常用的缓存
     * 去检查是否有超时的数据 和无效数据 有就 释放
     *
     * @param saveSize saveSize
     */
    private void passiveRelease(long saveSize) {
        if (memoryCacheSize + saveSize < CACHE_SIZE_MAX) {
            return;
        }
        if (cacheLog) {
            ZLog.e(ZTag.TAG_MEMORY, "即将达到CACHE_SIZE_MAX >> 强制释放不常用的内存");
        }
        ArrayList<Cache> caches = resetSize();
        //
        int length = caches.size();
        // 排序
        for (int i = 0; i < length - 1; i++) {
            Cache temp;
            for (int j = 0; j < length - 1 - i; j++) {
                Cache p = caches.get(j);
                Cache n = caches.get(j + 1);
                //最近使用的放在数组后面
                if (p.getLastTimeMillis() > n.getLastTimeMillis()) {
                    temp = p;
                    caches.set(j, n);
                    caches.set(j + 1, temp);
                } else if (p.getLastTimeMillis() == n.getLastTimeMillis()) {
                    // 时间相同 次数多的放在 后面
                    if (p.getUsedNum() > n.getUsedNum()) {
                        temp = p;
                        caches.set(j, n);
                        caches.set(j + 1, temp);
                    }
                }
            }
        }
        // 已释放大小
        long releaseLength = 0L;
        // 需要释放大小
        long needRelease = PASSIVE_RELEASE > saveSize ? PASSIVE_RELEASE : saveSize;
        // 循环清除， 直到到达 PASSIVE_RELEASE
        Iterator<Cache> iterator = caches.iterator();
        while (releaseLength < needRelease && iterator.hasNext()) {
            Cache cache = iterator.next();
            if (cache == null || cache.getKey() == null || cache.getBytes() == null) {
                continue;
            }
            releaseLength += cache.getBytes().length;
            clearCache(cache.getKey());
        }
        if (cacheLog) {
            ZLog.d(ZTag.TAG_MEMORY, "被动释放策略 >> 释放 : " + releaseLength);
            ZLog.d(ZTag.TAG_MEMORY, "被动释放策略 >> (after) memoryCacheSize : " + memoryCacheSize);
        }
    }

    // --------------------------------------------------

    @Override
    public void saveCache(String key, byte[] values, long keepTime, @DataLevel int dataLevel) {
        if (values == null) {
            return;
        }
        long startTime = 0;
        if (cacheLog) {
            startTime = SystemClock.uptimeMillis();
        }
        String result = dispatchMemoryCache(values);
        if (!ZStringUtil.isEmpty(result)) {
            if (cacheLog) {
                ZLog.w(ZTag.TAG_MEMORY, result);
            }
        }
        put(key, values, keepTime, dataLevel);
        if (cacheLog) {
            ZLog.d(ZTag.TAG_MEMORY, "saveCache耗时 : " + (SystemClock.uptimeMillis() - startTime));
        }
    }

    @Override
    public byte[] getCache(String key) {
        long startTime = 0;
        if (cacheLog) {
            startTime = SystemClock.uptimeMillis();
        }
        Cache cache = get(key);
        if (cache != null) {
            if (cacheLog) {
                ZLog.d(ZTag.TAG_MEMORY, "hit key : " + key);
            }
            if (cacheLog) {
                ZLog.d(ZTag.TAG_MEMORY, "getCache耗时 : " + (SystemClock.uptimeMillis() - startTime));
            }
            return cache.getBytes();
        } else {
            if (cacheLog) {
                ZLog.d(ZTag.TAG_MEMORY, "not found key : " + key);
            }
        }
        return null;
    }

    @Override
    public void clearCache(String key) {
        synchronized (LOCK) {
            Cache cache = get(key);
            if (cache != null) {
                if (cache.getBytes() != null) {
                    memoryCacheSize -= cache.getBytes().length;
                }
            }
            if (cacheLog) {
                ZLog.d(ZTag.TAG_MEMORY, "clearCache : " + key);
            }
            cacheMap.remove(key);
        }
    }

    @Override
    public void clearMemoryCache() {
        synchronized (LOCK) {
            if (cacheLog) {
                ZLog.d(ZTag.TAG_MEMORY, "clearMemoryCache");
            }
            cacheMap.clear();
            memoryCacheSize = 0;
        }
    }

    // --------------------------------------------------

    private void put(String key, byte[] values, long keepTime, @DataLevel int dataLevel) {
        synchronized (LOCK) {
            Cache cache = get(key);
            long offset = values != null ? values.length : 0;
            if (cache == null) {
                cache = new Cache(key, values, keepTime, 0, System.currentTimeMillis());
            } else {
                offset = cache.updateCache(values, keepTime);
            }
            cacheMap.put(key, cache);
            memoryCacheSize += offset;
            if (cacheLog) {
                ZLog.d(ZTag.TAG_MEMORY, "数据key : %s ", key);
                ZLog.d(ZTag.TAG_MEMORY, "数据length : %s", DescriptionUtil.toByteUnit(cache.getBytes() == null ? 0 : cache.getBytes().length, 1024));
                ZLog.d(ZTag.TAG_MEMORY, "被动释放临界点 : %s", DescriptionUtil.toByteUnit(PASSIVE_RELEASE, 1024));
                ZLog.d(ZTag.TAG_MEMORY, "缓存空间最大容量 : %s", DescriptionUtil.toByteUnit(CACHE_SIZE_MAX, 1024));
                ZLog.d(ZTag.TAG_MEMORY, "当前缓存空间已使用 : %s", DescriptionUtil.toByteUnit(memoryCacheSize, 1024));
            }
        }
    }

    private Cache get(String key) {
        synchronized (LOCK) {
            if (cacheMap.containsKey(key)) {
                return cacheMap.get(key);
            }
            return null;
        }
    }

    void setCacheLog(boolean cacheLog) {
        this.cacheLog = cacheLog;
    }
}
