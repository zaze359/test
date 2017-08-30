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
     * 能放进缓存的数据最大值  太大的不放内存
     */
    private static final long cacheBlockLength = 1 << 20;
    //
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
        // 去检查是否有超时的数据 和无效数据 有就 释放
        ArrayList<Cache> tempCaches = resetSize();
        if (values == null) {
            return "MemoryCache cache.getBytes is null";
        }
        long saveSize = values.length;
        if (saveSize > maxSize) {
            return "MemoryCache cacheData is larger than maxSize " + maxSize;
        }
        if (saveSize > cacheBlockLength) {
            return "MemoryCache cacheData is larger than cacheBlockLength " + cacheBlockLength;
        }
        if (memoryCacheSize + saveSize >= maxSize) {
            passiveRelease(saveSize, tempCaches);
        }
        return "";
    }

    /**
     * 重算大小
     */
    private ArrayList<Cache> resetSize() {
        ArrayList<Cache> caches = new ArrayList<>();
        long totalLength = 0L;
        for (String key : cacheMap.keySet()) {
            try {
                Cache cache = cacheMap.get(key);
                totalLength += cache.getBytes().length;
                caches.add(cache);
            } catch (Exception ignored) {
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
                // 无效数据
                cacheMap.remove(key);
                continue;
            }
            byte[] bytes = cache.getBytes();
            if (bytes == null || bytes.length == 0) {
                // 无效数据
                cacheMap.remove(key);
                continue;
            }
            if (currTime >= cache.getLastTimeMillis() + cache.getKeepTime()) {
                // 超时数据
                if (cacheLog) {
                    ZLog.d(ZTag.TAG_MEMORY, "MemoryCache onRelease : " + cache.toString());
                }
                cacheMap.remove(key);
            }
        }
    }

    /**
     * 强制释放不常用的
     *
     * @param saveSize
     * @param caches
     */
    private void passiveRelease(long saveSize, ArrayList<Cache> caches) {
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
            cacheMap.remove(cache.getKey());
        }
        if (cacheLog) {
            ZLog.d(ZTag.TAG_MEMORY, "MemoryCache after passiveRelease memoryCacheSize : " + memoryCacheSize);
        }
        System.gc();
    }

    // --------------------------------------------------
    @Override
    public String setCache(String key, byte[] values, long keepTime, @DataLevel int dataLevel) {
        if (values == null) {
            return "cacheData is null";
        }
        String result = dispatchMemoryCache(values);
        if (!ZStringUtil.isEmpty(result)) {
            if (cacheLog) {
                ZLog.d(ZTag.TAG_MEMORY, result);
            }
            return result;
        }
        Cache saved;
        if (cacheMap.containsKey(key)) {
            saved = cacheMap.get(key);
            if (saved != null) {
                saved.updateCache(values, keepTime);
            } else {
                saved = new Cache(key, values, keepTime, 0, System.currentTimeMillis());
            }
        } else {
            saved = new Cache(key, values, keepTime, 0, System.currentTimeMillis());
        }
        if (cacheLog) {
            ZLog.d(ZTag.TAG_MEMORY, "MemoryCache maxSize : " + maxSize / 1024f + "kb");
            ZLog.d(ZTag.TAG_MEMORY, "MemoryCache current memoryCacheSize : " + memoryCacheSize / 1024f + "kb");
            ZLog.d(ZTag.TAG_MEMORY, "MemoryCache free : " + (maxSize - memoryCacheSize) / 1024f + "kb");
            ZLog.d(ZTag.TAG_MEMORY, "MemoryCache saveSize : " + saved.getBytes().length / 1024f + "kb");
        }
        cacheMap.put(key, saved);
        return "";
    }

    @Override
    public byte[] getCache(String key) {
        if (cacheMap.containsKey(key)) {
            Cache cache = cacheMap.get(key);
            if (cache != null && cache.getBytes() != null) {
                return cache.getBytes();
            }
        }
        return null;
    }

    @Override
    public void deleteCache(String key) {
        if (cacheMap.containsKey(key)) {
            Cache cache = cacheMap.get(key);
            if (cache != null) {
                if (cache.getBytes() != null) {
                    memoryCacheSize -= cache.getBytes().length;
                }
            }
            cacheMap.remove(key);
        }
    }

    @Override
    public void clearMemoryCache() {
        cacheMap.clear();
        memoryCacheSize = 0;
    }

    // --------------------------------------------------
    public void setCacheLog(boolean cacheLog) {
        this.cacheLog = cacheLog;
    }
}
