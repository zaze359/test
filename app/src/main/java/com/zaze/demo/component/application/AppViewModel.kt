package com.zaze.demo.component.application

import android.app.Application
import android.content.pm.ApplicationInfo
import android.os.Build
import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import com.zaze.common.base.AbsAndroidViewModel
import com.zaze.common.base.BaseApplication
import com.zaze.common.base.ext.get
import com.zaze.common.base.ext.set
import com.zaze.common.thread.ThreadPlugins
import com.zaze.demo.R
import com.zaze.demo.app.MyApplication
import com.zaze.demo.debug.AppShortcut
import com.zaze.demo.debug.ApplicationManager
import com.zaze.demo.util.plugins.rx.MyObserver
import com.zaze.utils.AppUtil
import com.zaze.utils.FileUtil
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.operators.observable.ObservableFromIterable

/**
 * Description :
 * @author : ZAZE
 * @version : 2019-05-20 - 13:04
 */
class AppViewModel(application: Application) : AbsAndroidViewModel(application) {
    companion object {
        val apkDir = "${FileUtil.getSDCardRoot()}/zaze/apk"
        val baseDir = "${FileUtil.getSDCardRoot()}/zaze/${Build.MODEL}"
        val existsFile = "$baseDir/exists.xml"
        val unExistsFile = "$baseDir/unExists.xml"
        val extractFile = "$baseDir/extract.xml"
        val allFile = "$baseDir/all.xml"
    }

    val packageSet = HashMap<String, AppShortcut>()
    val appData = MutableLiveData<List<AppShortcut>>()
    // --------------------------------------------------
    fun loadAppList() {
        if (!isLoading()) {
            dataLoading.set(true)
            showProgress("load apps")
            Observable.fromCallable {
                // --------------------------------------------------
                packageSet.clear()
                val allAppList = AppUtil.getInstalledApplications(MyApplication.getInstance())
                // --------------------------------------------------
                FileUtil.deleteFile(baseDir)
//        FileUtil.deleteFile(unExistsFile)
//        FileUtil.deleteFile(extractFile)
//        FileUtil.deleteFile(allFile)
                // --------------------------------------------------
                loadAllInstallApp(allAppList, packageSet)
//            loadSystemApp(allAppList, packageSet)
//        loadUnSystemApp(allAppList, packageSet)
                // --------------------------------------------------
                packageSet.remove(BaseApplication.getInstance().packageName)
                // --------------------------------------------------
                val filterSet = HashSet<String>()
                filterSet.addAll(getStringArray(R.array.xuehai_keep_app).toList())
                filterSet.addAll(getStringArray(R.array.un_keep_app).toList())
                filterSet.addAll(getStringArray(R.array.android_keep_app).toList())
                filterSet.addAll(getStringArray(R.array.android_un_keep_app).toList())
                filterSet.addAll(getStringArray(R.array.samsung_keep_app).toList())
                filterSet.addAll(getStringArray(R.array.samsung_un_keep_app).toList())
                filterSet.addAll(getStringArray(R.array.huawei_keep_app).toList())
                filterSet.addAll(getStringArray(R.array.huawei_un_keep_app).toList())
                filterSet.addAll(getStringArray(R.array.lenovo_keep_app).toList())
                filterSet.addAll(getStringArray(R.array.lenovo_un_keep_app).toList())
                // --------------------------------------------------
                filterSet.addAll(getStringArray(R.array.test_app).toList())
                // 添加规则--------------------------------------------------
//        filterSet.mapTo(packageList) { it }
                // 移除规则--------------------------------------------------
//            filterSet.forEach {
//                packageSet.remove(it)
//            }
                show(packageSet.values)
            }.subscribeOn(ThreadPlugins.ioScheduler())
                    .doFinally {
                        dataLoading.set(false)
                        hideProgress()
                    }
                    .subscribe(MyObserver(compositeDisposable))
        }
    }

