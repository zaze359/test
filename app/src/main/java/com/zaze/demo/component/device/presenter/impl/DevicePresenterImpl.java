package com.zaze.demo.component.device.presenter.impl;

import com.zaze.aarrepo.commons.base.ZBasePresenter;
import com.zaze.aarrepo.utils.DeviceUtil;
import com.zaze.demo.component.device.presenter.DevicePresenter;
import com.zaze.demo.component.device.view.DeviceView;
import com.zaze.demo.model.DeviceModel;
import com.zaze.demo.model.ModelFactory;

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

        String macAddress = DeviceUtil.getMacAddress();
        view.showMacAddress(macAddress);

    }
}
