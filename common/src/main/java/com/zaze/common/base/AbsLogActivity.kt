package com.zaze.common.base

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.zaze.utils.log.ZLog

/**
 * Description :
 * @author : zaze
 * @version : 2021-07-15 - 15:20
 */
abstract class AbsLogActivity : AppCompatActivity() {

    companion object {
        var globalLog = true
        private const val TAG = "Lifecycle"
    }

    open val showLifecycle = globalLog

    private val activityName by lazy {
        this.javaClass.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (showLifecycle) ZLog.d(TAG, "$activityName onCreate taskId is ${this.taskId} ${this.isTaskRoot}")
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        if (showLifecycle) ZLog.d(TAG, "$activityName onPostCreate")
    }

    @SuppressLint("MissingSuperCall")
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (showLifecycle) ZLog.d(TAG, "$activityName onNewIntent")
    }

    override fun onStart() {
        super.onStart()
        if (showLifecycle)  ZLog.d(TAG, "$activityName onStart")
    }

    override fun onRestart() {
        super.onRestart()
        if (showLifecycle) ZLog.d(TAG, "$activityName onRestart")
    }

    override fun onResume() {
        super.onResume()
        if (showLifecycle) ZLog.d(TAG, "$activityName onResume")
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        if (showLifecycle) ZLog.d(TAG, "$activityName onSaveInstanceState")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        if (showLifecycle) ZLog.d(TAG, "$activityName onRestoreInstanceState")
    }

    override fun onPause() {
        super.onPause()
        if (showLifecycle) ZLog.d(TAG, "$activityName onPause")
    }

    override fun onStop() {
        super.onStop()
        if (showLifecycle) ZLog.d(TAG, "$activityName onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        if (showLifecycle) ZLog.d(TAG, "$activityName onDestroy")
    }
}

