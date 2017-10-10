package com.zaze.demo.model.impl;

import android.os.Build;

import com.zaze.demo.app.MyApplication;
import com.zaze.demo.model.DeviceModel;
import com.zaze.demo.model.entity.DeviceStatus;
import com.zaze.utils.ZDeviceUtil;
import com.zaze.utils.ZStringUtil;
import com.zaze.utils.date.ZDateUtil;

import java.util.ArrayList;
import java.util.Arrays;
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
        DeviceStatus deviceStatus;
        // --------------------------------------------------
        deviceStatus = new DeviceStatus();
        deviceStatus.setTag("Android版本");
        deviceStatus.setContent(ZDeviceUtil.INSTANCE.getSystemVersion());
        list.add(deviceStatus);
        // --------------------------------------------------
        deviceStatus = new DeviceStatus();
        deviceStatus.setTag("androidSDK");
        deviceStatus.setContent(String.valueOf(Build.VERSION.SDK_INT));
        list.add(deviceStatus);
        // --------------------------------------------------
        deviceStatus = new DeviceStatus();
        deviceStatus.setTag("型号");
        deviceStatus.setContent(ZDeviceUtil.INSTANCE.getDeviceModel());
        list.add(deviceStatus);
        // --------------------------------------------------
        deviceStatus = new DeviceStatus();
        deviceStatus.setTag("主板");
        deviceStatus.setContent(Build.BOARD);
        list.add(deviceStatus);
        // --------------------------------------------------
        deviceStatus = new DeviceStatus();
        deviceStatus.setTag("DISPLAY");
        deviceStatus.setContent(Build.DISPLAY);
        list.add(deviceStatus);
        // --------------------------------------------------
        deviceStatus = new DeviceStatus();
        deviceStatus.setTag("FINGERPRINT");
        deviceStatus.setContent(Build.FINGERPRINT);
        list.add(deviceStatus);
        // --------------------------------------------------
        deviceStatus = new DeviceStatus();
        deviceStatus.setTag("系统启动程序版本号(bootloader)");
        deviceStatus.setContent(Build.BOOTLOADER);
        list.add(deviceStatus);
        // --------------------------------------------------
        deviceStatus = new DeviceStatus();
        deviceStatus.setTag("系统定制商");
        deviceStatus.setContent(Build.BRAND);
        list.add(deviceStatus);
        // --------------------------------------------------
        deviceStatus = new DeviceStatus();
        deviceStatus.setTag("硬件序列号");
        deviceStatus.setContent(Build.SERIAL);
        list.add(deviceStatus);
        // --------------------------------------------------
        deviceStatus = new DeviceStatus();
        deviceStatus.setTag("Product");
        deviceStatus.setContent(Build.PRODUCT);
        list.add(deviceStatus);
        // --------------------------------------------------
        deviceStatus = new DeviceStatus();
        deviceStatus.setTag("描述Build的标签");
        deviceStatus.setContent(Build.TAGS);
        list.add(deviceStatus);
        // --------------------------------------------------
        deviceStatus = new DeviceStatus();
        deviceStatus.setTag("TIME");
        deviceStatus.setContent(ZDateUtil.timeMillisToString(Build.TIME, "yyyy-MM-dd HH:mm:ss"));
        list.add(deviceStatus);
        // --------------------------------------------------
        deviceStatus = new DeviceStatus();
        deviceStatus.setTag("builder类型");
        deviceStatus.setContent(Build.TYPE);
        list.add(deviceStatus);
        // --------------------------------------------------
        deviceStatus = new DeviceStatus();
        deviceStatus.setTag("USER");
        deviceStatus.setContent(Build.USER);
        list.add(deviceStatus);
        // --------------------------------------------------
        deviceStatus = new DeviceStatus();
        deviceStatus.setTag("Device ID");
        deviceStatus.setContent(ZDeviceUtil.INSTANCE.getDeviceId(MyApplication.getInstance()));
        list.add(deviceStatus);
        // --------------------------------------------------
        deviceStatus = new DeviceStatus();
        deviceStatus.setTag("cpu指令集");
        deviceStatus.setContent(Build.CPU_ABI);
        list.add(deviceStatus);
        // --------------------------------------------------
        deviceStatus = new DeviceStatus();
        deviceStatus.setTag("cpu指令集2");
        deviceStatus.setContent(Build.CPU_ABI2);
        list.add(deviceStatus);
        // --------------------------------------------------
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            deviceStatus = new DeviceStatus();
            deviceStatus.setTag("cpu指令集");
            deviceStatus.setContent(Arrays.toString(Build.SUPPORTED_ABIS));
            list.add(deviceStatus);
        }
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
