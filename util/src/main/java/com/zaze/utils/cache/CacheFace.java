package com.zaze.utils.cache;

/**
 * Description :
 * date : 2015-11-27 - 17:11
 *
 * @author : zaze
 * @version : 1.0
 */
interface CacheFace {
    /**
     * 保存到缓存
     *
     * @param key       key
     * @param values    values
     * @param keepTime  keepTime
     * @param dataLevel dataLevel
     */
    void saveCache(String key, byte[] values, long keepTime, @DataLevel int dataLevel);

    /**
     * 从缓存中获取数据
     *
     * @param key key
     * @return byte[]
     */
    byte[] getCache(String key);

    /**
     * 清除指定缓存
     *
     * @param key key
     */
    void clearCache(String key);

    /**
     * 清除所有缓存
     */
    void clearMemoryCache();
}
