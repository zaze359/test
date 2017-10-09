package com.zaze.demo.model.impl;

import com.zaze.demo.app.MyApplication;
import com.zaze.demo.model.DeviceModel;
import com.zaze.demo.model.entity.DeviceStatus;
import com.zaze.utils.ZDeviceUtil;
import com.zaze.utils.ZStringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-01-22 - 17:26
 */
public class DeviceModelImpl implements DeviceModel {
    @Override
    public List<DeviceStatus> getDeviceInfo() {
        List<DeviceStatus> list = new ArrayList<>();
        DeviceStatus deviceStatus = new DeviceStatus();
        deviceStatus.setTag("Machine Code");
        deviceStatus.setContent(ZDeviceUtil.INSTANCE.getUUID(MyApplication.getInstance()));
        list.add(deviceStatus);
        // --------------------------------------------------
        deviceStatus = new DeviceStatus();
        deviceStatus.setTag("Device ID");
        deviceStatus.setContent(ZDeviceUtil.INSTANCE.getDeviceId(MyApplication.getInstance()));
        list.add(deviceStatus);
        // --------------------------------------------------
        deviceStatus = new DeviceStatus();
        deviceStatus.setTag("型号");
        deviceStatus.setContent(ZDeviceUtil.INSTANCE.getDeviceModel());
        list.add(deviceStatus);
        // --------------------------------------------------
        deviceStatus = new DeviceStatus();
        deviceStatus.setTag("Android版本");
        deviceStatus.setContent(ZDeviceUtil.INSTANCE.getSystemVersion());
        list.add(deviceStatus);
        // --------------------------------------------------
        deviceStatus = new DeviceStatus();
        deviceStatus.setTag("内存空间");
        String vmTotal = ZStringUtil.format("%sMB", ZDeviceUtil.INSTANCE.getVMTotalMemory() >> 20);
        String vmMax = ZStringUtil.format("%sMB", ZDeviceUtil.INSTANCE.getVMMaxMemory() >> 20);
        String vmFree = ZStringUtil.format("%sMB", ZDeviceUtil.INSTANCE.getVMFreeMemory() >> 20);
        deviceStatus.setContent(ZStringUtil.format("最大空间 : %s\n总空间 : %s\n剩余空间 : %s", vmMax, vmTotal, vmFree));
        list.add(deviceStatus);
        // --------------------------------------------------
        deviceStatus = new DeviceStatus();
        deviceStatus.setTag("SD卡空间状况");
        String sdTotal = ZStringUtil.format("%sMB", ZDeviceUtil.INSTANCE.getSdTotalSpace() >> 20);
        String sdFree = ZStringUtil.format("%sMB", ZDeviceUtil.INSTANCE.getSdFreeSpace() >> 20);
        deviceStatus.setContent(ZStringUtil.format("总空间 : %s\n剩余空间 : %s", sdTotal, sdFree));
        list.add(deviceStatus);
        // --------------------------------------------------
        deviceStatus = new DeviceStatus();
        deviceStatus.setTag("机身空间状况");
        String dataTotal = ZStringUtil.format("%sMB", ZDeviceUtil.INSTANCE.getDataTotalSpace() >> 20);
        String dataFree = ZStringUtil.format("%sMB", ZDeviceUtil.INSTANCE.getDataFreeSpace() >> 20);
        deviceStatus.setContent(ZStringUtil.format("总大小 : %s\n剩余大小 : %s", dataTotal, dataFree));
        list.add(deviceStatus);
        // --------------------------------------------------
        return list;
    }
}
