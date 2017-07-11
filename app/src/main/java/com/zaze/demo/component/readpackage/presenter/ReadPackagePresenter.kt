package com.zaze.demo.component.readpackage.presenter

/**
 * Description :

 * @author : zaze
 * *
 * @version : 2017-04-17 05:15 1.0
 */
interface ReadPackagePresenter {
    fun getAllApkFile(dir: String)

    fun getAllInstallApp()

    fun getAllSystemApp()

    fun filterApp(matchStr: String)

}
