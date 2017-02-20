package com.zaze.model;

import com.zaze.model.entity.DeviceStatus;

import java.util.List;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-01-22 - 17:26
 */
public interface DeviceModel {

    List<DeviceStatus> getDeviceInfo();
}
