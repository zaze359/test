package com.zaze.demo.theme

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.zaze.demo.theme.widgets.TintImageView
import com.zaze.demo.theme.widgets.TintTextView

/**
 * 将一些场景的控件转为支持设置主题的自定义控件。
 */
class SkinFactory(
    private val delegate: ((
        parent: View?,
        name: String,
        context: Context,
        attrs: AttributeSet
    ) -> View?)?
) : LayoutInflater.Factory2 {
    override fun onCreateView(
        parent: View?,
        name: String,
        context: Context,
        attrs: AttributeSet
    ): View? {
        return when(name) {
            "TextView" -> {
                return TintTextView(context, attrs)
            }
            "ImageView" -> {
                return TintImageView(context, attrs)
            }
            else -> {
                delegate?.invoke(parent, name, context, attrs)
            }
        }

    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return onCreateView(null, name, context, attrs)
    }
}