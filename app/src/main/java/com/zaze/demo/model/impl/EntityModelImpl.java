package com.zaze.demo.model.impl;


import com.zaze.demo.component.admin.DeviceAdminActivity;
import com.zaze.demo.component.animation.VectorActivity;
import com.zaze.demo.component.animation.ui.AnimationActivity;
import com.zaze.demo.component.animation.ui.AnimationActivity1;
import com.zaze.demo.component.animation.ui.SharedElementActivity;
import com.zaze.demo.component.animation.ui.TransitionActivity;
import com.zaze.demo.component.application.AppActivity;
import com.zaze.demo.component.bitmap.BitmapActivity;
import com.zaze.demo.component.cache.ui.CacheActivity;
import com.zaze.demo.component.coroutine.CoroutineActivity;
import com.zaze.demo.customview.CustomViewActivity;
import com.zaze.demo.component.device.DeviceActivity;
import com.zaze.demo.component.file.explorer.FileExplorerActivity;
import com.zaze.demo.component.font.ui.FontActivity;
import com.zaze.demo.component.gps.ui.GpsActivity;
import com.zaze.demo.component.handler.HandlerActivity;
import com.zaze.demo.component.logcat.ui.LogcatActivity;
import com.zaze.demo.component.network.NetworkStatsActivity;
import com.zaze.demo.component.notification.ui.NotificationActivity;
import com.zaze.demo.component.okhttp.ui.OkHttpActivity;
import com.zaze.demo.component.preference.MyPreferenceActivity;
import com.zaze.demo.component.progress.ProgressActivity;
import com.zaze.demo.component.provider.ui.ProviderActivity;
import com.zaze.demo.component.rxandroid.RxAndroidActivity;
import com.zaze.demo.component.socket.client.ui.ClientActivity;
import com.zaze.demo.component.socket.server.ServerActivity;
import com.zaze.demo.component.system.ui.SystemActivity;
import com.zaze.demo.component.task.ui.TaskActivity;
import com.zaze.demo.component.time.TimeActivity;
import com.zaze.demo.component.toolbar.ToolBarDemoActivity;
import com.zaze.demo.component.webview.WebViewActivity;
import com.zaze.demo.component.wifi.ui.WifiActivity;
import com.zaze.demo.debug.CaptureActivity;
import com.zaze.demo.debug.SensorActivity;
import com.zaze.demo.model.EntityModel;
import com.zaze.demo.model.entity.AnimationEntity;
import com.zaze.demo.model.entity.TableEntity;

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
        list.add(new TableEntity("应用查询", AppActivity.class, ++i));
        list.add(new TableEntity("设备管理器", DeviceAdminActivity.class, ++i));
//        list.add(new TableEntity("分享", ShareActivity.class, ++i));
        list.add(new TableEntity("设备信息", DeviceActivity.class, ++i));
        list.add(new TableEntity("系统设置", SystemActivity.class, ++i));
        list.add(new TableEntity("文件管理", FileExplorerActivity.class, ++i));
        list.add(new TableEntity("协程", CoroutineActivity.class, ++i));
        list.add(new TableEntity("传感器", SensorActivity.class, ++i));
        list.add(new TableEntity("WebView", WebViewActivity.class, ++i));
        list.add(new TableEntity("Bitmap", BitmapActivity.class, ++i));
        list.add(new TableEntity("流量监控", NetworkStatsActivity.class, ++i));
        list.add(new TableEntity("截屏", CaptureActivity.class, ++i));
        list.add(new TableEntity("字体变换", FontActivity.class, ++i));
        list.add(new TableEntity("自定义View", CustomViewActivity.class, ++i));
        list.add(new TableEntity("消息通知栏", NotificationActivity.class, ++i));
        list.add(new TableEntity("任务机制", TaskActivity.class, ++i));
        list.add(new TableEntity("缓存机制", CacheActivity.class, ++i));
        list.add(new TableEntity("动画效果", AnimationActivity.class, ++i));
        list.add(new TableEntity("网络管理", WifiActivity.class, ++i));
        list.add(new TableEntity("时间转换", TimeActivity.class, ++i));
        list.add(new TableEntity("GPS定位", GpsActivity.class, ++i));
        list.add(new TableEntity("进度条", ProgressActivity.class, ++i));
        list.add(new TableEntity("服务端Socket", ServerActivity.class, ++i));
        list.add(new TableEntity("客户端Socket", ClientActivity.class, ++i));
        list.add(new TableEntity("OkHttp", OkHttpActivity.class, ++i));
        list.add(new TableEntity("Vector", VectorActivity.class, ++i));
        list.add(new TableEntity("Toolbar", ToolBarDemoActivity.class, ++i));
        list.add(new TableEntity("RxAndroid", RxAndroidActivity.class, ++i));
        list.add(new TableEntity("Content Provider", ProviderActivity.class, ++i));
        list.add(new TableEntity("XmlPreference", MyPreferenceActivity.class, ++i));
        list.add(new TableEntity("Logcat", LogcatActivity.class, ++i));
        list.add(new TableEntity("Handler", HandlerActivity.class, ++i));
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
