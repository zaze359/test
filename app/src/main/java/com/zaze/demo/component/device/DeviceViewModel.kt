package com.zaze.demo.component.device

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zaze.common.base.AbsViewModel
import com.zaze.demo.model.ModelFactory
import com.zaze.demo.model.entity.DeviceStatus
import com.zaze.utils.DisplayUtil.getMetrics
import com.zaze.utils.ZStringUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-01-22 01:39 1.0
 */
class DeviceViewModel() : AbsViewModel() {
    private val deviceModel by lazy {
        ModelFactory.getDeviceModel()
    }

    val deviceInfoList = MutableLiveData<ArrayList<DeviceStatus>>()
    val inchData = MutableLiveData<String>()

    fun loadDeviceInfo() {
        viewModelScope.launch {
            deviceInfoList.value = withContext(Dispatchers.IO) {
                deviceModel.deviceInfo
            }
        }
    }

    /**
     * 计算物理尺寸
     *
     * @param dpi dpi
     */
    fun calculatePhysicalSize(iDpi: Int) {
        val dpi = if (iDpi < 1) {
            getMetrics().densityDpi
        } else {
            iDpi
        }
        val inch = Math.sqrt(
            Math.pow(getMetrics().widthPixels.toDouble(), 2.0) + Math.pow(
                getMetrics().heightPixels.toDouble(), 2.0
            )
        ) / dpi
        inchData.value = ZStringUtil.format("%.2f寸", inch)
    }

    fun calculateDpi(inch: String) {
        try {
            val inchNum = inch.toFloat()
            if (inchNum > 0) {
                val dpi = Math.sqrt(
                    Math.pow(getMetrics().widthPixels.toDouble(), 2.0) + Math.pow(
                        getMetrics().heightPixels.toDouble(), 2.0
                    )
                ) / inchNum
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}