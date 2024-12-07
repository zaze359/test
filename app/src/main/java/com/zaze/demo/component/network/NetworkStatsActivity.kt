package com.zaze.demo.component.network

import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.zaze.common.base.AbsActivity
import com.zaze.common.base.ext.obtainViewModel
import com.zaze.demo.R
import com.zaze.demo.databinding.NetworkStatsActBinding
import com.zaze.demo.debug.NetTrafficStats


/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-12-22 04:36 1.0
 */
class NetworkStatsActivity : AbsActivity() {
    private var adapter: NetworkStatsAdapter? = null
    private lateinit var viewModel: NetworkStatsViewModel
    private lateinit var databinding: NetworkStatsActBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        databinding = DataBindingUtil.setContentView(this, R.layout.network_stats_act)
        databinding.lifecycleOwner = this
        databinding.viewModel = obtainViewModel(NetworkStatsViewModel::class.java).apply {
//            networkTraffic.observe(this@NetworkStatsActivity, Observer {
//                showNetworkStats(it)
//            })
            this@NetworkStatsActivity.viewModel = this
        }
        adapter = NetworkStatsAdapter(this, emptyList())
        databinding.networkStatsRecycler.layoutManager = LinearLayoutManager(this)
        databinding.networkStatsRecycler.adapter = adapter

        databinding.networkStatsBtn.setOnClickListener {
            hasPermissionToReadNetworkStats()
        }

        databinding.networkStatsRefreshLayout.setOnRefreshListener {
            viewModel.load()
        }
        if (hasPermissionToReadNetworkStats()) {
            databinding.networkStatsRefreshLayout.post {
                viewModel.load()
            }
        }
    }

    private fun hasPermissionToReadNetworkStats(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }
        val appOps = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            android.os.Process.myUid(), packageName
        )
        if (mode == AppOpsManager.MODE_ALLOWED) {
            return true
        }
        // 打开“有权查看使用情况的应用”页面
        val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
        startActivity(intent)
        return false
    }

    private fun showNetworkStats(netTrafficStats: Collection<NetTrafficStats>?) {
        adapter?.setDataList(netTrafficStats) ?: let {
            adapter = NetworkStatsAdapter(this, netTrafficStats)
        }
    }

}