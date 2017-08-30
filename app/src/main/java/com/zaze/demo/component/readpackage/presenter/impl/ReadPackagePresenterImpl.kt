package com.zaze.demo.component.readpackage.presenter.impl

import android.content.pm.ApplicationInfo
import com.zaze.common.base.ZBaseApplication
import com.zaze.common.base.ZBasePresenter
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

    override fun filterApp(matchStr: String) {
        Observable.fromCallable({
            ZAppUtil.getInstalledApplications(ZBaseApplication.getInstance())
        }).subscribeOn(Schedulers.io())
                .map({
                    v ->
                    val list = ArrayList<PackageEntity>()
                    v.filter {
                        it.packageName.contains(ZStringUtil.parseString(matchStr).toLowerCase())
                    }.mapTo(list) {
                        initEntity(it.packageName)
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    v ->
                    view.showPackageList(v)
                })
    }

    override fun getAllApkFile(dir: String) {
        val list = ArrayList<PackageEntity>()
        val result = ZCommand.execRootCmdForRes("ls $dir *.apk")
        val apkList = result.successList
        for (apk in apkList) {
            val packageInfo = ZAppUtil.getPackageArchiveInfo(ZBaseApplication.getInstance(), apk)
            if (packageInfo != null) {
                list.add(initEntity(packageInfo.packageName))
            }
        }
        view.showPackageList(list)
    }

    override fun getAllInstallApp() {
        val appList = ZAppUtil.getInstalledApplications(ZBaseApplication.getInstance())
        val list = ArrayList<PackageEntity>()
        appList.mapTo(list) { initEntity(it.packageName) }
        view.showPackageList(list)
    }

    override fun getAllSystemApp() {
        val appList = ZAppUtil.getInstalledApplications(ZBaseApplication.getInstance())
        val list = ArrayList<PackageEntity>()
        appList
                .filter { it.flags and ApplicationInfo.FLAG_SYSTEM > 0 }
                .mapTo(list) { initEntity(it.packageName) }
        view.showPackageList(list)
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
