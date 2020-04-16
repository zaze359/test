package com.zaze.demo.component.application


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.zaze.common.base.AbsActivity
import com.zaze.common.base.ext.obtainViewModel
import com.zaze.demo.R
import com.zaze.utils.ZOnClickHelper
import kotlinx.android.synthetic.main.app_act.*

/**
 * Description :

 * @author : zaze
 * *
 * @version : 2017-04-17 05:15 1.0
 */
class AppActivity : AbsActivity() {
    private lateinit var viewModel: AppViewModel
    private var adapter: AppAdapter? = null


    override fun init(savedInstanceState: Bundle?) {
        setContentView(R.layout.app_act)
        viewModel = obtainViewModel(AppViewModel::class.java).apply {
            appData.observe(this@AppActivity, Observer { appList ->
                appCountTv.text = "查询到 ${appList.size}个应用"
                adapter?.setDataList(appList) ?: let {
                    adapter = AppAdapter(this@AppActivity, appList)
                    appRecycleView.layoutManager = LinearLayoutManager(this@AppActivity)
                    appRecycleView.adapter = adapter
                }
            })
            progress.observe(this@AppActivity, Observer {
                progress(it)
            })
        }

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
