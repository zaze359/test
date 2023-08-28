package com.zaze.demo.feature.applications

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.android.material.shape.MaterialShapeDrawable
import com.zaze.common.base.AbsActivity
import com.zaze.common.base.ext.setupActionBar
import com.zaze.core.model.data.AppNavigation
import com.zaze.demo.feature.applications.databinding.ActivityAppListBinding
import com.zaze.utils.ZOnClickHelper
import dagger.hilt.android.AndroidEntryPoint

/**
 * Description :
 * @author : zaze
 *
 * @version : 2017-04-17 05:15 1.0
 */
@AndroidEntryPoint
@Route(path = AppNavigation.appListRoute)
class AppListActivity : AbsActivity() {
    private val viewModel: AppListViewModel by viewModels()
    private var adapter: AppListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAppListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBar(binding.appbarLayout.toolbar) {
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
            it.setNavigationOnClickListener {
                finish()
            }
        }

        viewModel.appData.observe(this@AppListActivity, Observer { appList ->
            binding.appCountTv.text = "查询到 ${appList.size}个应用"
            adapter ?: let {
                adapter = AppListAdapter(this@AppListActivity)
                binding.appRecycleView.layoutManager = LinearLayoutManager(this@AppListActivity)
                binding.appRecycleView.adapter = adapter
            }
            adapter?.submitList(appList)
        })

        ZOnClickHelper.setOnClickListener(binding.appExtractBtn) {
            viewModel.extractApp()
        }
        binding.appResolvingApkCb.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.loadSdcardApk()
            } else {
                viewModel.loadAppList()
            }
        }
        binding.appSearchEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.filterApp(s.toString())
            }
        })
        viewModel.loadAppList()
    }
}
