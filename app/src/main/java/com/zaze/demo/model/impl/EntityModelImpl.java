package com.zaze.demo.model.impl;


import com.zaze.demo.component.cache.ui.CacheActivity;
import com.zaze.demo.component.checknet.ui.CheckNetActivity;
import com.zaze.demo.component.device.ui.DeviceActivity;
import com.zaze.demo.component.fileexplorer.ui.FileExplorerActivity;
import com.zaze.demo.component.gps.ui.GpsActivity;
import com.zaze.demo.component.okhttp.ui.OkHttpActivity;
import com.zaze.demo.component.preference.MyPreferenceActivity;
import com.zaze.demo.component.progress.ProgressActivity;
import com.zaze.demo.component.provider.ui.ProviderActivity;
import com.zaze.demo.component.readpackage.ui.ReadPackageActivity;
import com.zaze.demo.component.rxandroid.RxAndroidActivity;
import com.zaze.demo.component.task.ui.TaskActivity;
import com.zaze.demo.component.time.TimeActivity;
import com.zaze.demo.component.toolbar.ToolBarDemoActivity;
import com.zaze.demo.component.webview.ui.WebViewActivity;
import com.zaze.demo.component.xbus.ui.EventBusActivity;
import com.zaze.demo.model.EntityModel;
import com.zaze.demo.model.entity.AnimationEntity;
import com.zaze.demo.model.entity.TableEntity;
import com.zaze.vector.VectorActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-08-16 - 11:20
 */
public class EntityModelImpl implements EntityModel {

    @Override
    public List<TableEntity> getTableList() {
        List<TableEntity> list = new ArrayList<>();
//        list.add(new TableEntity(1, "Animation", AnimationActivity.class));
        list.add(new TableEntity(2, "toolbar", ToolBarDemoActivity.class));
        list.add(new TableEntity(3, "Rx Android", RxAndroidActivity.class));
        list.add(new TableEntity(4, "Device Info", DeviceActivity.class));
        list.add(new TableEntity(5, "Time", TimeActivity.class));
        list.add(new TableEntity(6, "GPS", GpsActivity.class));
        list.add(new TableEntity(7, "Event Bus", EventBusActivity.class));
        list.add(new TableEntity(8, "Task", TaskActivity.class));
        list.add(new TableEntity(9, "Content Provider", ProviderActivity.class));
        list.add(new TableEntity(10, "Read Package", ReadPackageActivity.class));
        list.add(new TableEntity(11, "Preference", MyPreferenceActivity.class));
        list.add(new TableEntity(12, "Ok Http", OkHttpActivity.class));
        list.add(new TableEntity(13, "Check Net", CheckNetActivity.class));
        list.add(new TableEntity(14, "Progress", ProgressActivity.class));
        list.add(new TableEntity(15, "Vector", VectorActivity.class));
        list.add(new TableEntity(16, "File Explorer", FileExplorerActivity.class));
        list.add(new TableEntity(17, "Web View", WebViewActivity.class));
        list.add(new TableEntity(18, "Memory Cache", CacheActivity.class));
//        SortUtil.sortList(list, "getType", "desc");
//        com.android.internal.R.string.config_webViewPackageName
        return list;
    }

    @Override
    public List<AnimationEntity> getAnimationList() {
        List<AnimationEntity> list = new ArrayList<>();
        list.add(new AnimationEntity("View Property Animator", null));
        return list;
    }
}
