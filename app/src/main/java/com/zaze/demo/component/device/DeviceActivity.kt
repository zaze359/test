package com.zaze.demo.component.device

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.zaze.common.base.AbsActivity
import com.zaze.common.base.ext.myViewModels
import com.zaze.common.base.ext.obtainViewModelFactory
import com.zaze.demo.R
import com.zaze.demo.model.entity.DeviceStatus
import com.zaze.demo.theme.ThemeUtils
import com.zaze.demo.theme.ThemeUtils.refreshUI
import com.zaze.utils.DisplayUtil.getDensityDpiName
import com.zaze.utils.DisplayUtil.getMetrics
import com.zaze.utils.NetUtil
import com.zaze.utils.ZStringUtil
import kotlinx.android.synthetic.main.device_activity.*
import java.util.*

/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-01-22 01:39 1.0
 */
class DeviceActivity : AbsActivity() {
    private var adapter: DeviceAdapter? = null
    private val viewModel: DeviceViewModel by myViewModels()

    //    private var device_test_tint_iv: TintImageView? = null
//    private var device_test_tint_layout: TintConstraintLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.device_activity)
        viewModel.deviceInfoList.observe(this, Observer { deviceStatuses ->
            showDeviceInfo(deviceStatuses)
        })
        viewModel.inchData.observe(this, Observer { s -> deviceInchTv.text = s })
        findViewById<View>(R.id.device_calculate_btn).setOnClickListener {
            setTheme(R.style.BlackTheme)
            device_test_tint_iv.setImageResource(R.mipmap.ic_looks_2)
            viewModel.calculatePhysicalSize(0)
        }
        viewModel.loadDeviceInfo()
    }

    private fun showDeviceInfo(list: ArrayList<DeviceStatus>) {
        adapter?.setDataList(list) ?: DeviceAdapter(this, list).also {
            deviceInfoRecyclerView.layoutManager = LinearLayoutManager(this)
            adapter = it
            deviceInfoRecyclerView.adapter = adapter
        }
    }

    override fun setTheme(resId: Int) {
        super.setTheme(resId)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ThemeUtils.refreshUI(this)
        }
    }
}