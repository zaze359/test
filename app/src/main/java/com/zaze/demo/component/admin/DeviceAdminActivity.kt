package com.zaze.demo.component.admin

import android.app.Activity
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.zaze.common.base.AbsActivity
import com.zaze.common.base.ext.myViewModels
import com.zaze.demo.R
import com.zaze.utils.log.ZLog
import kotlinx.android.synthetic.main.device_admin_act.*

/**
 * Description :
 * @author : zaze
 * @version : 2020-12-21 - 14:12
 */
class DeviceAdminActivity : AbsActivity() {
    companion object {
        const val TAG = "DeviceAdminActivity"
    }

    private val viewModel: DeviceAdminViewModel by myViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.device_admin_act)
        viewModel.itemsData.observe(this, Observer {
            deviceAdminLayout.layoutManager = LinearLayoutManager(this)
            deviceAdminLayout.adapter = DeviceAdminAdapter(this, it)
        })
        viewModel.activeAction.observe(this, Observer {
            viewModel.addDeviceAdmin(this, 0)
        })
        addDeviceAdminBtn.setOnClickListener {
            viewModel.addDeviceAdmin(this, 0)
        }
        removeDeviceAdminBtn.setOnClickListener {
            viewModel.removeDeviceAdmin()
        }
        viewModel.loadItems()
    }

}