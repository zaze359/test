package com.zaze.demo.kotlin.commen.readpackage.presenter.impl

import android.content.pm.ApplicationInfo
import com.zaze.aarrepo.commons.base.ZBaseApplication
import com.zaze.aarrepo.commons.base.ZBasePresenter
import com.zaze.aarrepo.utils.AppUtil
import com.zaze.aarrepo.utils.RootCmd
import com.zaze.aarrepo.utils.StringUtil
import com.zaze.demo.kotlin.commen.readpackage.presenter.ReadPackagePresenter
import com.zaze.demo.kotlin.commen.readpackage.view.ReadPackageView
import com.zaze.demo.model.entity.PackageEntity
import rx.Observable
import rx.functions.Func1
import java.util.concurrent.Callable

/**
 * Description :

 * @author : zaze
 * *
 * @version : 2017-04-17 05:15 1.0
 */
class ReadPackagePresenterImpl(view: ReadPackageView) : ZBasePresenter<ReadPackageView>(view), ReadPackagePresenter {
    override fun filterApp(matchStr: String) {
//        Observable.fromCallable(Callable<List<ApplicationInfo>> {
//            AppKeepUtil.getDeleteList()
//        })

        Observable.fromCallable(Callable<List<ApplicationInfo>> {
            AppUtil.getInstalledApplications(ZBaseApplication.getInstance(), 0)
        }).flatMap(Func1<List<ApplicationInfo>, Observable<ApplicationInfo>> {
            null
        })


        val appList = AppUtil.getInstalledApplications(ZBaseApplication.getInstance(), 0)
        val list = ArrayList<PackageEntity>()
        for (app in appList) {
            if (app.packageName.contains(StringUtil.parseString(matchStr).toLowerCase())) {
                list.add(initEntity(app.packageName))
            }
        }
        view.showPackageList(list)
    }

    override fun getAllApkFile(dir: String) {
        val list = ArrayList<PackageEntity>()
        val result = RootCmd.execRootCmdForRes("ls $dir *.apk")
        val apkList = result.msgList
        for (apk in apkList) {
            val packageInfo = AppUtil.getPackageArchiveInfo(ZBaseApplication.getInstance(), apk)
            if (packageInfo != null) {
                list.add(initEntity(packageInfo.packageName))
            }
        }
        view.showPackageList(list)
    }

    override fun getAllInstallApp() {
        val appList = AppUtil.getInstalledApplications(ZBaseApplication.getInstance(), 0)
        val list = ArrayList<PackageEntity>()
        for (applicationInfo in appList) {
            list.add(initEntity(applicationInfo.packageName))
        }
        view.showPackageList(list)
    }

    override fun getAllSystemApp() {
        val appList = AppUtil.getInstalledApplications(ZBaseApplication.getInstance(), 0)
        val list = ArrayList<PackageEntity>()
        for (applicationInfo in appList) {
            if (applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM > 0) {
                list.add(initEntity(applicationInfo.packageName))
            }
        }
        view.showPackageList(list)
    }

    fun initEntity(packageName: String): PackageEntity {
        val packageEntity = PackageEntity()
        packageEntity.packageName = packageName
        packageEntity.appName = AppUtil.getAppName(ZBaseApplication.getInstance(), packageName, "")
        packageEntity.versionName = AppUtil.getAppVersion(ZBaseApplication.getInstance(), packageName)
        packageEntity.versionCode = AppUtil.getAppVersionCode(ZBaseApplication.getInstance(), packageName)
        return packageEntity
    }
}
