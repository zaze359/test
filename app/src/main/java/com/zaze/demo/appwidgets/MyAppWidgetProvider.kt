package com.zaze.demo.appwidgets

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.RemoteViews
import com.zaze.demo.R
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag

/**
 * Description :
 * @author : zaze
 * @version : 2021-11-01 - 09:41
 */
class MyAppWidgetProvider : AppWidgetProvider() {

    companion object {
        private const val TAG = "${ZTag.TAG}AppWidgetProvider"
        private var click = 0
        const val REFRESH_ACTION = "com.zaze.demo.appwidget.action.REFRESH"
    }

    override fun onReceive(context: Context, intent: Intent) {
        ZLog.i(TAG, "onReceive: ${intent.action}")
        if (REFRESH_ACTION == intent.action) {
            AppWidgetManager.getInstance(context)?.let {
                val appWidgetIds =
                    it.getAppWidgetIds(ComponentName(context, MyAppWidgetProvider::class.java))
                updateAppWidget(context, it, appWidgetIds)
            }
            return
        }
        super.onReceive(context, intent)
    }

    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        ZLog.i(TAG, "onUpdate")
        updateAppWidget(context, appWidgetManager, appWidgetIds)
    }

    private fun updateAppWidget(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        if (context == null || appWidgetManager == null) {
            return
        }
        val remoteViews = RemoteViews(context.packageName, R.layout.my_appwidget)
        val flag = PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT

        // 打开应用
        remoteViews.setOnClickPendingIntent(
            R.id.appwidget_open_btn, PendingIntent.getActivity(
                context,
                0,
                context.packageManager.getLaunchIntentForPackage(context.packageName),
                flag
            )
        )
        // 刷新
        remoteViews.setOnClickPendingIntent(
            R.id.appwidget_refresh_btn, PendingIntent.getBroadcast(
                context,
                0,
                Intent(REFRESH_ACTION).apply {
                    `package` = context.packageName
                },
                flag
            )
        )
        click++
        remoteViews.setTextViewText(R.id.appwidget_content_tv, click.toString())
        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews)
    }

    override fun onAppWidgetOptionsChanged(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetId: Int,
        newOptions: Bundle?
    ) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
//        appWidgetManager?.getAppWidgetInfo(appWidgetId)?
        ZLog.i(TAG, "onAppWidgetOptionsChanged: $appWidgetId")
    }

    override fun onDeleted(context: Context?, appWidgetIds: IntArray?) {
        super.onDeleted(context, appWidgetIds)
        ZLog.i(TAG, "onDeleted: $appWidgetIds")
    }

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
        ZLog.i(TAG, "onEnabled")
    }

    override fun onDisabled(context: Context?) {
        super.onDisabled(context)
        ZLog.i(TAG, "onDisabled")
    }

    override fun onRestored(context: Context?, oldWidgetIds: IntArray?, newWidgetIds: IntArray?) {
        super.onRestored(context, oldWidgetIds, newWidgetIds)
        ZLog.i(TAG, "onRestored: $oldWidgetIds >> $newWidgetIds")
    }
}