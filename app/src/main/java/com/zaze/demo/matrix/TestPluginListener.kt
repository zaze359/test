package com.zaze.demo.matrix

import android.content.Context
import com.tencent.matrix.plugin.DefaultPluginListener
import com.tencent.matrix.plugin.Plugin
import com.tencent.matrix.report.Issue

/**
 * Description :
 * @author : zaze
 * @version : 2022-05-17 22:57
 */
class TestPluginListener(context: Context) : DefaultPluginListener(context) {
    companion object {
        const val TAG = "TestPluginListener"
    }

    override fun onInit(plugin: Plugin?) {
        super.onInit(plugin)
    }

    override fun onStart(plugin: Plugin?) {
        super.onStart(plugin)
    }

    override fun onStop(plugin: Plugin?) {
        super.onStop(plugin)
    }

    override fun onDestroy(plugin: Plugin?) {
        super.onDestroy(plugin)
    }

    override fun onReportIssue(issue: Issue?) {
        super.onReportIssue(issue)
    }
}