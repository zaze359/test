package com.zaze.demo.component.readpackage.ui


import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import com.zaze.common.adapter.third.UltimateRecyclerViewHelper
import com.zaze.common.base.ZBaseActivity
import com.zaze.common.widget.head.ZOrientation
import com.zaze.demo.R
import com.zaze.demo.component.readpackage.adapter.ReadPackageAdapter
import com.zaze.demo.component.readpackage.presenter.ReadPackagePresenter
import com.zaze.demo.component.readpackage.presenter.impl.ReadPackagePresenterImpl
import com.zaze.demo.component.readpackage.view.ReadPackageView
import com.zaze.demo.model.entity.PackageEntity
import com.zaze.utils.ZOnClickHelper
import kotlinx.android.synthetic.main.activity_read_package.*

/**
 * Description :

 * @author : zaze
 * *
 * @version : 2017-04-17 05:15 1.0
 */
class ReadPackageActivity : ZBaseActivity(), ReadPackageView {
    private var presenter: ReadPackagePresenter? = null
    private var adapter: ReadPackageAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_package)
        headWidget.setText("查看包名", ZOrientation.CENTER)
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

    override fun showPackageList(list: List<PackageEntity>) {

        val showList = ArrayList<PackageEntity>()
        list.filter { !TextUtils.isEmpty(it.sourceDir) }.mapTo(showList) { it }
        if (adapter == null) {
            adapter = ReadPackageAdapter(this, showList)
            package_recycle_view!!.layoutManager = LinearLayoutManager(this)
            UltimateRecyclerViewHelper.init(package_recycle_view)
            package_recycle_view.setAdapter(adapter)
        } else {
            adapter!!.setDataList(showList)
        }

    }
}
