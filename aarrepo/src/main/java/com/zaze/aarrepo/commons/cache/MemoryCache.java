package com.zaze.aarrepo.commons.cache;


import com.zaze.aarrepo.commons.log.LogKit;
import com.zaze.aarrepo.commons.task.TaskService;
import com.zaze.aarrepo.commons.task.TaskServiceAction;
import com.zaze.aarrepo.utils.DeviceUtil;

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
    private static final long cacheBlockLength = 1024 * 1000L;
    //
    private static final ConcurrentHashMap<String, Cache> cacheMap = new ConcurrentHashMap<>();

    private static volatile MemoryCache memoryCache;

    public static MemoryCache getInstance() {
        if (memoryCache == null) {
            synchronized (MemoryCache.class) {
                if (memoryCache == null) {
                    memoryCache = new MemoryCache();
                    TaskService.registerOrdinaryTask(TaskServiceAction.RELEASE_MEMORY_CACHE);
                }
            }
        }
        return memoryCache;
    }

    private MemoryCache() {
        maxSize = DeviceUtil.getVMMaxMemory() / 8;
        passiveRelease = (long) (maxSize * 0.4);
    }


    @Override
    public String setCache(String key, byte[] values, long keepTime, @DataLevel.DataAnno int dataLevel) {
        if (values == null) {
            return "cacheData is null";
        }
        String result = dispatchMemoryCache(values);
        if (!"".equals(result)) {
            if (cacheLog) {
                LogKit.i(result);
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
            LogKit.v("MemoryCache maxSize : " + maxSize / 1024f + "kb");
            LogKit.v("MemoryCache current memoryCacheSize : " + memoryCacheSize / 1024f + "kb");
            LogKit.v("MemoryCache free : " + (maxSize - memoryCacheSize) / 1024f + "kb");
            LogKit.v("MemoryCache saveSize : " + saved.getBytes().length / 1024f + "kb");
        }
        cacheMap.put(key, saved);
        return "";
    }

    /**
     * 检查数据
     * 分配内存
     */
    private String dispatchMemoryCache(byte[] values) {
        // 去检查是否有超时的数据 和无效数据 有就 释放
//        onRelease();
        ArrayList<Cache> tempCaches = resetSize();
        if (values == null) {
            return "cache.getBytes is null";
        }
        long saveSize = values.length;

        if (saveSize > maxSize) {
            // too larger
            if (cacheLog) {
                LogKit.w("MemoryCache cacheData is larger than maxSize " + maxSize);
            }
            return "cacheData is larger than maxSize " + maxSize;
        }

        if (saveSize > cacheBlockLength) {
            // too larger
            if (cacheLog) {
                LogKit.w("MemoryCache cacheData is larger than cacheBlockLength " + cacheBlockLength);
            }
            return "cacheData is larger than cacheBlockLength " + cacheBlockLength;
        }
        long totalFree = DeviceUtil.getVMFreeMemory();
        if (totalFree <= (1024 << 10)) {
            // 系统总剩余内存 小于10MB 
            // 存磁盘 ???
            // 释放
            clearMemoryCache();
            if (cacheLog) {
                LogKit.e("MemoryCache Device RomFreeSpace is too small !! " + totalFree);
            }
            return "Device RomFreeSpace is too small !! " + totalFree;
        }
        if (memoryCacheSize + saveSize >= maxSize) {
            passiveRelease(saveSize, tempCaches);
        }
        return "";
    }

    /**
     * 重算大小
     */
    public ArrayList<Cache> resetSize() {
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
            LogKit.i("MemoryCache onRelease");
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
                    LogKit.i("MemoryCache onRelease : " + cache.toString());
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
        long releaseLength = 0l;
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
            LogKit.i("MemoryCache after passiveRelease memoryCacheSize : " + memoryCacheSize);
        }
        System.gc();
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

    // ------------
    public void setCacheLog(boolean cacheLog) {
        this.cacheLog = cacheLog;
    }
}
