package com.zaze.accessibility

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.zaze.core.data.repository.AdRepository
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

    @Inject
    lateinit var adRepository: AdRepository

    private val adHandler by lazy {
        AdHandler(this, adRepository)
    }

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

//    private val handlerThread

    override fun onCreate() {
        super.onCreate()
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val notificationManager = NotificationManagerCompat.from(this)
//            if (notificationManager.getNotificationChannel(channelId) == null) {
//                NotificationChannel(
//                    channelId,
//                    channelId,
//                    NotificationManager.IMPORTANCE_LOW
//                ).let {
//                    it.enableLights(false)
//                    it.enableVibration(false)
//                    it.setShowBadge(false)
//                    NotificationManagerCompat.from(this).createNotificationChannel(it)
//                }
//            }
//        }
//
//        val notificationIntent = Intent(this, AccessibilityActivity::class.java)
//        val flag = PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
//        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, flag)
//        val notification = NotificationCompat.Builder(this, channelId)
//            .setSmallIcon(R.drawable.ic_all_inclusive)
//            .setContentText("测试前台服务保活")
//            .setContentTitle("保活服务")
//            .setCategory(NotificationCompat.CATEGORY_SERVICE)
//            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
//            .setContentIntent(pendingIntent)
//            .setPriority(NotificationCompat.PRIORITY_MAX) // 未展开
//            .build()
//        startForeground(1, notification)
        Log.i(TAG, "onCreate")
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.i(TAG, "onServiceConnected")
        coroutineScope.launch {
            adHandler.loadRules()
        }
    }

    override fun onInterrupt() {
        Log.i(TAG, "onInterrupt")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        coroutineScope.launch {
            adHandler.onAccessibilityEvent(event)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy")
//        stopForeground(true)
    }
}