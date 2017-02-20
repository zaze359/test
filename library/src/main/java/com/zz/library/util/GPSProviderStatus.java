package com.zz.library.util;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-02-13 - 10:49
 */
public class GPSProviderStatus {
    //用户手动开启GPS
    public static final int GPS_ENABLED = 0;
    //用户手动关闭GPS
    public static final int GPS_DISABLED = 1;
    //服务已停止，并且在短时间内不会改变
    public static final int GPS_OUT_OF_SERVICE = 2;
    //服务暂时停止，并且在短时间内会恢复
    public static final int GPS_TEMPORARILY_UNAVAILABLE = 3;
    //服务正常有效
    public static final int GPS_AVAILABLE = 4;
}
