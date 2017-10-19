package com.zaze.utils.cache;


import com.zaze.utils.ZDeviceUtil;
import com.zaze.utils.ZStringUtil;
import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description :
 * date : 2015-11-27 - 17:11
 *
 * @author : zaze
 * @version : 1.0
 */
public class MemoryCache implements CacheFace, OnReleaseListener {

    private boolean cacheLog = false;

    /**
     * 缓存空间大小(根据一定规则计算 得到, 默认4 MB)
     */
    private final long maxSize;

    /**
     * 强制释放时 最小值
     */
    private final long passiveRelease;
    /**
     * 当前缓存空间使用大小
     */
    private long memoryCacheSize = 0;
    /**
     * 能放进缓存的数据最大值 太大的不放内存
     */
    private static final long CACHE_BLOCK_LENGTH = 1 << 20;
    //
//    private static final ConcurrentHashMap<String, SoftReference<Cache>> cacheMap = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, Cache> cacheMap = new ConcurrentHashMap<>();

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
        maxSize = ZDeviceUtil.INSTANCE.getVMMaxMemory() / 8;
        passiveRelease = (long) (maxSize * 0.4);
    }


    /**
     * 检查数据
     * 分配内存
     */
    private String dispatchMemoryCache(byte[] values) {
        ArrayList<Cache> tempCaches = resetSize();
        if (values == null) {
            return "MemoryCache cache.getBytes is null";
        }
        long saveSize = values.length;
        if (saveSize > maxSize) {
            return "MemoryCache cacheData is larger than maxSize " + maxSize;
        }
        if (saveSize > CACHE_BLOCK_LENGTH) {
            return "MemoryCache cacheData is larger than CACHE_BLOCK_LENGTH " + CACHE_BLOCK_LENGTH;
        }
        passiveRelease(saveSize, tempCaches);
        return "";
    }

    /**
     * 重算大小
     */
    private ArrayList<Cache> resetSize() {
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

    // 外部线程循环调用 
    @Override
    public void onRelease() {
        if (cacheLog) {
            ZLog.d(ZTag.TAG_MEMORY, "MemoryCache onRelease");
        }
        long currTime = System.currentTimeMillis();
        Map<String, Cache> tempMap = new HashMap<>();
        tempMap.putAll(cacheMap);
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
                    ZLog.d(ZTag.TAG_MEMORY, "MemoryCache onRelease : " + cache.toString());
                }
            }
        }
    }

    /**
     * 强制释放不常用的
     * 去检查是否有超时的数据 和无效数据 有就 释放
     *
     * @param saveSize
     * @param caches
     */
    private void passiveRelease(long saveSize, ArrayList<Cache> caches) {
        if (memoryCacheSize + saveSize < maxSize) {
            return;
        }
        if (cacheLog) {
            ZLog.e(ZTag.TAG_MEMORY, "即将达到memoryCache最大值 >> 强制释放不常用的内存");
        }
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
                        // 时间相同 次数多的放在 后面
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
        long needRelease = passiveRelease > saveSize ? passiveRelease : saveSize;
        // 循环清除
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
            ZLog.d(ZTag.TAG_MEMORY, "passiveRelease >> needRelease : " + needRelease);
            ZLog.d(ZTag.TAG_MEMORY, "passiveRelease >> releaseLength : " + releaseLength);
            ZLog.d(ZTag.TAG_MEMORY, "passiveRelease >> (after) memoryCacheSize : " + memoryCacheSize);
        }
        System.gc();
    }

    // --------------------------------------------------
    @Override
    public void saveCache(String key, byte[] values, long keepTime, @DataLevel int dataLevel) {
        if (values == null) {
            return;
        }
        String result = dispatchMemoryCache(values);
        if (!ZStringUtil.isEmpty(result)) {
            if (cacheLog) {
                ZLog.d(ZTag.TAG_MEMORY, result);
            }
        }
        put(key, values, keepTime, dataLevel);
    }

    @Override
    public byte[] getCache(String key) {
        Cache cache = get(key);
        if (cache != null) {
            if (cacheLog) {
                ZLog.d(ZTag.TAG_MEMORY, "MemoryCache hit key : " + key);
            }
            return cache.getBytes();
        } else {
            if (cacheLog) {
                ZLog.d(ZTag.TAG_MEMORY, "MemoryCache not found key : " + key);
            }
        }
        return null;
    }

    @Override
    public void clearCache(String key) {
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

    @Override
    public void clearMemoryCache() {
        if (cacheLog) {
            ZLog.d(ZTag.TAG_MEMORY, "clearMemoryCache");
        }
        cacheMap.clear();
        memoryCacheSize = 0;
        System.gc();
    }

    // --------------------------------------------------
    private void put(String key, byte[] values, long keepTime, @DataLevel int dataLevel) {
        Cache cache = get(key);
        if (cache == null) {
            cache = new Cache(key, values, keepTime, 0, System.currentTimeMillis());
        } else {
            cache.updateCache(values, keepTime);
        }
        cacheMap.put(key, cache);
        if (cacheLog) {
            ZLog.d(ZTag.TAG_MEMORY, "MemoryCache put key : %s ", key);
//            ZLog.d(ZTag.TAG_MEMORY, "MemoryCache maxSize : " + maxSize / 1024f + "kb");
//            ZLog.d(ZTag.TAG_MEMORY, "MemoryCache free : " + (maxSize - memoryCacheSize) / 1024f + "kb");
            ZLog.d(ZTag.TAG_MEMORY, "MemoryCache saveSize : %1.3fkb", cache.getBytes().length / 1024f);
            ZLog.d(ZTag.TAG_MEMORY, "MemoryCache current memoryCacheSize : %1.3fkb", memoryCacheSize / 1024f);
            ZLog.d(ZTag.TAG_MEMORY, "MemoryCache maxSize : %1.3fkb", maxSize / 1024f);
            ZLog.d(ZTag.TAG_MEMORY, "MemoryCache passiveRelease : %1.3fkb", passiveRelease / 1024f);
        }
    }

    private Cache get(String key) {
        if (cacheMap.containsKey(key)) {
            return cacheMap.get(key);
        }
        return null;
    }

    public void setCacheLog(boolean cacheLog) {
        this.cacheLog = cacheLog;
    }
}
