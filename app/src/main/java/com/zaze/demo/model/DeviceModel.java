package com.zaze.demo.model;

import com.zaze.demo.model.entity.DeviceStatus;

import java.util.ArrayList;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-01-22 - 17:26
 */
public interface DeviceModel {

    /**
     * getDeviceInfo
     *
     * @return List<DeviceStatus>
     */
    ArrayList<DeviceStatus> getDeviceInfo();
}
