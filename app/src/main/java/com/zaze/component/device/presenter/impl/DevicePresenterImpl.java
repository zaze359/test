package com.zaze.component.device.presenter.impl;

import com.zaze.aarrepo.commons.base.ZBasePresenter;
import com.zaze.component.device.presenter.DevicePresenter;
import com.zaze.component.device.view.DeviceView;
import com.zaze.model.DeviceModel;
import com.zaze.model.ModelFactory;

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
    }
}
