package com.zaze.demo.component.application

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.zaze.common.base.AbsActivity
import com.zaze.common.base.ext.myViewModels
import com.zaze.demo.R
import com.zaze.utils.ZOnClickHelper
import kotlinx.android.synthetic.main.activity_app_list.*

/**
 * Description :

 * @author : zaze
 * *
 * @version : 2017-04-17 05:15 1.0
 */
class AppListActivity : AbsActivity() {
    private val viewModel: AppListViewModel by myViewModels()

    private var adapter: AppListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_list)
        viewModel.appData.observe(this@AppListActivity, Observer { appList ->
            appCountTv.text = "查询到 ${appList.size}个应用"
            adapter?.setDataList(appList) ?: let {
                adapter = AppListAdapter(this@AppListActivity, appList)
                appRecycleView.layoutManager = LinearLayoutManager(this@AppListActivity)
                appRecycleView.adapter = adapter
            }
        })

        ZOnClickHelper.setOnClickListener(appExtractBtn) {
            viewModel.extractApp()
        }
        appResolvingApkCb.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.loadSdcardApk()
            } else {
                viewModel.loadAppList()
            }
        }
        appSearchEt.addTextChangedListener(object : TextWatcher {
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