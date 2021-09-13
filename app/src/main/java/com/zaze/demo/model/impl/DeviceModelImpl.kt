package com.zaze.demo.model.impl

import android.net.wifi.WifiManager
import android.os.Build
import android.text.TextUtils
import com.zaze.common.base.BaseApplication
import com.zaze.demo.app.MyApplication
import com.zaze.demo.model.DeviceModel
import com.zaze.demo.model.entity.DeviceStatus
import com.zaze.demo.util.StorageLoader
import com.zaze.utils.*
import com.zaze.utils.FileUtil.writeToFile
import com.zaze.utils.date.DateUtil
import org.json.JSONException
import org.json.JSONObject
import java.lang.reflect.Method
import java.util.*

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-01-22 - 17:26
 */
class DeviceModelImpl : DeviceModel {
    override fun getDeviceInfo(): ArrayList<DeviceStatus> {
        val list = ArrayList<DeviceStatus>().apply {
            val displayProfile = DisplayUtil.getDisplayProfile()
            add(
                DeviceStatus(
                    tag = "屏幕分辨率",
                    content = "${displayProfile.realWidthPixels}x${displayProfile.realHeightPixels}(${displayProfile.widthDp}x${displayProfile.heightDp})"
                )
            )
            add(
                DeviceStatus(
                    tag = "屏幕密度",
                    content = "${displayProfile.getDensityDpiName()}(${displayProfile.densityDpi})(${displayProfile.density})"
                )
            )
            add(
                DeviceStatus(
                    tag = "WLAN MAC地址",
                    content = NetUtil.getWLANMacAddress()
                )
            )
            add(
                DeviceStatus(
                    tag = "Android版本",
                    name = "os",
                    content = Build.VERSION.RELEASE
                )
            )
            add(
                DeviceStatus(
                    tag = "android SDK",
                    name = "sdkInt",
                    content = Build.VERSION.SDK_INT.toString()
                )
            )
            add(
                DeviceStatus(
                    tag = "型号",
                    name = "model",
                    content = Build.MODEL
                )
            )
            add(
                DeviceStatus(
                    tag = "主板",
                    name = "board",
                    content = Build.BOARD
                )
            )
            add(
                DeviceStatus(
                    tag = "版本号",
                    name = "display",
                    content = Build.DISPLAY
                )
            )
            add(
                DeviceStatus(
                    tag = "FINGERPRINT",
                    name = "fingerprint",
                    content = Build.FINGERPRINT
                )
            )
            add(
                DeviceStatus(
                    tag = "基带版本",
                    name = "bootloader",
                    content = Build.BOOTLOADER
                )
            )
            add(
                DeviceStatus(
                    tag = "系统定制商",
                    name = "brand",
                    content = Build.BRAND
                )
            )
            add(
                DeviceStatus(
                    tag = "硬件序列号",
                    content = Build.SERIAL
                )
            )
            add(
                DeviceStatus(
                    tag = "Product",
                    name = "product",
                    content = Build.PRODUCT
                )
            )
            add(
                DeviceStatus(
                    tag = "描述Build的标签",
                    name = "tags",
                    content = Build.TAGS
                )
            )
            add(
                DeviceStatus(
                    tag = "TIME",
                    name = "tags",
                    content = DateUtil.timeMillisToString(
                        Build.TIME,
                        "yyyy-MM-dd HH:mm:ss",
                        TimeZone.getDefault()
                    )
                )
            )
            add(
                DeviceStatus(
                    tag = "builder类型",
                    name = "type",
                    content = Build.TYPE
                )
            )
            add(
                DeviceStatus(
                    tag = "USER",
                    name = "user",
                    content = Build.USER
                )
            )
            add(
                DeviceStatus(
                    tag = "ROOT",
                    name = "root",
                    content = ZCommand.isRoot().toString()
                )
            )
            add(
                DeviceStatus(
                    tag = "设备号",
                    name = "deviceId",
                    content = DeviceUtil.getUUID(MyApplication.getInstance())
                )
            )
            add(
                DeviceStatus(
                    tag = "IMEI",
                    name = "imei",
                    content = DeviceUtil.getDeviceId(MyApplication.getInstance())
                )
            )
            // --------------------------------------------------

            val result = ZCommand.execCmdForRes("cat /proc/version")
            add(
                DeviceStatus(
                    tag = "内核版本",
                    name = "kernel",
                    content = if (result.isSuccess) {
                        result.successMsg
                    } else {
                        result.errorMsg
                    }
                )
            )
            add(
                DeviceStatus(
                    tag = "CPU 数目",
                    content = Runtime.getRuntime().availableProcessors().toString()
                )
            )
            add(
                DeviceStatus(
                    tag = "cpu指令集",
                    content = Build.CPU_ABI
                )
            )
            add(
                DeviceStatus(
                    tag = "cpu指令集2",
                    content = Build.CPU_ABI2
                )
            )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                add(
                    DeviceStatus(
                        tag = "cpu指令集",
                        content = Arrays.toString(Build.SUPPORTED_ABIS)
                    )
                )
            }
            // --------------------------------------------------
            add(
                DeviceStatus(
                    tag = "运存情况",
                    content = "最大运存: ${DescriptionUtil.toByteUnit(DeviceUtil.getRuntimeMaxMemory())}\n" +
                            "总运存: ${DescriptionUtil.toByteUnit(DeviceUtil.getRuntimeTotalMemory())}\n" +
                            "剩余运存: ${DescriptionUtil.toByteUnit(DeviceUtil.getRuntimeFreeMemory())}"
                )
            )
            val memoryInfo = DeviceUtil.getDeviceMemory(MyApplication.getInstance())
            add(
                DeviceStatus(
                    tag = "内存空间",
                    content = "总内存: ${DescriptionUtil.toByteUnit(memoryInfo.totalMem)}\n" +
                            "可用内存: ${DescriptionUtil.toByteUnit(memoryInfo.availMem)}"
                )
            )
//            add(
//                DeviceStatus(
//                    tag = "SD卡存储空间",
//                    content = "总空间: ${DescriptionUtil.toByteUnit(DeviceUtil.getSdTotalSpace())}\n" +
//                            "剩余空间: ${DescriptionUtil.toByteUnit(DeviceUtil.getSdFreeSpace())}"
//                )
//            )
            val storageInfo = StorageLoader.loadStorageStats(BaseApplication.getInstance())
            add(
                DeviceStatus(
                    tag = "存储空间",
                    content = "总大小: ${DescriptionUtil.toByteUnit(storageInfo.totalBytes)}\n" +
                            "剩余大小: ${DescriptionUtil.toByteUnit(storageInfo.freeBytes)}"
                )
            )
        }
        // --------------------------------------------------


        // --------------------------------------------------
        val jsonObject = JSONObject()
        try {
            for (status in list) {
                val name = status.name
                if (!TextUtils.isEmpty(name)) {
                    jsonObject.put(name, status.content)
                }
            }
            jsonObject.put("mac", NetUtil.getWLANMacAddress())
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        writeToFile("/sdcard/aaa.json", jsonObject.toString(), false)

//        jsonObject.put("deviceId", XhApplication.getInstance().getDeviceId());
//        jsonObject.put("model", Build.MODEL);
//        jsonObject.put("os", Build.VERSION.RELEASE);
//        jsonObject.put("imei", ZStringUtil.parseString(ZDeviceUtil.getDeviceId(XhApplication.getInstance())));
//        jsonObject.put("mac", NetworkUtil.getWLANMacAddress());
        return list
    }
}