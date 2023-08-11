package com.zaze.core.data.repository

import android.app.Application
import android.os.Build
import com.zaze.core.data.di.IODispatcher
import com.zaze.core.model.data.DeviceStatus
import com.zaze.utils.*
import com.zaze.utils.date.DateUtil
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-01-22 - 17:26
 */
class DeviceRepository @Inject constructor(
    val application: Application,
    @IODispatcher private val dispatcher: CoroutineDispatcher
) {
    suspend fun getDeviceInfo(): List<DeviceStatus> = withContext(dispatcher) {
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
                    tag = "MAC地址",
                    content = NetUtil.getLocalMacAddressFromIp()
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
                    content = DeviceUtil.getUUID(application)
                )
            )
            add(
                DeviceStatus(
                    tag = "IMEI",
                    name = "imei",
                    content = DeviceUtil.getDeviceId(application)
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
            val memoryInfo = DeviceUtil.getDeviceMemory(application)
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
            val storageInfo = StorageLoader.loadStorageStats(application)
            add(
                DeviceStatus(
                    tag = "存储空间(${StorageLoader.StorageInfo.UNIT})",
                    content = "总大小: ${storageInfo.showTotalBytes()}\n" +
                            "剩余大小: ${storageInfo.showFreeBytes()}"
                )
            )
        }
        // --------------------------------------------------

//
//        // --------------------------------------------------
//        val jsonObject = JSONObject()
//        try {
//            for (status in list) {
//                val name = status.name
//                if (!TextUtils.isEmpty(name)) {
//                    jsonObject.put(name, status.content)
//                }
//            }
//            jsonObject.put("mac", NetUtil.getWLANMacAddress())
//        } catch (e: JSONException) {
//            e.printStackTrace()
//        }
//
//        StorageLoader.getStorageInfo(Environment.getRootDirectory())
//            .log("Environment.getRootDirectory()")
//        val context = MyApplication.getInstance()
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            writeToFile(
//                "${context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.absolutePath}/aaa.json",
//                jsonObject.toString(),
//                false
//            )
//        }

//        jsonObject.put("deviceId", BaseApplication.getInstance().getDeviceId());
//        jsonObject.put("model", Build.MODEL);
//        jsonObject.put("os", Build.VERSION.RELEASE);
//        jsonObject.put("imei", ZStringUtil.parseString(ZDeviceUtil.getDeviceId(BaseApplication.getInstance())));
//        jsonObject.put("mac", NetworkUtil.getWLANMacAddress());
        list
    }


}