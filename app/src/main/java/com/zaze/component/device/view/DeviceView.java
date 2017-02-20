package com.zaze.component.device.view;

import com.zaze.aarrepo.commons.base.BaseView;
import com.zaze.model.entity.DeviceStatus;

import java.util.List;

/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-01-22 01:39 1.0
 */
public interface DeviceView extends BaseView {

    /**
     * 设备状态
     *
     * @param list
     */
    void showDeviceInfo(List<DeviceStatus> list);
}
