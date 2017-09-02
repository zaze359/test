package com.zaze.demo.component.readpackage.presenter.impl

import android.content.pm.ApplicationInfo
import com.zaze.common.base.ZBaseApplication
import com.zaze.common.base.ZBasePresenter
import com.zaze.demo.R
import com.zaze.demo.component.readpackage.presenter.ReadPackagePresenter
import com.zaze.demo.component.readpackage.view.ReadPackageView
import com.zaze.demo.model.entity.PackageEntity
import com.zaze.utils.ZAppUtil
import com.zaze.utils.ZCommand
import com.zaze.utils.ZStringUtil
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Description :

 * @author : zaze
 * *
 * @version : 2017-04-17 05:15 1.0
 */
class ReadPackagePresenterImpl(view: ReadPackageView) : ZBasePresenter<ReadPackageView>(view), ReadPackagePresenter {
    val showList = ArrayList<PackageEntity>()
    override fun filterApp(matchStr: String) {
        Observable.fromCallable({
            showList
        }).subscribeOn(Schedulers.io())
                .map({
                    v ->
                    val list = ArrayList<PackageEntity>()
                    v.filter {
                        it.packageName!!.contains(ZStringUtil.parseString(matchStr).toLowerCase())
                    }.mapTo(list) {
                        initEntity(it.packageName!!)
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    v ->
                    view.showPackageList(v)
                })
    }

    override fun getAllApkFile(dir: String) {
        showList.clear()
        val result = ZCommand.execRootCmdForRes("ls $dir *.apk")
        val apkList = result.successList
        for (apk in apkList) {
            val packageInfo = ZAppUtil.getPackageArchiveInfo(ZBaseApplication.getInstance(), apk)
            if (packageInfo != null) {
                showList.add(initEntity(packageInfo.packageName))
            }
        }
        view.showPackageList(showList)
    }

    override fun getAllInstallApp() {
        val appList = ZAppUtil.getInstalledApplications(ZBaseApplication.getInstance())
        showList.clear()
        appList.mapTo(showList) { initEntity(it.packageName) }
        view.showPackageList(showList)
    }

    override fun getAllSystemApp() {
        val appList = ZAppUtil.getInstalledApplications(ZBaseApplication.getInstance())
        showList.clear()
        appList.filter { it.flags and ApplicationInfo.FLAG_SYSTEM > 0 }
                .mapTo(showList) { initEntity(it.packageName) }
        view.showPackageList(showList)
    }

    override fun getAssignInstallApp() {
        val appList = ZAppUtil.getInstalledApplications(ZBaseApplication.getInstance())
        showList.clear()
        val keepList = view.getStringArray(R.array.system_keep_app).toList()
        appList.filter { keepList.contains(it.packageName) }.mapTo(showList) { initEntity(it.packageName) }
        view.showPackageList(showList)
    }


    fun initEntity(packageName: String): PackageEntity {
        val packageEntity = PackageEntity()
        val application = ZAppUtil.getApplicationInfo(ZBaseApplication.getInstance(), packageName)
        if (application != null) {
            packageEntity.sourceDir = application.sourceDir
        }
        packageEntity.packageName = packageName
        packageEntity.appName = ZAppUtil.getAppName(ZBaseApplication.getInstance(), packageName)
        packageEntity.versionName = ZAppUtil.getAppVersionName(ZBaseApplication.getInstance(), packageName)
        packageEntity.versionCode = ZAppUtil.getAppVersionCode(ZBaseApplication.getInstance(), packageName)
        return packageEntity
    }
}
