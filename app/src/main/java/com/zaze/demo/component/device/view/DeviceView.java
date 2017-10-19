package com.zaze.demo.component.device.view;


import com.zaze.common.base.ZBaseView;
import com.zaze.demo.model.entity.DeviceStatus;

import java.util.List;

/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-01-22 01:39 1.0
 */
public interface DeviceView extends ZBaseView {

    /**
     * 设备状态
     *
     * @param list list
     */
    void showDeviceInfo(List<DeviceStatus> list);

    /**
     * showMacAddress
     *
     * @param macAddress macAddress
     */
    void showMacAddress(String macAddress);
}
