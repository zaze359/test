package com.zaze.demo.kotlin.commen.readpackage.ui


import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import com.zaze.aarrepo.commons.base.ZBaseActivity
import com.zaze.aarrepo.commons.log.ZLog
import com.zaze.aarrepo.commons.widget.head.HeadFace
import com.zaze.aarrepo.utils.FileUtil
import com.zaze.aarrepo.utils.helper.OnClickHelper
import com.zaze.aarrepo.utils.helper.UltimateRecyclerViewHelper
import com.zaze.demo.R
import com.zaze.demo.kotlin.commen.readpackage.adapter.ReadPackageAdapter
import com.zaze.demo.kotlin.commen.readpackage.presenter.ReadPackagePresenter
import com.zaze.demo.kotlin.commen.readpackage.presenter.impl.ReadPackagePresenterImpl
import com.zaze.demo.kotlin.commen.readpackage.view.ReadPackageView
import com.zaze.demo.model.entity.PackageEntity
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

        headWidget.setText("查看包名", HeadFace.CENTER)
        presenter = ReadPackagePresenterImpl(this)
        //        presenter.getAllApkFile("/sdcard/");
        //        presenter.getAllSystemApp();
        presenter!!.getAllInstallApp()
        OnClickHelper.setOnClickListener(package_extract_btn) {
            val keepApp = adapter!!.packageListStr
            ZLog.i("package", keepApp)
            FileUtil.write2SDCardFile("/zaze/keepApp.txt", keepApp)
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
        if (adapter == null) {
            adapter = ReadPackageAdapter(this, list)
            package_recycle_view!!.layoutManager = LinearLayoutManager(this)
            UltimateRecyclerViewHelper.init(package_recycle_view)
            package_recycle_view.setAdapter(adapter)
        } else {
            adapter!!.setDataList(list)
        }

    }
}
