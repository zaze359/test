package com.zaze.demo.component.readpackage.presenter.impl

import android.content.pm.ApplicationInfo
import android.os.Build
import com.zaze.common.base.ZBasePresenter
import com.zaze.demo.R
import com.zaze.demo.app.MyApplication
import com.zaze.demo.component.readpackage.presenter.ReadPackagePresenter
import com.zaze.demo.component.readpackage.view.ReadPackageView
import com.zaze.demo.model.entity.PackageEntity
import com.zaze.utils.ZAppUtil
import com.zaze.utils.ZFileUtil
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

    val baseDir = "${ZFileUtil.getSDCardRoot()}/zaze/${Build.MODEL}"
    val existsFile = "$baseDir/exists.xml"
    val unExistsFile = "$baseDir/unExists.xml"
    val extractFile = "$baseDir/z_app.xml"

    val packageList = HashSet<String>()
    // --------------------------------------------------
    override fun getAppList() {
//        getAllApkFile("/sdcard/")
        getAllInstallApp()
//        getUnSystemApp()
//        getAllSystemApp()
//        getAssignInstallApp()
        val showList = ArrayList<PackageEntity>()
        packageList.mapTo(showList) {
            initEntity(it)
        }
        view.showPackageList(showList)
    }

    override fun getAllApkFile(dir: String) {
        reset()
//        val result = ZCommand.execRootCmdForRes("ls $dir *.apk")
//        val apkList = result.successList
//        for (apk in apkList) {
//            initEntity(apk)
//        }
    }

    override fun getAllInstallApp() {
        reset()
        val appList = ZAppUtil.getInstalledApplications(MyApplication.getInstance())
        appList.mapTo(packageList) { it.packageName }
    }

    override fun getAllSystemApp() {
        val appList = ZAppUtil.getInstalledApplications(MyApplication.getInstance())
        reset()
        appList.filter { it.flags and ApplicationInfo.FLAG_SYSTEM > 0 }
                .mapTo(packageList) { it.packageName }
    }

    override fun getUnSystemApp() {
        reset()
        val appList = ZAppUtil.getInstalledApplications(MyApplication.getInstance())
        appList.filter { it.flags and ApplicationInfo.FLAG_SYSTEM <= 0 }
                .mapTo(packageList) { it.packageName }
    }

    override fun getAssignInstallApp() {
        reset()
        getAllSystemApp()
        val filterSet = HashSet<String>()
        filterSet.addAll(view.getStringArray(R.array.xuehai_keep_app).toList())
        filterSet.addAll(view.getStringArray(R.array.un_keep_app).toList())
        filterSet.addAll(view.getStringArray(R.array.android_keep_app).toList())
        filterSet.addAll(view.getStringArray(R.array.android_un_keep_app).toList())
//        filterSet.addAll(view.getStringArray(R.array.samsung_keep_app).toList())
//        filterSet.addAll(view.getStringArray(R.array.samsung_un_keep_app).toList())
        filterSet.addAll(view.getStringArray(R.array.huawei_keep_app).toList())
        filterSet.addAll(view.getStringArray(R.array.huawei_un_keep_app).toList())
//        filterSet.addAll(view.getStringArray(R.array.lenovo_keep_app).toList())
//        filterSet.addAll(view.getStringArray(R.array.lenovo_un_keep_app).toList())
        //        filterSet.mapTo(packageList) { it }
        packageList.removeAll(filterSet)

    }

    // --------------------------------------------------
    private fun reset() {
        packageList.clear()
        ZFileUtil.deleteFile(existsFile)
        ZFileUtil.deleteFile(unExistsFile)
        ZFileUtil.deleteFile(extractFile)
    }

    // --------------------------------------------------
    override fun extract(dataList: List<PackageEntity>?) {
        if (dataList != null) {
            val packageEntityList = dataList
            val builder = StringBuilder()
            for (entity in packageEntityList) {
                if (builder.isNotEmpty()) {
                    builder.append("\n")
                }
                builder.append("<item>${entity.packageName}</item><!--${entity.appName}-->")
            }
            ZFileUtil.writeToFile(extractFile, builder.toString())
        }
    }

    override fun filterApp(matchStr: String) {
        Observable.fromCallable({
            packageList
        }).subscribeOn(Schedulers.io())
                .map({
                    v ->
                    val list = ArrayList<PackageEntity>()
                    v.filter {
                        it.contains(ZStringUtil.parseString(matchStr).toLowerCase())
                    }.mapTo(list) {
                        initEntity(it)
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    v ->
                    view.showPackageList(v)
                })
    }

    // --------------------------------------------------
    private fun initEntity(packageName: String): PackageEntity {
        val packageEntity = PackageEntity()
        val application = ZAppUtil.getApplicationInfo(MyApplication.getInstance(), packageName)
        packageEntity.packageName = packageName
        if (application != null) {
            packageEntity.sourceDir = application.sourceDir
            packageEntity.appName = ZAppUtil.getAppName(MyApplication.getInstance(), packageName)
            packageEntity.versionName = ZAppUtil.getAppVersionName(MyApplication.getInstance(), packageName)
            packageEntity.versionCode = ZAppUtil.getAppVersionCode(MyApplication.getInstance(), packageName)
            ZFileUtil.writeToFile(existsFile, "<item>$packageName</item>\n", true)
        } else {
            ZFileUtil.writeToFile(unExistsFile, "<item>$packageName</item>\n", true)
        }
        return packageEntity
    }
}
