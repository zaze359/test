package com.zaze.core.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.google.android.material.R
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.shape.MaterialShapeDrawable
import com.zaze.core.ui.databinding.AppbarLayoutBinding

class TopAppBarLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = R.attr.appBarLayoutStyle
) : AppBarLayout(context, attrs, defStyleAttr) {
    private val appbarBinding: AppbarLayoutBinding

    init {
        appbarBinding = AppbarLayoutBinding.inflate(LayoutInflater.from(context), this, true)
        // 设置状态栏颜色
        statusBarForeground = MaterialShapeDrawable.createWithElevationOverlay(context)
    }

    val toolbar: MaterialToolbar
        get() = appbarBinding.toolbar

    var title: CharSequence
        get() = appbarBinding.toolbar.title.toString()
        set(value) {
            appbarBinding.toolbar.title = value
        }


}