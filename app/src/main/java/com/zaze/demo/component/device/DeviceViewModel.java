package com.zaze.demo.component.device;

import android.app.Application;
import android.view.Display;

import androidx.lifecycle.MutableLiveData;

import com.zaze.common.base.AbsAndroidViewModel;
import com.zaze.demo.model.DeviceModel;
import com.zaze.demo.model.ModelFactory;
import com.zaze.demo.model.entity.DeviceStatus;
import com.zaze.utils.DisplayUtil;
import com.zaze.utils.NetUtil;
import com.zaze.utils.ZStringUtil;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-01-22 01:39 1.0
 */
public class DeviceViewModel extends AbsAndroidViewModel {
    private DeviceModel deviceModel;
    MutableLiveData<ArrayList<DeviceStatus>> deviceInfoList = new MutableLiveData<>();
    MutableLiveData<String> inchData = new MutableLiveData<>();


    public DeviceViewModel(@NotNull Application application) {
        super(application);
        deviceModel = ModelFactory.getDeviceModel();
    }

    public void loadDeviceInfo() {
        deviceInfoList.setValue(deviceModel.getDeviceInfo());
    }

    /**
     * 计算物理尺寸
     *
     * @param dpi dpi
     */
    public void calculatePhysicalSize(int dpi) {
        if (dpi < 1) {
            dpi = DisplayUtil.getMetrics().densityDpi;
        }
        double inch = Math.sqrt(Math.pow(DisplayUtil.getMetrics().widthPixels, 2) + Math.pow(DisplayUtil.getMetrics().heightPixels, 2)) / dpi;
        inchData.setValue(ZStringUtil.format("%.2f寸", inch));
    }

    public void calculateDpi(String inch) {
        try {
            float inchNum = Float.parseFloat(inch);
            if (inchNum > 0) {
                double dpi = Math.sqrt(Math.pow(DisplayUtil.getMetrics().widthPixels, 2) + Math.pow(DisplayUtil.getMetrics().heightPixels, 2)) / inchNum;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
