package com.zaze.demo.debug

import android.graphics.Color
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.BackgroundColorSpan
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.TextAppearanceSpan
import android.view.View
import android.widget.TextView
import androidx.compose.ui.text.android.style.LetterSpacingSpanPx
import androidx.core.text.set
import androidx.core.text.toSpannable

class LogViewWrapper(private val textView: TextView) {

    fun setText(text: String?) {
        textView.movementMethod = LinkMovementMethod.getInstance()
//        textView.text = text
        textView.highlightColor = Color.WHITE
        text?.let {
            val builder = SpannableStringBuilder()
            val textSpan = it.toSpannable()
            textSpan.setSpan(
                ForegroundColorSpan(Color.BLUE),
                0,
                it.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
//            textSpan.setSpan(
//                BetterHighlightSpan(textSpan),
//                0,
//                it.length,
//                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
//            )
//            textSpan.setSpan(
//                BackgroundColorSpan(Color.RED),
//                0,
//                it.length,
//                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
//            )
//            textSpan.setSpan(object : ClickableSpan() {
//                override fun onClick(widget: View) {
//                    if(widget is TextView) {
//                        widget.highlightColor = Color.RED
//                    }
//                }
//
//                override fun updateDrawState(ds: TextPaint) {
//                    super.updateDrawState(ds)
//                    ds.isUnderlineText = true
//                    ds.clearShadowLayer()
//                }
//            }, 0, it.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            builder.append(textSpan)
            textView.text = builder
        }
    }


}