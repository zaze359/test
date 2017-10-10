package com.zaze.demo.component.device.presenter.impl;

import com.zaze.common.base.ZBasePresenter;
import com.zaze.demo.component.device.presenter.DevicePresenter;
import com.zaze.demo.component.device.view.DeviceView;
import com.zaze.demo.model.DeviceModel;
import com.zaze.demo.model.ModelFactory;
import com.zaze.utils.ZNetUtil;

/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-01-22 01:39 1.0
 */
public class DevicePresenterImpl extends ZBasePresenter<DeviceView> implements DevicePresenter {
    private DeviceModel deviceModel;

    public DevicePresenterImpl(DeviceView view) {
        super(view);
        deviceModel = ModelFactory.getDeviceModel();
    }

    @Override
    public void getDeviceInfo() {
        view.showDeviceInfo(deviceModel.getDeviceInfo());
        String macAddress = ZNetUtil.getWLANMacAddress();
        view.showMacAddress("WLAN MAC地址 : " + macAddress);

    }
}
