package com.zaze.demo.component.device

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zaze.common.base.AbsAndroidViewModel
import com.zaze.common.base.AbsViewModel
import com.zaze.common.base.BaseApplication
import com.zaze.core.data.repository.DeviceRepository
import com.zaze.core.model.data.DeviceStatus
import com.zaze.utils.DisplayUtil
import com.zaze.utils.ZStringUtil
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-01-22 01:39 1.0
 */
@HiltViewModel
class DeviceViewModel @Inject constructor(
    application: Application
) : AbsAndroidViewModel(application) {

    @Inject
    lateinit var deviceModel: DeviceRepository

    val deviceInfoList = MutableLiveData<List<DeviceStatus>>()
    val inchData = MutableLiveData<String>()

    fun loadDeviceInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            deviceInfoList.postValue(deviceModel.getDeviceInfo())
        }
    }

    /**
     * 计算物理尺寸
     *
     * @param dpi dpi
     */
    fun calculatePhysicalSize(iDpi: Int) {
        val dpi = if (iDpi < 1) {
            DisplayUtil.getMetrics().densityDpi
        } else {
            iDpi
        }
        val displayProfile = DisplayUtil.getDisplayProfile()
//        displayProfile.realWidthPixels}x${displayProfile.realHeightPixels
        val inch = Math.sqrt(
            Math.pow(displayProfile.realWidthPixels.toDouble(), 2.0) + Math.pow(
                displayProfile.realHeightPixels.toDouble(), 2.0
            )
        ) / dpi
        inchData.value = ZStringUtil.format("%.2f寸", inch)
    }

    fun calculateDpi(inch: String) {
        try {
            val inchNum = inch.toFloat()
            if (inchNum > 0) {
                val dpi = Math.sqrt(
                    Math.pow(DisplayUtil.getMetrics().widthPixels.toDouble(), 2.0) + Math.pow(
                        DisplayUtil.getMetrics().heightPixels.toDouble(), 2.0
                    )
                ) / inchNum
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}