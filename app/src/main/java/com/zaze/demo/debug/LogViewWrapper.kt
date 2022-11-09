package com.zaze.demo.debug

import android.graphics.Color
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import androidx.core.text.toSpannable

class LogViewWrapper(private val textView: TextView) {

    fun setText(text: String?) {
//        textView.text = text
        text?.let {
            val builder = SpannableStringBuilder()
            val textSpan = it.toSpannable();
            textSpan.setSpan(ForegroundColorSpan(Color.BLUE), 0, it.length, 0)
            builder.append(textSpan)
            textView.text = builder
        }
    }


}