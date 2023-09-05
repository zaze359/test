package com.zaze.core.designsystem.skin

import android.content.Context
import android.content.res.Resources.Theme
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.zaze.core.designsystem.R


/**
 * 将一些场景的控件转为支持设置主题的自定义控件。
 */
class SkinLayoutInflaterFactory(
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
        // 做一些我们自己的配置
        // 不需要处理时传给 delegate 处理
        return createViewInternal(parent, name, context, attrs)?.also {
//            it.
        }

    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return onCreateView(null, name, context, attrs)
    }

    private fun createViewInternal(
        parent: View?,
        name: String,
        context: Context,
        attrs: AttributeSet
    ): View? {
        return when (name) {
            "TextView" -> {
                return MaterialTextView(context, attrs)
            }
            "Button" -> {
                return MaterialButton(context, attrs)
            }
//            "ImageView" -> {
//                return TintImageView(context, attrs)
//            }
            else -> {
                delegate?.invoke(parent, name, context, attrs)
            }
        }
    }
    private var mEmptyTheme: Theme? = null
    private fun loadFromAttribute(
        context: Context,
        attrs: AttributeSet
    ) {
        if (mEmptyTheme == null) {
            mEmptyTheme = context.applicationContext.resources.newTheme()
        }
        val a: TypedArray = mEmptyTheme!!.obtainStyledAttributes(attrs, R.styleable.ZSkinDef, 0, 0)
        val count = a.indexCount
        repeat(a.indexCount) {
            val attr = a.getIndex(it)
            val name = a.getString(it)
            if(name.isNullOrEmpty()) return@repeat
        }
    }
}