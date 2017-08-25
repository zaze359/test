package com.zaze.utils.log;

/**
 * Description :
 * date : 2015-11-27 - 17:11
 *
 * @author : zaze
 * @version : 1.0
 */
public interface ZLogFace {

    void v(String strTag, String strLog);

    void d(String strTag, String strLog);

    void i(String strTag, String strLog);

    void w(String strTag, String strLog);

    void e(String strTag, String strLog);

    void writeLogToFile(String strTag, String strLog);

    String getLogFromFile();
}
