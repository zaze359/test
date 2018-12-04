package com.zaze.demo.component.device.presenter;

import com.zaze.common.base.mvp.BaseMvpPresenter;
import com.zaze.demo.component.device.contract.DeviceContract;
import com.zaze.demo.model.DeviceModel;
import com.zaze.demo.model.ModelFactory;
import com.zaze.utils.ZDisplayUtil;
import com.zaze.utils.ZNetUtil;

/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-01-22 01:39 1.0
 */
public class DevicePresenter extends BaseMvpPresenter<DeviceContract.View> implements DeviceContract.Presenter {
    private DeviceModel deviceModel;

    public DevicePresenter(DeviceContract.View view) {
        super(view);
        deviceModel = ModelFactory.getDeviceModel();
    }

    @Override
    public void getDeviceInfo() {
        getView().showDeviceInfo(deviceModel.getDeviceInfo());
        String macAddress = ZNetUtil.getWLANMacAddress();
        getView().showMacAddress("WLAN MAC地址 : " + macAddress);
    }

    @Override
    public void calculate(String inch) {
        try {
            float inchNum = Float.parseFloat(inch);
            if (inchNum > 0) {
                getView().showDpi("" + Math.sqrt(Math.pow(ZDisplayUtil.getScreenWidthPixels(), 2) + Math.pow(ZDisplayUtil.getScreenHeightPixels(), 2)) / inchNum);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
