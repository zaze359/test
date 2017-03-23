package com.zaze.demo.model.impl;

import com.zaze.aarrepo.utils.DeviceUtil;
import com.zaze.aarrepo.utils.StringUtil;
import com.zaze.demo.app.MyApplication;
import com.zaze.demo.model.DeviceModel;
import com.zaze.demo.model.entity.DeviceStatus;

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
        deviceStatus.setTag("设备号");
        deviceStatus.setContent(DeviceUtil.getMachineCode(MyApplication.getInstance()));
        list.add(deviceStatus);
        // -------
        deviceStatus = new DeviceStatus();
        deviceStatus.setTag("型号");
        deviceStatus.setContent(DeviceUtil.getModel());
        list.add(deviceStatus);
        // -------
        deviceStatus = new DeviceStatus();
        deviceStatus.setTag("Android版本");
        deviceStatus.setContent(DeviceUtil.getSysVersion());
        list.add(deviceStatus);
        // -------
        deviceStatus = new DeviceStatus();
        deviceStatus.setTag("SD卡空间状况");
        String sdTotal = StringUtil.format("%sMB", DeviceUtil.getSDTotalSpace() >> 20);
        String sdFree = StringUtil.format("%sMB", DeviceUtil.getSDFreeSpace() >> 20);
        deviceStatus.setContent(StringUtil.format("总空间 : %s\n剩余空间 : %s", sdTotal, sdFree));
        list.add(deviceStatus);
        // -------
        deviceStatus = new DeviceStatus();
        deviceStatus.setTag("机身空间状况");
        String romTotal = StringUtil.format("%sMB", DeviceUtil.getRomTotalSpace() >> 20);
        String romFree = StringUtil.format("%sMB", DeviceUtil.getRomFreeSpace() >> 20);
        deviceStatus.setContent(StringUtil.format("总大小 : %s\n剩余大小 : %s", romTotal, romFree));
        list.add(deviceStatus);
        return list;
    }
}
