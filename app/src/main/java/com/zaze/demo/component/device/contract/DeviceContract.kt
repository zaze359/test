package com.zaze.demo.component.device.contract

import com.zaze.common.base.BaseView
import com.zaze.demo.model.entity.DeviceStatus

/**
 * Description :
 * @author : ZAZE
 * @version : 2018-11-13 - 10:54
 */

interface DeviceContract {

    interface View : BaseView {
        /**
         * 设备状态
         *
         * @param list list
         */
        fun showDeviceInfo(list: ArrayList<DeviceStatus>)

        /**
         * showMacAddress
         *
         * @param macAddress macAddress
         */
        fun showMacAddress(macAddress: String)

        fun showDpi(dpi: String)
    }

    interface Presenter {
        /**
         * 获取设备状态
         */
        fun getDeviceInfo()

        fun calculate(inch: String)
    }
}