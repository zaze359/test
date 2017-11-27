package com.zaze.demo.model.impl;


import com.zaze.demo.component.animation.ui.AnimationActivity;
import com.zaze.demo.component.animation.ui.AnimationActivity1;
import com.zaze.demo.component.animation.ui.SharedElementActivity;
import com.zaze.demo.component.animation.ui.TransitionActivity;
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
import com.zaze.demo.component.wifi.ui.WifiActivity;
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
        list.add(new TableEntity("Animation", AnimationActivity.class, ++i));
        list.add(new TableEntity("Wifi", WifiActivity.class, ++i));
        list.add(new TableEntity("System", SystemActivity.class, ++i));
        list.add(new TableEntity("toolbar", ToolBarDemoActivity.class, ++i));
        list.add(new TableEntity("Rx Android", RxAndroidActivity.class, ++i));
        list.add(new TableEntity("Device Info", DeviceActivity.class, ++i));
        list.add(new TableEntity("Time", TimeActivity.class, ++i));
        list.add(new TableEntity("GPS", GpsActivity.class, ++i));
        list.add(new TableEntity("Event Bus", EventBusActivity.class, ++i));
        list.add(new TableEntity("Task", TaskActivity.class, ++i));
        list.add(new TableEntity("Content Provider", ProviderActivity.class, ++i));
        list.add(new TableEntity("Read Package", ReadPackageActivity.class, ++i));
        list.add(new TableEntity("Preference", MyPreferenceActivity.class, ++i));
        list.add(new TableEntity("Ok Http", OkHttpActivity.class, ++i));
        list.add(new TableEntity("Check Net", CheckNetActivity.class, ++i));
        list.add(new TableEntity("Progress", ProgressActivity.class, ++i));
        list.add(new TableEntity("Vector", VectorActivity.class, ++i));
        list.add(new TableEntity("File Explorer", FileExplorerActivity.class, ++i));
        list.add(new TableEntity("Web View", WebViewActivity.class, ++i));
        list.add(new TableEntity("Memory Cache", CacheActivity.class, ++i));
        list.add(new TableEntity("Logcat", LogcatActivity.class, ++i));
        list.add(new TableEntity("Socket Server", ServerActivity.class, ++i));
        list.add(new TableEntity("Socket Client", ClientActivity.class, ++i));
        return list;
    }

    @Override
    public List<AnimationEntity> getAnimationList() {
        List<AnimationEntity> list = new ArrayList<>();
        list.add(new AnimationEntity("Scene Transition Animation(平移过渡)", TransitionActivity.class, AnimationEntity.Type.SCENE_TRANSITION));
        list.add(new AnimationEntity("Scale Up Animation(放大过渡)", AnimationActivity1.class, AnimationEntity.Type.SCALE_UP));
        list.add(new AnimationEntity("Shared Element()", SharedElementActivity.class, AnimationEntity.Type.SCENE_TRANSITION));
        list.add(new AnimationEntity("Scene Transition Animation(平移过渡)", AnimationActivity1.class, AnimationEntity.Type.SCENE_TRANSITION));
        list.add(new AnimationEntity("Scene Transition Animation(平移过渡)", AnimationActivity1.class, AnimationEntity.Type.SCENE_TRANSITION));
        list.add(new AnimationEntity("Scene Transition Animation(平移过渡)", AnimationActivity1.class, AnimationEntity.Type.SCENE_TRANSITION));
        return list;
    }
}
