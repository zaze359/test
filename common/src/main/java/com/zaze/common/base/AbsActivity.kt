package com.zaze.common.base

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.os.PersistableBundle
import androidx.annotation.ArrayRes
import androidx.annotation.DimenRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.zaze.common.R
import com.zaze.common.base.ext.setImmersion
import com.zaze.common.widget.loading.LoadingDialog
import com.zaze.common.widget.loading.LoadingView
import com.zaze.utils.ToastUtil
import com.zaze.utils.log.ZLog

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-11-30 - 15:48
 */
abstract class AbsActivity : AppCompatActivity() {

    companion object {
        private const val showLifeCycle = true
        private const val TAG = "LifeCycle"
    }

    val activityName = "${this.javaClass.simpleName}@${Integer.toHexString(this.hashCode())}"

    private val loadingDialog by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        LoadingDialog(this, createLoadingView())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (showLifeCycle) {
            ZLog.i(TAG, "$activityName onCreate")
        }
        init(savedInstanceState)
        setImmersion(isFullScreen(), getStatusBarColor())
    }

    /**
     * onCreate(savedInstanceState)中的初始化
     * [savedInstanceState] savedInstanceState
     */
    open fun init(savedInstanceState: Bundle?) {}

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (showLifeCycle)
            ZLog.i(TAG, "$activityName onNewIntent")
    }

    override fun onStart() {
        super.onStart()
        if (showLifeCycle)
            ZLog.i(TAG, "$activityName onStart")
    }

    override fun onRestart() {
        super.onRestart()
        if (showLifeCycle)
            ZLog.i(TAG, "$activityName onRestart")
    }

    override fun onResume() {
        super.onResume()
        if (showLifeCycle)
            ZLog.i(TAG, "$activityName onResume")
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        super.onSaveInstanceState(outState, outPersistentState)
        if (showLifeCycle)
            ZLog.i(TAG, "$activityName onSaveInstanceState")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        if (showLifeCycle)
            ZLog.i(TAG, "$activityName onRestoreInstanceState")
    }

    override fun onPause() {
        super.onPause()
        if (showLifeCycle)
            ZLog.i(TAG, "$activityName onPause")
    }

    override fun onStop() {
        super.onStop()
        if (showLifeCycle)
            ZLog.i(TAG, "$activityName onStop")
    }


    override fun onDestroy() {
        loadingDialog.dismiss()
        super.onDestroy()
        if (showLifeCycle)
            ZLog.i(TAG, "$activityName onDestroy")
    }
    // --------------------------------------------------
    /**
     * 构建loadingView
     * @return LoadingView
     */
    open fun createLoadingView(): LoadingView {
        return LoadingView.createHorizontalLoading(this).apply {
            this.setTextColor(ContextCompat.getColor(context, R.color.white))
        }
    }

    /**
     * 获取状态栏色值
     * color res id
     */
    protected open fun getStatusBarColor(): Int {
        return R.color.colorPrimary
    }

    /**
     * 是否全屏
     */
    protected open fun isFullScreen(): Boolean {
        return false
    }

    // --------------------------------------------------

    fun showToast(resId: Int) {
        showToast(getString(resId))
    }

    fun showToast(content: String?) {
        ToastUtil.toast(this, content)
    }
    // --------------------------------------------------

    fun progress(isShow: Boolean?) {
        progress(if (isShow == null || !isShow) null else LoadingView.DEFAULT_TIP_TEXT)
    }

    fun progress(message: String? = null) {
        if (message == null) {
            loadingDialog.dismiss()
        } else {
            loadingDialog.setText(message).show()
        }
    }
    // --------------------------------------------------
    /**
     * 读取dimen 转 px
     */
    fun getDimen(@DimenRes resId: Int): Int {
        return this.resources.getDimensionPixelSize(resId)
    }

    /**
     * arrays.xml 转数据
     */
    fun getStringArray(@ArrayRes resId: Int): Array<String> {
        return this.resources.getStringArray(resId)
    }

    override fun getResources(): Resources {
        return BaseApplication.getInstance().resources
    }

}

