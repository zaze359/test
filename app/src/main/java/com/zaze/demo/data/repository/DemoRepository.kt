package com.zaze.demo.data.repository

import com.zaze.common.di.CustomDispatchers
import com.zaze.common.di.Dispatcher
import com.zaze.core.model.data.AppNavigation
import com.zaze.demo.feature.anim.VectorActivity
import com.zaze.demo.feature.anim.AnimationActivity
import com.zaze.demo.component.admin.DeviceAdminActivity
import com.zaze.demo.component.cache.ui.CacheActivity
import com.zaze.demo.component.customview.CustomViewActivity
import com.zaze.demo.component.device.DeviceActivity
import com.zaze.demo.component.file.explorer.FileExplorerActivity
import com.zaze.demo.component.font.ui.FontActivity
import com.zaze.demo.component.gps.ui.GpsActivity
import com.zaze.demo.component.handler.HandlerActivity
import com.zaze.demo.component.lifecycle.LifecycleActivity
import com.zaze.demo.component.logcat.ui.LogcatActivity
import com.zaze.demo.component.network.NetworkStatsActivity
import com.zaze.demo.component.okhttp.OkHttpActivity
import com.zaze.demo.component.progress.ProgressActivity
import com.zaze.demo.component.rxandroid.RxAndroidActivity
import com.zaze.demo.component.socket.client.ui.ClientActivity
import com.zaze.demo.component.socket.server.ServerActivity
import com.zaze.demo.state.StateActivity
import com.zaze.demo.component.system.ui.SystemActivity
import com.zaze.demo.component.task.ui.TaskActivity
import com.zaze.demo.component.time.TimeActivity
import com.zaze.demo.component.toolbar.ToolBarDemoActivity
import com.zaze.demo.component.webview.WebViewActivity
import com.zaze.demo.component.wifi.WifiActivity
import com.zaze.demo.debug.CaptureActivity
import com.zaze.demo.debug.SensorActivity
import com.zaze.demo.data.entity.TableEntity
import com.zaze.demo.feature.image.BitmapActivity
import com.zaze.demo.feature.image.DrawableActivity
import com.zaze.demo.feature.notification.NotificationActivity
import com.zaze.demo.feature.settings.MyPreferenceActivity
import com.zaze.demo.feature.settings.SettingsActivity
import com.zaze.demo.usagestats.UsageStatesActivity
import com.zaze.feature.sliding.conflict.SlidingConflictActivity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class DemoRepository @Inject constructor(@Dispatcher(CustomDispatchers.IO) private val dispatcher: CoroutineDispatcher) {
    suspend fun loadDemos(): List<TableEntity> = withContext(dispatcher) {
        var i = 0
        ArrayList<TableEntity>().also { list ->
            list.add(
                TableEntity(
                    "Compose",
                    AppNavigation.composeRoute,
                    ++i
                )
            )
            list.add(
                TableEntity(
                    "应用查询",
                    AppNavigation.appListRoute,
                    ++i
                )
            )
            list.add(
                TableEntity(
                    "播放音视频",
                    AppNavigation.mediaRoute,
                    ++i
                )
            )
            list.add(
                TableEntity(
                    "滑动冲突",
                    SlidingConflictActivity::class.java,
                    ++i
                )
            )

            list.add(
                TableEntity(
                    "自定义View",
                    CustomViewActivity::class.java,
                    ++i
                )
            )
            list.add(
                TableEntity(
                    "Bitmap",
                    BitmapActivity::class.java,
                    ++i
                )
            )
            list.add(
                TableEntity(
                    "Drawable",
                    DrawableActivity::class.java,
                    ++i
                )
            )
            list.add(
                TableEntity(
                    "生命周期观测",
                    LifecycleActivity::class.java,
                    ++i
                )
            )
            list.add(
                TableEntity(
                    "状态保存/恢复",
                    StateActivity::class.java,
                    ++i
                )
            )
            list.add(
                TableEntity(
                    "设备管理器",
                    DeviceAdminActivity::class.java,
                    ++i
                )
            )
            list.add(
                TableEntity(
                    "应用使用数据",
                    UsageStatesActivity::class.java,
                    ++i
                )
            )
//        list.add(new TableEntity("分享", ShareActivity.class, ++i));
            //        list.add(new TableEntity("分享", ShareActivity.class, ++i));
            list.add(
                TableEntity(
                    "设备信息",
                    DeviceActivity::class.java,
                    ++i
                )
            )
            list.add(
                TableEntity(
                    "系统设置",
                    SystemActivity::class.java,
                    ++i
                )
            )
            list.add(
                TableEntity(
                    "文件管理",
                    FileExplorerActivity::class.java,
                    ++i
                )
            )
            list.add(
                TableEntity(
                    "传感器",
                    SensorActivity::class.java,
                    ++i
                )
            )
            list.add(
                TableEntity(
                    "WebView",
                    WebViewActivity::class.java,
                    ++i
                )
            )
            list.add(
                TableEntity(
                    "流量监控",
                    NetworkStatsActivity::class.java,
                    ++i
                )
            )
            list.add(
                TableEntity(
                    "截屏",
                    CaptureActivity::class.java,
                    ++i
                )
            )
            list.add(
                TableEntity(
                    "字体变换",
                    FontActivity::class.java,
                    ++i
                )
            )
            list.add(
                TableEntity(
                    "消息通知栏",
                    NotificationActivity::class.java,
                    ++i
                )
            )
            list.add(
                TableEntity(
                    "任务机制",
                    TaskActivity::class.java,
                    ++i
                )
            )
            list.add(
                TableEntity(
                    "缓存机制",
                    CacheActivity::class.java,
                    ++i
                )
            )
            list.add(
                TableEntity(
                    "动画效果",
                    AnimationActivity::class.java,
                    ++i
                )
            )
            list.add(
                TableEntity(
                    "网络管理",
                    WifiActivity::class.java,
                    ++i
                )
            )
            list.add(
                TableEntity(
                    "时间转换",
                    TimeActivity::class.java,
                    ++i
                )
            )
            list.add(
                TableEntity(
                    "GPS定位",
                    GpsActivity::class.java,
                    ++i
                )
            )
            list.add(
                TableEntity(
                    "进度条",
                    ProgressActivity::class.java,
                    ++i
                )
            )
            list.add(
                TableEntity(
                    "服务端Socket",
                    ServerActivity::class.java,
                    ++i
                )
            )
            list.add(
                TableEntity(
                    "客户端Socket",
                    ClientActivity::class.java,
                    ++i
                )
            )
            list.add(
                TableEntity(
                    "OkHttp",
                    OkHttpActivity::class.java,
                    ++i
                )
            )
            list.add(
                TableEntity(
                    "Vector",
                    VectorActivity::class.java,
                    ++i
                )
            )
            list.add(
                TableEntity(
                    "Toolbar",
                    ToolBarDemoActivity::class.java,
                    ++i
                )
            )
            list.add(
                TableEntity(
                    "RxAndroid",
                    RxAndroidActivity::class.java,
                    ++i
                )
            )
            list.add(
                TableEntity(
                    "Logcat",
                    LogcatActivity::class.java,
                    ++i
                )
            )
            list.add(
                TableEntity(
                    "Handler",
                    HandlerActivity::class.java,
                    ++i
                )
            )
        }
    }
}