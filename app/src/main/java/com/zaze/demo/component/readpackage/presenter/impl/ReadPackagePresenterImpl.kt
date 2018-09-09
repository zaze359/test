package com.zaze.demo.component.readpackage.presenter.impl

import android.content.pm.ApplicationInfo
import android.os.Build
import com.zaze.common.base.mvp.BaseMvpPresenter
import com.zaze.demo.R
import com.zaze.demo.app.MyApplication
import com.zaze.demo.component.readpackage.presenter.ReadPackagePresenter
import com.zaze.demo.component.readpackage.view.ReadPackageView
import com.zaze.demo.model.entity.PackageEntity
import com.zaze.utils.AppUtil
import com.zaze.utils.FileUtil
import com.zaze.utils.ZStringUtil
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Description :

 * @author : zaze
 * *
 * @version : 2017-04-17 05:15 1.0
 */
class ReadPackagePresenterImpl(view: ReadPackageView) : BaseMvpPresenter<ReadPackageView>(view), ReadPackagePresenter {

    val baseDir = "${FileUtil.getSDCardRoot()}/zaze/${Build.MODEL}"
    val existsFile = "$baseDir/exists.xml"
    val unExistsFile = "$baseDir/unExists.xml"
    val extractFile = "$baseDir/extract.xml"
    val allFile = "$baseDir/all.xml"

    val packageList = HashSet<String>()
    // --------------------------------------------------
    override fun getAppList() {
//        getAllApkFile("/sdcard/")
        getAllInstallApp()
//        getUnSystemApp()
//        getSystemApp()
//        getAssignInstallApp()
        val showList = ArrayList<PackageEntity>()
        packageList.mapTo(showList) {
            initEntity(it)
        }
        view.showPackageList(showList)
    }

    override fun getAllApkFile(dir: String) {
        reset()
    }

    override fun getAllInstallApp() {
        reset()
        val appList = AppUtil.getInstalledApplications(MyApplication.getInstance())
        appList.mapTo(packageList) { it.packageName }
    }

    override fun getSystemApp() {
        val appList = AppUtil.getInstalledApplications(MyApplication.getInstance())
        reset()
        appList.filter { it.flags and ApplicationInfo.FLAG_SYSTEM > 0 }
                .mapTo(packageList) { it.packageName }
    }

    override fun getUnSystemApp() {
        reset()
        val appList = AppUtil.getInstalledApplications(MyApplication.getInstance())
        appList.filter { it.flags and ApplicationInfo.FLAG_SYSTEM <= 0 }
                .mapTo(packageList) { it.packageName }
    }

    override fun getAssignInstallApp() {
        reset()
        val filterSet = HashSet<String>()
        filterSet.addAll(view.getStringArray(R.array.xuehai_keep_app).toList())
        filterSet.addAll(view.getStringArray(R.array.un_keep_app).toList())
        filterSet.addAll(view.getStringArray(R.array.android_keep_app).toList())
        filterSet.addAll(view.getStringArray(R.array.android_un_keep_app).toList())
//        filterSet.addAll(view.getStringArray(R.array.samsung_keep_app).toList())
//        filterSet.addAll(view.getStringArray(R.array.samsung_un_keep_app).toList())
//        filterSet.addAll(view.getStringArray(R.array.samsung_special_app).toList())
//        filterSet.addAll(view.getStringArray(R.array.huawei_keep_app).toList())
//        filterSet.addAll(view.getStringArray(R.array.huawei_un_keep_app).toList())
//        filterSet.addAll(view.getStringArray(R.array.lenovo_keep_app).toList())
//        filterSet.addAll(view.getStringArray(R.array.lenovo_un_keep_app).toList())
        // --------------------------------------------------
//        filterSet.addAll(view.getStringArray(R.array.test_app).toList())
        // --------------------------------------------------
//        filterSet.mapTo(packageList) { it }
        // --------------------------------------------------
//        getAllInstallApp()
        getSystemApp()
//        getUnSystemApp()
        packageList.removeAll(filterSet)

    }

    // --------------------------------------------------
    private fun reset() {
        packageList.clear()
        FileUtil.deleteFile(baseDir)
//        FileUtil.deleteFile(unExistsFile)
//        FileUtil.deleteFile(extractFile)
//        FileUtil.deleteFile(allFile)
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
            FileUtil.writeToFile(extractFile, builder.toString())
        }
    }

    override fun filterApp(matchStr: String) {
        Observable.fromCallable({
            packageList
        }).subscribeOn(Schedulers.io())
                .map({ v ->
                    val list = ArrayList<PackageEntity>()
                    v.filter {
                        it.contains(ZStringUtil.parseString(matchStr).toLowerCase())
                    }.mapTo(list) {
                        initEntity(it)
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ v ->
                    view.showPackageList(v)
                })
    }

    // --------------------------------------------------
    private fun initEntity(packageName: String): PackageEntity {
        val packageEntity = PackageEntity()
        val application = AppUtil.getApplicationInfo(MyApplication.getInstance(), packageName)
        packageEntity.packageName = packageName
        if (application != null) {
            packageEntity.sourceDir = application.sourceDir
            packageEntity.appName = AppUtil.getAppName(MyApplication.getInstance(), packageName)
            packageEntity.versionName = AppUtil.getAppVersionName(MyApplication.getInstance(), packageName)
            packageEntity.versionCode = AppUtil.getAppVersionCode(MyApplication.getInstance(), packageName)
            FileUtil.writeToFile(existsFile, "<item>$packageName</item><!--${packageEntity.appName}-->\n", true)
        } else {
            FileUtil.writeToFile(unExistsFile, "<item>$packageName</item>\n", true)
        }
        FileUtil.writeToFile(allFile, "<item>$packageName</item><!--${packageEntity.appName}-->\n", true)
        return packageEntity
    }
}
