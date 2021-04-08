package com.zaze.demo.customview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class CustomView : View {


    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val paint = Paint()
    val path = Path()

    init {
        paint.color = Color.RED
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 5f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
//        canvas.drawPoint(100f, 100f, pointPaint)
//        canvas.drawCircle(100f, 100f, 90f, paint)
//        //
//        path.reset()
//        path.moveTo(10f, 10f)
//        path.lineTo(10f, 100f)
//        path.lineTo(300f, 100f)
//        path.close()
//        canvas.drawPath(path, paint)


//        canvas.drawPoint(10f, 10f, pointPaint)
//        canvas.drawPoint(100f, 10f, pointPaint)
//        canvas.drawPoint(10f, 210f, pointPaint)
//        canvas.drawPoint(10f, 10f, pointPaint)

        val rect = RectF(100f, 10f, 200f, 100f)

        path.reset()
        path.moveTo(0f, 0f)
        // 画弧线
        // 此弧线是以矩形的中心为椭圆的圆心
        // 路径默认是连贯的
        // x轴正方向为0
        path.arcTo(rect, 0f, 350f)
        paint.color = Color.RED
        canvas.drawPath(path, paint)
        path.reset()
        path.moveTo(0f, 0f)
        // 强制将圆弧的起点作为绘制的起点
        path.arcTo(rect, 0f, 90f, true)
        paint.color = Color.BLUE
        canvas.drawPath(path, paint)

        paint.color = Color.BLACK
//        canvas.drawLine(10f, 0f, 10f, height * 1.0f, paint)
//        canvas.drawLine(0f, 10f, width * 1.0f, 10f, paint)

        canvas.drawLine(100f, 0f, 100f, height * 1.0f, paint)
        canvas.drawLine(200f, 0f, 200f, height * 1.0f, paint)

        canvas.drawLine(100f, 100f, width * 1.0f, 100f, paint)
        canvas.drawLine(200f, 100f, width * 1.0f, 100f, paint)

        canvas.drawLine(150f, 50f, 150f, height * 1.0f, paint)
        canvas.drawLine(150f, 50f, width * 1.0f, 50f, paint)

        paint.color = Color.GREEN
        canvas.drawRect(rect, paint)

    }

}