package com.zaze.demo.component.readpackage.presenter

import com.zaze.demo.model.entity.PackageEntity

/**
 * Description :
 * @author : zaze
 * @version : 2017-04-17 05:15 1.0
 */
interface ReadPackagePresenter {

    fun getAppList()

    fun getAllApkFile(dir: String)

    fun getAllInstallApp()

    fun getAllSystemApp()

    fun getUnSystemApp()

    fun getAssignInstallApp()

    fun filterApp(matchStr: String)

    fun extract(dataList: List<PackageEntity>?)


}
