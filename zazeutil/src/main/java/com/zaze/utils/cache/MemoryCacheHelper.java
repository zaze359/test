package com.zaze.utils.cache;


import com.zaze.utils.ZStringUtil;

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

    // --------------------------------------------------
    public static String getCache(String key) {
        return ZStringUtil.bytesToString(getCacheBytes(key));
    }

    public static byte[] getCacheBytes(String key) {
        return MemoryCache.getInstance().getCache(key);
    }
    // --------------------------------------------------

    public static void deleteCache(String key) {
        MemoryCache.getInstance().clearCache(key);
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
     * @param key       key
     * @param valueStr  valueStr
     * @param keepTime  keepTime
     * @param dataLevel dataLevel
     */
    public static void saveCache(String key, String valueStr, long keepTime, @DataLevel int dataLevel) {
        saveCacheBytes(key, ZStringUtil.string2Bytes(valueStr), keepTime, dataLevel);
    }

    /**
     * @param key    key
     * @param values values
     */
    public static void saveCacheBytes(String key, byte[] values) {
        saveCacheBytes(key, values, HOURE);
    }

    /**
     * @param key      key
     * @param values   values
     * @param dataTime dataTime
     */
    public static void saveCacheBytes(String key, byte[] values, long dataTime) {
        saveCacheBytes(key, values, dataTime, DataLevel.LOW_LEVEL_DATA);
    }

    /**
     * @param key       key
     * @param values    values
     * @param keepTime  keepTime
     * @param dataLevel dataLevel
     */
    public static void saveCacheBytes(String key, byte[] values, long keepTime, @DataLevel int dataLevel) {
        MemoryCache.getInstance().saveCache(key, values, keepTime, dataLevel);
    }
}
