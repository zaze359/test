package com.zaze.commons.cache;

/**
 * Description :
 * date : 2015-11-27 - 17:11
 *
 * @author : zaze
 * @version : 1.0
 */
public interface CacheFace {
    String setCache(String key, byte[] values, long keepTime, @DataLevel.DataAnno int dataLevel);
    byte[] getCache(String key);
    void deleteCache(String key);
    void clearMemoryCache();
}
