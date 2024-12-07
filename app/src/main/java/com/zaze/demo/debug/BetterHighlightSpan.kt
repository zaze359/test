package com.zaze.demo.debug

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.FontMetricsInt
import android.graphics.Path
import android.graphics.RectF
import android.text.TextPaint
import android.text.style.ReplacementSpan
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag

class BetterHighlightSpan(text: CharSequence?) : ReplacementSpan() {
    private val backgroundColor = Color.parseColor("#33E74D54")
    private val textColor = Color.parseColor("#FFE74D54")
    var leftPadding: Int = 4
    var rightPadding: Int = 4
    var leftCornerSize: Int = 4
    var rightCornerSize: Int = 4
    var isSubOrSup: Int = 0 //0center,1top,2bottom
    var mSize: Int = 0

    override fun getSize(
        paint: Paint,
        text: CharSequence,
        start: Int,
        end: Int,
        fm: FontMetricsInt?
    ): Int {
        mSize = Math.round(paint.measureText(text, start, end)) + leftPadding + rightPadding
        return mSize
    }


    override fun draw(
        canvas: Canvas,
        text: CharSequence,
        start: Int,
        end: Int,
        x: Float,
        top: Int,
        y: Int,
        bottom: Int,
        paint: Paint
    ) {
        val fm = paint.fontMetricsInt
        val i = y - (top - fm.ascent)
        val newBottom = y + i
        // change color and draw background highlight
        val rect = RectF(
            x, top.toFloat(), x + paint.measureText(text, start, end) + leftPadding + rightPadding,
            (newBottom + 1).toFloat()
        )
        paint.color = backgroundColor
        val path = Path()
        path.addRoundRect(
            rect, floatArrayOf(
                leftCornerSize.toFloat(), leftCornerSize.toFloat(),
                rightCornerSize.toFloat(), rightCornerSize.toFloat(),
                rightCornerSize.toFloat(), rightCornerSize.toFloat(),
                leftCornerSize.toFloat(), leftCornerSize.toFloat()
            ),
            Path.Direction.CW
        )
        canvas.drawPath(path, paint)

        // revert color and draw text
        paint.color = textColor
        var subY = y.toFloat() //默认居中
        if (isSubOrSup == 1) { //top
            subY = (top - fm.ascent).toFloat()
        } else if (isSubOrSup == 2) {
            subY = newBottom.toFloat()
        }

        //        if (isSubOrSup!=0){
//            paint.setTextSize(paint.getTextSize()*0/7f);
//        }
        ZLog.i(ZTag.TAG, "canvas.drawText start: " + text.subSequence(start, end))
        ZLog.i(ZTag.TAG, "canvas.drawText xy: $x; $subY")
        //        RectF rect = new RectF(x, y + paint.ascent(), x + mSize, y + paint.descent());
//        canvas.drawRoundRect(rect, 8, 8, paint);
//        a(canvas, text, start, end, x, top, y, bottom, paint);
        val newPaint: Paint = getCustomTextPaint(paint)
        newPaint.isAntiAlias = true
        newPaint.textAlign = Paint.Align.CENTER
        //        canvas.drawText(text, start, end, x + leftPadding, subY, textPaint);
        // 获取文字左边的偏移量
        val offsetX = ((rect.right - rect.left - mSize) / 2).toInt()
        //        canvas.drawText(text, start, end, x + offsetX, y, newPaint);
//        canvas.drawText(text, start, end, x + leftPadding, subY, paint);
//        canvas.drawLine(x, newBottom + 1,
//                x + paint.measureText(text, start, end) + leftPadding + rightPadding,
//                newBottom + 2, paint);
    }


    private fun getCustomTextPaint(srcPaint: Paint): TextPaint {
        val textPaint = TextPaint(srcPaint)
        textPaint.color = Color.BLACK
        textPaint.textSize = 60f
        return textPaint
    }
}