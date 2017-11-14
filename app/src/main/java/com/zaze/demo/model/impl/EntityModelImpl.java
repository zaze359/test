package com.zaze.demo.model.impl;


import com.zaze.demo.component.animation.ui.AnimationActivity;
import com.zaze.demo.component.cache.ui.CacheActivity;
import com.zaze.demo.component.checknet.ui.CheckNetActivity;
import com.zaze.demo.component.device.ui.DeviceActivity;
import com.zaze.demo.component.fileexplorer.ui.FileExplorerActivity;
import com.zaze.demo.component.gps.ui.GpsActivity;
import com.zaze.demo.component.logcat.ui.LogcatActivity;
import com.zaze.demo.component.okhttp.ui.OkHttpActivity;
import com.zaze.demo.component.preference.MyPreferenceActivity;
import com.zaze.demo.component.progress.ProgressActivity;
import com.zaze.demo.component.provider.ui.ProviderActivity;
import com.zaze.demo.component.readpackage.ui.ReadPackageActivity;
import com.zaze.demo.component.rxandroid.RxAndroidActivity;
import com.zaze.demo.component.socket.client.ui.ClientActivity;
import com.zaze.demo.component.socket.server.ui.ServerActivity;
import com.zaze.demo.component.system.ui.SystemActivity;
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
        int i = 0;
        list.add(new TableEntity(i++, "Animation", AnimationActivity.class));
        list.add(new TableEntity(i++, "Logcat", LogcatActivity.class));
        list.add(new TableEntity(i++, "Socket Server", ServerActivity.class));
        list.add(new TableEntity(i++, "Socket Client", ClientActivity.class));
        list.add(new TableEntity(i++, "System", SystemActivity.class));
        list.add(new TableEntity(i++, "toolbar", ToolBarDemoActivity.class));
        list.add(new TableEntity(i++, "Rx Android", RxAndroidActivity.class));
        list.add(new TableEntity(i++, "Device Info", DeviceActivity.class));
        list.add(new TableEntity(i++, "Time", TimeActivity.class));
        list.add(new TableEntity(i++, "GPS", GpsActivity.class));
        list.add(new TableEntity(i++, "Event Bus", EventBusActivity.class));
        list.add(new TableEntity(i++, "Task", TaskActivity.class));
        list.add(new TableEntity(i++, "Content Provider", ProviderActivity.class));
        list.add(new TableEntity(i++, "Read Package", ReadPackageActivity.class));
        list.add(new TableEntity(i++, "Preference", MyPreferenceActivity.class));
        list.add(new TableEntity(i++, "Ok Http", OkHttpActivity.class));
        list.add(new TableEntity(i++, "Check Net", CheckNetActivity.class));
        list.add(new TableEntity(i++, "Progress", ProgressActivity.class));
        list.add(new TableEntity(i++, "Vector", VectorActivity.class));
        list.add(new TableEntity(i++, "File Explorer", FileExplorerActivity.class));
        list.add(new TableEntity(i++, "Web View", WebViewActivity.class));
        list.add(new TableEntity(i++, "Memory Cache", CacheActivity.class));
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
