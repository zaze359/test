package com.zaze.demo.theme

import android.util.AttributeSet
import android.widget.TextView
import com.zaze.demo.R
import com.zaze.demo.theme.AppCompatBaseHelper

/**
 * Description :
 * @author : zaze
 * @version : 2021-02-22 - 16:01
 */
class AppCompatTextHelper(view: TextView) :
        AppCompatBaseHelper<TextView>(view) {

    private var mainTextColorTint = 0

    override fun loadFromAttribute(attrs: AttributeSet?, defStyleAttr: Int) {
        if (attrs == null) {
            return
        }
//        val array = mView.context.obtainStyledAttributes(
//            attrs,
//            R.styleable.TintViewTextHelper,
//            defStyleAttr,
//            0
//        )
//        if (array.hasValue(R.styleable.TintViewTextHelper_mainTextColorTint)) {
//            mainTextColorTint =
//                array.getResourceId(R.styleable.TintViewTextHelper_mainTextColorTint, 0)
//            mView.setTextColor(mainTextColorTint)
//        }
//        array.recycle()
    }

    override fun tint() {
        TintManager.getColorStateList(mView.context, R.attr.mainTextColorTint)?.let {
            mView.setTextColor(it)
        }
    }
}