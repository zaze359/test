package com.zaze.utils.cache;

/**
 * Description :
 * date : 2015-11-27 - 17:11
 *
 * @author : zaze
 * @version : 1.0
 */
public interface CacheFace {
    void setCache(String key, byte[] values, long keepTime, @DataLevel int dataLevel);

    byte[] getCache(String key);

    void deleteCache(String key);

    void clearMemoryCache();
}
