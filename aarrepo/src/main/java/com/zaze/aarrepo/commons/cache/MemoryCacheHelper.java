package com.zaze.aarrepo.commons.cache;

import com.zaze.aarrepo.utils.StringUtil;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-02-24 - 14:47
 */
public class MemoryCacheHelper {
    public static final long SECOND = 1000L;
    public static final long MINUTE = 60000L;
    public static final long HOURE = 3600000L;
    public static final long DAY = 86400000L;
    public static final long WEEK = 604800000L;
    public static final long HALF_YEAR = 110376000000L;
    public static final long YEAR = 220752000000L;

    public static String getCache(String key) {
        return StringUtil.bytesToString(MemoryCache.getInstance().getCache(key));
    }

    public static byte[] getCacheBytes(String key) {
        return MemoryCache.getInstance().getCache(key);
    }
    // -------

    public static void deleteCache(String key) {
        MemoryCache.getInstance().deleteCache(key);
    }

    public static void clearMemoryCache() {
        MemoryCache.getInstance().clearMemoryCache();
    }

    // --------------------------------------------------

    /**
     * @param key
     * @param valueStr
     */
    public static void saveCache(String key, String valueStr) {
        saveCache(key, valueStr, HOURE);
    }

    /**
     * @param key
     * @param valueStr
     * @param dataTime
     */
    public static void saveCache(String key, String valueStr, long dataTime) {
        saveCache(key, valueStr, dataTime, DataLevel.LOW_LEVEL_DATA);
    }


    /**
     * @param key
     * @param valueStr
     * @param keepTime
     * @param dataLevel
     */
    public static void saveCache(String key, String valueStr, long keepTime, @DataLevel.DataAnno int dataLevel) {
        saveCacheBytes(key, StringUtil.string2Bytes(valueStr), keepTime, dataLevel);
    }

    /**
     * @param key
     * @param values
     */
    public static void saveCacheBytes(String key, byte[] values) {
        saveCacheBytes(key, values, HOURE);
    }

    /**
     * @param key
     * @param values
     * @param dataTime
     */
    public static void saveCacheBytes(String key, byte[] values, long dataTime) {
        saveCacheBytes(key, values, dataTime, DataLevel.LOW_LEVEL_DATA);
    }

    /**
     * @param key
     * @param values
     * @param keepTime
     * @param dataLevel
     */
    public static void saveCacheBytes(String key, byte[] values, long keepTime, @DataLevel.DataAnno int dataLevel) {
        MemoryCache.getInstance().setCache(key, values, keepTime, dataLevel);
    }
}
