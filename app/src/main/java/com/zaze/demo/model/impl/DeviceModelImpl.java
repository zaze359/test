package com.zaze.demo.model.impl;

import android.app.ActivityManager;
import android.os.Build;
import android.text.TextUtils;

import com.zaze.demo.app.MyApplication;
import com.zaze.demo.model.DeviceModel;
import com.zaze.demo.model.entity.DeviceStatus;
import com.zaze.utils.FileUtil;
import com.zaze.utils.ZCommand;
import com.zaze.utils.ZDeviceUtil;
import com.zaze.utils.ZNetUtil;
import com.zaze.utils.ZStringUtil;
import com.zaze.utils.date.DateUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.TimeZone;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-01-22 - 17:26
 */
public class DeviceModelImpl implements DeviceModel {
    @Override
    public ArrayList<DeviceStatus> getDeviceInfo() {
        ArrayList<DeviceStatus> list = new ArrayList<>();
        DeviceStatus deviceStatus;
        // --------------------------------------------------
        deviceStatus = new DeviceStatus();
        deviceStatus.setTag("Android版本");
        deviceStatus.setName("os");
        deviceStatus.setContent(Build.VERSION.RELEASE);
        list.add(deviceStatus);
        // --------------------------------------------------
        deviceStatus = new DeviceStatus();
        deviceStatus.setTag("androidSDK");
        deviceStatus.setName("sdkInt");
        deviceStatus.setContent(String.valueOf(Build.VERSION.SDK_INT));
        list.add(deviceStatus);
        // --------------------------------------------------
        deviceStatus = new DeviceStatus();
        deviceStatus.setTag("型号");
        deviceStatus.setName("model");
        deviceStatus.setContent(Build.MODEL);
        list.add(deviceStatus);
        // --------------------------------------------------
        deviceStatus = new DeviceStatus();
        deviceStatus.setTag("主板");
        deviceStatus.setName("board");
        deviceStatus.setContent(Build.BOARD);
        list.add(deviceStatus);
        // --------------------------------------------------
        deviceStatus = new DeviceStatus();
        deviceStatus.setTag("版本号");
        deviceStatus.setName("display");
        deviceStatus.setContent(Build.DISPLAY);
        list.add(deviceStatus);
        // --------------------------------------------------
        deviceStatus = new DeviceStatus();
        deviceStatus.setTag("FINGERPRINT");
        deviceStatus.setName("fingerprint");
        deviceStatus.setContent(Build.FINGERPRINT);
        list.add(deviceStatus);
        // --------------------------------------------------
        deviceStatus = new DeviceStatus();
        deviceStatus.setTag("基带版本");
        deviceStatus.setName("bootloader");
        deviceStatus.setContent(Build.BOOTLOADER);
        list.add(deviceStatus);
        // --------------------------------------------------
        deviceStatus = new DeviceStatus();
        deviceStatus.setTag("系统定制商");
        deviceStatus.setName("brand");
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
        deviceStatus.setName("product");
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
        deviceStatus.setContent(DateUtil.timeMillisToString(Build.TIME, "yyyy-MM-dd HH:mm:ss", TimeZone.getDefault()));
        list.add(deviceStatus);
        // --------------------------------------------------
        deviceStatus = new DeviceStatus();
        deviceStatus.setTag("builder类型");
        deviceStatus.setContent(Build.TYPE);
        list.add(deviceStatus);
        // --------------------------------------------------
//        deviceStatus = new DeviceStatus();
//        deviceStatus.setTag("内核版本");
//        deviceStatus.setContent(FileUtil.readFromFile(FileUtil.getSDCardRoot() + "/proc/version").toString());
//        list.add(deviceStatus);
        // --------------------------------------------------
        deviceStatus = new DeviceStatus();
        deviceStatus.setTag("USER");
        deviceStatus.setContent(Build.USER);
        list.add(deviceStatus);
        // --------------------------------------------------
        deviceStatus = new DeviceStatus();
        deviceStatus.setTag("ROOT");
        deviceStatus.setName("root");
        deviceStatus.setContent(String.valueOf(ZCommand.isRoot()));
        list.add(deviceStatus);
        // --------------------------------------------------
        deviceStatus = new DeviceStatus();
        deviceStatus.setTag("设备号");
        deviceStatus.setName("deviceId");
        deviceStatus.setContent(ZDeviceUtil.getUUID(MyApplication.getInstance()));
        list.add(deviceStatus);
        // --------------------------------------------------
        deviceStatus = new DeviceStatus();
        deviceStatus.setTag("IMEI");
        deviceStatus.setName("IMEI");
        deviceStatus.setContent(ZDeviceUtil.getDeviceId(MyApplication.getInstance()));
        list.add(deviceStatus);
        // --------------------------------------------------
        deviceStatus = new DeviceStatus();
        deviceStatus.setTag("CPU 数目");
        deviceStatus.setContent(Runtime.getRuntime().availableProcessors() + "");
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
        deviceStatus.setTag("运存情况");
        String vmMax = ZStringUtil.format("%sMB", ZDeviceUtil.getRuntimeMaxMemory() >> 20);
        String vmTotal = ZStringUtil.format("%sMB", ZDeviceUtil.getRuntimeTotalMemory() >> 20);
        String vmFree = ZStringUtil.format("%sKB", ZDeviceUtil.getRuntimeFreeMemory() >> 10);
        deviceStatus.setContent(ZStringUtil.format("最大运存 : %s\n总运存 : %s\n剩余运存 : %s", vmMax, vmTotal, vmFree));
        list.add(deviceStatus);
        // --------------------------------------------------
        deviceStatus = new DeviceStatus();
        deviceStatus.setTag("内存空间");
        ActivityManager.MemoryInfo memoryInfo = ZDeviceUtil.getDeviceMemory(MyApplication.getInstance());
        String deviceTotalMem = ZStringUtil.format("%sMB", memoryInfo.totalMem >> 20);
        String deviceAvailMem = ZStringUtil.format("%sMB", memoryInfo.availMem >> 20);
        deviceStatus.setContent(ZStringUtil.format("总内存 : %s\n可用内存 : %s", deviceTotalMem, deviceAvailMem));
        list.add(deviceStatus);
        // --------------------------------------------------
        deviceStatus = new DeviceStatus();
        deviceStatus.setTag("SD卡空间状况");
        String sdTotal = ZStringUtil.format("%sMB", ZDeviceUtil.getSdTotalSpace() >> 20);
        String sdFree = ZStringUtil.format("%sMB", ZDeviceUtil.getSdFreeSpace() >> 20);
        deviceStatus.setContent(ZStringUtil.format("总空间 : %s\n剩余空间 : %s", sdTotal, sdFree));
        list.add(deviceStatus);
        // --------------------------------------------------
        deviceStatus = new DeviceStatus();
        deviceStatus.setTag("机身空间状况");
        String dataTotal = ZStringUtil.format("%sMB", ZDeviceUtil.getDataTotalSpace() >> 20);
        String dataFree = ZStringUtil.format("%sMB", ZDeviceUtil.getDataFreeSpace() >> 20);
        deviceStatus.setContent(ZStringUtil.format("总大小 : %s\n剩余大小 : %s", dataTotal, dataFree));
        list.add(deviceStatus);
        // --------------------------------------------------


        // --------------------------------------------------
        JSONObject jsonObject = new JSONObject();
        try {
            for (DeviceStatus status : list) {
                String name = status.getName();
                if (!TextUtils.isEmpty(name)) {
                    jsonObject.put(name, status.getContent());
                }
            }
            jsonObject.put("mac", ZNetUtil.getWLANMacAddress());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        FileUtil.writeToFile("/sdcard/aaa.json", jsonObject.toString(), false);

//        jsonObject.put("deviceId", XhApplication.getInstance().getDeviceId());
//        jsonObject.put("model", Build.MODEL);
//        jsonObject.put("os", Build.VERSION.RELEASE);
//        jsonObject.put("imei", ZStringUtil.parseString(ZDeviceUtil.getDeviceId(XhApplication.getInstance())));
//        jsonObject.put("mac", NetworkUtil.getWLANMacAddress());


        return list;
    }
}
