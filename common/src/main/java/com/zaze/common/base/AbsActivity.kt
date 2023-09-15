package com.zaze.common.base

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ArrayRes
import androidx.annotation.DimenRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.zaze.common.R
import com.zaze.common.widget.loading.LoadingDialog
import com.zaze.common.widget.loading.LoadingView
import com.zaze.core.designsystem.skin.SkinLayoutInflaterFactory
import com.zaze.utils.ToastUtil
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-11-30 - 15:48
 */
abstract class AbsActivity : AbsThemeActivity() {

    private val loadingLazy = lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        LoadingDialog(this, createLoadingView())
    }

    //    init {
//        // 设置监听，会在onCreate() 时被调用。
//        addOnContextAvailableListener {
//        }
//    }
    override fun onCreate(savedInstanceState: Bundle?) {
        layoutInflater.factory2 =
            SkinLayoutInflaterFactory { parent: View?, name: String?, context: Context, attrs: AttributeSet ->
                delegate.createView(parent, name, context, attrs)
            }
        super.onCreate(savedInstanceState)
    }

    /**
     * 构建loadingView
     * @return LoadingView
     */
    open fun createLoadingView(): LoadingView {
        return LoadingView.createHorizontalLoading(this).apply {
            this.setTextColor(ContextCompat.getColor(context, R.color.white))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (loadingLazy.isInitialized()) {
            loadingLazy.value.dismiss()
        }
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
            if(loadingLazy.isInitialized()) {
                loadingLazy.value.dismiss()
            }
        } else {
            loadingLazy.value.setText(message).show()
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

//    override fun getResources(): Resources {
//        return BaseApplication.getInstance().resources
//    }

    // --------------------------------------------------

}

