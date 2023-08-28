package com.zaze.demo.component.admin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.zaze.common.base.AbsActivity
import com.zaze.demo.R
import com.zaze.demo.databinding.DeviceAdminActBinding
import com.zaze.utils.log.ZLog

/**
 * Description :
 * @author : zaze
 * @version : 2020-12-21 - 14:12
 */
class DeviceAdminActivity : AbsActivity() {
    companion object {
        const val TAG = "DeviceAdminActivity"
    }

    private val viewModel: DeviceAdminViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
            DataBindingUtil.setContentView<DeviceAdminActBinding>(this, R.layout.device_admin_act)
        viewModel.itemsData.observe(this) {
            binding.deviceAdminLayout.layoutManager = LinearLayoutManager(this)
            binding.deviceAdminLayout.adapter = DeviceAdminAdapter(this, it)
        }
//        viewModel.activeAction.observe(this) {
//            viewModel.addDeviceAdmin(this, 0)
//        }
        binding.addDeviceAdminBtn.setOnClickListener {
            viewModel.addDeviceAdmin(this, 0)
        }
        binding.removeDeviceAdminBtn.setOnClickListener {
            viewModel.removeDeviceAdmin()
        }
        viewModel.loadItems()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        ZLog.w(TAG, "onActivityResult: ${resultCode == Activity.RESULT_OK}")
    }

}