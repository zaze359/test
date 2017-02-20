package com.zz.library.util;

import android.location.Location;
import android.os.Bundle;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-02-13 - 10:51
 */
public interface GPSLocationListener {
    /**
     * 方法描述：位置信息发生改变时被调用
     *
     * @param location 更新位置后的新的Location对象
     */
    void updateLocation(Location location);

    /**
     * 方法描述：provider定位源类型变化时被调用
     *
     * @param provider provider的类型
     * @param status   provider状态
     * @param extras   provider的一些设置参数（如高精度、低功耗等）
     */
    void updateStatus(String provider, int status, Bundle extras);

    /**
     * 方法描述：GPS状态发生改变时被调用（GPS手动启动、手动关闭、GPS不在服务区、GPS占时不可用、GPS可用)
     *
     * @param gpsStatus 详见{@link GPSProviderStatus}
     */
    void updateGPSProviderStatus(int gpsStatus);
}
