package com.zaze.demo.component.readpackage.ui


import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import androidx.recyclerview.widget.LinearLayoutManager
import com.zaze.common.base.BaseActivity
import com.zaze.demo.R
import com.zaze.demo.component.readpackage.adapter.ReadPackageAdapter
import com.zaze.demo.component.readpackage.presenter.ReadPackagePresenter
import com.zaze.demo.component.readpackage.presenter.impl.ReadPackagePresenterImpl
import com.zaze.demo.component.readpackage.view.ReadPackageView
import com.zaze.demo.debug.AppShortcut
import com.zaze.utils.ZOnClickHelper
import kotlinx.android.synthetic.main.read_package_activity.*

/**
 * Description :

 * @author : zaze
 * *
 * @version : 2017-04-17 05:15 1.0
 */
class ReadPackageActivity : BaseActivity(), ReadPackageView {
    private var presenter: ReadPackagePresenter? = null
    private var adapter: ReadPackageAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.read_package_activity)
        presenter = ReadPackagePresenterImpl(this)
        presenter?.getAppList()
        ZOnClickHelper.setOnClickListener(package_extract_btn) {
            presenter?.extract(adapter?.dataList)
        }

        package_input_et.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                presenter!!.filterApp(s.toString())
            }
        })
    }

    override fun showPackageList(list: List<AppShortcut>) {
        val showList = ArrayList<AppShortcut>()
        list.asSequence().filter { !TextUtils.isEmpty(it.sourceDir) }.mapTo(showList) { it }
        if (adapter == null) {
            adapter = ReadPackageAdapter(this, showList)
            package_recycle_view!!.layoutManager = LinearLayoutManager(this)
            package_recycle_view.adapter = adapter
        } else {
            adapter!!.setDataList(showList)
        }
    }
}
