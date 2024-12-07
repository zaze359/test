package com.zaze.accessibility

import android.accessibilityservice.AccessibilityService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.zaze.core.data.repository.AdRepository
import com.zaze.utils.IdGenerator
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Description :
 * @author : zaze
 * @version : 2020-12-21 - 16:39
 */
@AndroidEntryPoint
class MyAccessibilityService : AccessibilityService() {

    companion object {
        private const val TAG = "MyAccessibilityService"
        private val channelId = "KeepLiveForegroundService"
    }

    private var notificationBuilder: NotificationCompat.Builder? = null

    @Inject
    lateinit var adRepository: AdRepository

    private val adHandler by lazy {
        AdHandler(this, adRepository)
    }

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

//    private val handlerThread

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = NotificationManagerCompat.from(this)
            if (notificationManager.getNotificationChannel(channelId) == null) {
                NotificationChannel(
                    channelId,
                    channelId,
                    NotificationManager.IMPORTANCE_DEFAULT
                ).let {
                    it.enableLights(false)
                    it.enableVibration(false)
                    it.setShowBadge(false)
                    NotificationManagerCompat.from(this).createNotificationChannel(it)
                }
            }
        }

        val notificationIntent = Intent(this, AccessibilityActivity::class.java)
        val flag = PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, flag)
        notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_all_inclusive)
            .setContentText("点击助手")
            .setContentTitle("辅助执行点击操作")
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_MAX) // 未展开
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.i(TAG, "onServiceConnected")
        notificationBuilder?.let {
            startForeground(IdGenerator.generateId(), it.build())
        }
        coroutineScope.launch {
            adHandler.loadRules()
        }
    }

    override fun onInterrupt() {
        Log.i(TAG, "onInterrupt")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        Log.i(TAG, "adHandler onAccessibilityEvent: $event")
//        when (event.eventType) {
//            AccessibilityEvent.TYPE_VIEW_CLICKED -> {
//                ZLog.i(TAG, "Clicked: ${event.source}")
//                ZLog.i(TAG, "Clicked: ${event.source?.viewIdResourceName}")
//            }
//        }
//        coroutineScope.launch(Dispatchers.Default) {
//        }

//        adHandler.onAccessibilityEvent(event)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy")
        stopForeground(true)
    }
}