    private fun show(apps: MutableCollection<AppShortcut>) {
        appData.postValue(apps.filter {
            !TextUtils.isEmpty(it.sourceDir)
        })
    }

    private fun loadAllInstallApp(appList: List<ApplicationInfo>, packageSet: HashMap<String, AppShortcut>) {
        appList.forEach {
            packageSet[it.packageName] = ApplicationManager.getAppShortcut(it.packageName)
        }
    }

    private fun loadSystemApp(appList: List<ApplicationInfo>, packageSet: HashMap<String, AppShortcut>) {
        appList.filter { it.flags and ApplicationInfo.FLAG_SYSTEM > 0 }
                .forEach {
                    packageSet[it.packageName] = ApplicationManager.getAppShortcut(it.packageName)
                }
    }

    private fun loadUnSystemApp(appList: List<ApplicationInfo>, packageSet: HashMap<String, AppShortcut>) {
        appList.filter { it.flags and ApplicationInfo.FLAG_SYSTEM <= 0 }
                .forEach {
                    packageSet[it.packageName] = ApplicationManager.getAppShortcut(it.packageName)
                }
    }

    fun loadSdcardApk() {
        if (!isLoading()) {
            showProgress("加载apk")
            dataLoading.set(true)
            Observable.fromCallable {
                val showList = ArrayList<AppShortcut>()
                FileUtil.searchFileBySuffix(apkDir, "apk", true).forEach { file ->
                    initEntity(ApplicationManager.getAppShortcutFormApk(file.absolutePath))?.let {
                        showList.add(it)
                    }
                }
                showList
            }.subscribeOn(ThreadPlugins.ioScheduler())
                    .map { showList ->
                        appData.postValue(showList.filter {
                            !TextUtils.isEmpty(it.sourceDir)
                        })

                    }
                    .doFinally {
                        dataLoading
                        hideProgress()
                    }
                    .subscribe(MyObserver(compositeDisposable))
        }
    }

    // --------------------------------------------------
    fun extractApp() {
        if (!isLoading()) {
            dataLoading.set(true)
            showProgress("正在提前数据....")
            Observable.fromCallable {
                val dataList = appData.get()
                if (dataList != null) {
                    FileUtil.deleteFile(extractFile)
                    val builder = StringBuilder()
                    for (entity in dataList) {
                        if (builder.isNotEmpty()) {
                            builder.append("\n")
                        }
                        builder.append("<item>${entity.packageName}</item><!--${entity.name}-->")
                    }
                    FileUtil.writeToFile(extractFile, builder.toString())
                }
            }.subscribeOn(ThreadPlugins.ioScheduler())
                    .doFinally {
                        dataLoading.set(false)
                        hideProgress()
                    }
                    .subscribe(MyObserver(compositeDisposable))
        }
    }

    fun filterApp(matchStr: String) {
        Observable.create<MutableCollection<AppShortcut>> { e ->
            e.onNext(packageSet.values)
            e.onComplete()
        }.subscribeOn(ThreadPlugins.ioScheduler())
                .flatMap {
                    ObservableFromIterable(it)
                }.filter {
                    it.packageName.contains(matchStr, true) || it.name.contains(matchStr, true)
                }.toList()
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : MyObserver<MutableCollection<AppShortcut>>(compositeDisposable) {
                    override fun onNext(result: MutableCollection<AppShortcut>) {
                        super.onNext(result)
                        show(result)
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        show(HashSet())
                    }
                })
    }

    // --------------------------------------------------
    private fun initEntity(appShortcut: AppShortcut?): AppShortcut? {
        return appShortcut?.also {
            val packageName = appShortcut.packageName
            if (appShortcut.isInstalled) {
                FileUtil.writeToFile(existsFile, "<item>$packageName</item><!--${appShortcut.name}-->\n", true)
            } else {
                FileUtil.writeToFile(unExistsFile, "<item>$packageName</item>\n", true)
            }
            FileUtil.writeToFile(allFile, "<item>$packageName</item><!--${appShortcut.name}-->\n", true)
        }
    }
}