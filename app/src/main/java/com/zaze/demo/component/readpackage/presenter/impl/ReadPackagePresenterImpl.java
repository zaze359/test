package com.zaze.demo.component.readpackage.presenter.impl;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;

import com.zaze.aarrepo.commons.base.ZBaseApplication;
import com.zaze.aarrepo.commons.base.ZBasePresenter;
import com.zaze.aarrepo.utils.AppUtil;
import com.zaze.aarrepo.utils.RootCmd;
import com.zaze.demo.component.readpackage.presenter.ReadPackagePresenter;
import com.zaze.demo.component.readpackage.view.ReadPackageView;
import com.zaze.demo.model.entity.PackageEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-04-17 05:15 1.0
 */
public class ReadPackagePresenterImpl extends ZBasePresenter<ReadPackageView> implements ReadPackagePresenter {

    public ReadPackagePresenterImpl(ReadPackageView view) {
        super(view);
    }

    @Override
    public void getAllApkFile(String dir) {
        List<PackageEntity> list = new ArrayList<>();
        RootCmd.CommandResult result = RootCmd.execRootCmdForRes("ls " + dir + " *.apk");
        List<String> apkList = result.msgList;
        for (String apk : apkList) {
            PackageInfo packageInfo = AppUtil.getPackageArchiveInfo(ZBaseApplication.getInstance(), apk);
            if (packageInfo != null) {
                PackageEntity packageEntity = new PackageEntity();
                packageEntity.setPackageName(packageInfo.packageName);
                list.add(packageEntity);
            }
        }
        view.showPackageList(list);
    }

    @Override
    public void getAllInstallApp() {
        List<ApplicationInfo> appList = AppUtil.getInstalledApplications(ZBaseApplication.getInstance(), 0);
        List<PackageEntity> list = new ArrayList<>();
        for (ApplicationInfo applicationInfo : appList) {
//            PackageInfo packageInfo = AppUtil.getPackageArchiveInfo(ZBaseApplication.getInstance(), apk);
            PackageEntity packageEntity = new PackageEntity();
            packageEntity.setPackageName(applicationInfo.packageName);
//                packageEntity.setPackageName(applicationInfo.packageName);
            list.add(packageEntity);
        }
        view.showPackageList(list);
    }

    @Override
    public void getAllSystemApp() {
        List<ApplicationInfo> appList = AppUtil.getInstalledApplications(ZBaseApplication.getInstance(), 0);
        List<PackageEntity> list = new ArrayList<>();
        for (ApplicationInfo applicationInfo : appList) {
            if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0) {
                PackageEntity packageEntity = new PackageEntity();
                packageEntity.setPackageName(applicationInfo.packageName);
//                packageEntity.setPackageName(applicationInfo.packageName);
                list.add(packageEntity);
            }
        }
        view.showPackageList(list);

    }

}
