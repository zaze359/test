package com.zaze.demo.component.device

import android.content.res.Resources
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.zaze.common.base.AbsActivity
import com.zaze.common.base.ext.initToolbar
import com.zaze.demo.R
import com.zaze.demo.databinding.DeviceActivityBinding
import com.zaze.core.designsystem.util.ThemeUtils
import com.zaze.demo.debug.kotlin.Temp
import com.zaze.utils.DisplayUtil
import dagger.hilt.android.AndroidEntryPoint

/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-01-22 01:39 1.0
 */
@AndroidEntryPoint
class DeviceActivity : AbsActivity() {
    private var adapter: DeviceAdapter? = null

    val viewModel: DeviceViewModel by viewModels()

    //    private var device_test_tint_iv: TintImageView? = null
//    private var device_test_tint_layout: TintConstraintLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DeviceActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initToolbar(binding.toolbar)
        viewModel.deviceInfoList.observe(this, Observer { list ->
            adapter?.setDataList(list) ?: DeviceAdapter(this, list).also {
                binding.deviceInfoRecyclerView.layoutManager = LinearLayoutManager(this)
                adapter = it
                binding.deviceInfoRecyclerView.adapter = adapter
            }
        })
        viewModel.inchData.observe(this, Observer { s -> binding.deviceInchTv.text = s })
        binding.deviceCalculateBtn.setOnClickListener {
            setTheme(R.style.BlackTheme)
            binding.deviceTestTintIv.setImageResource(R.drawable.ic_looks_2)
            viewModel.calculatePhysicalSize(0)
            Temp.createDimensBySW(800, DisplayUtil.getScreenWidthDp().toInt())
//            com.zaze.demo.debug.kotlin.Temp()
        }
        viewModel.loadDeviceInfo()
    }

    override fun setTheme(resId: Int) {
        super.setTheme(resId)
        ThemeUtils.refreshUI(this)
    }

    override fun onApplyThemeResource(theme: Resources.Theme?, resid: Int, first: Boolean) {
        super.onApplyThemeResource(theme, resid, first)
    }
}