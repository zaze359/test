package com.zaze.demo.component.customview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Scroller
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag

class CustomView : View {

    private lateinit var mScroller: Scroller


    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context)
    }

    fun init(context: Context) {
        mScroller = Scroller(context, LinearInterpolator())
    }

    private val paint = Paint()
    private val path = Path()

    private val dstBmp = makeDst(200, 200)
    private val srcBmp = makeSrc(200, 200)

    private var mPath = Path()
    private var mRect = RectF(300f, 20f, 400f, 160f)

    init {
        paint.color = Color.RED
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 5f
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        ZLog.i(ZTag.TAG_DEBUG, "onMeasure")
//        when (MeasureSpec.getMode(widthMeasureSpec)) {
//            MeasureSpec.EXACTLY -> {
//                ZLog.i(ZTag.TAG, "floating: EXACTLY $widthMeasureSpec")
//            }
//
//            MeasureSpec.AT_MOST -> {
//                ZLog.i(ZTag.TAG, "floating: AT_MOST $widthMeasureSpec")
//            }
//
//            MeasureSpec.UNSPECIFIED -> {
//                ZLog.i(ZTag.TAG, "floating: UNSPECIFIED $widthMeasureSpec")
//            }
//        }
        updatePath()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        ZLog.i(ZTag.TAG_DEBUG, "onLayout")
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
//        post{
//            updateVolumePath(w, h)
//        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        ZLog.i(ZTag.TAG_DEBUG, "onDraw")
//        canvas.drawPoint(100f, 100f, pointPaint)
//        canvas.drawCircle(100f, 100f, 10f, paint)
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
//        canvas.drawRect(mRect, paint)
        canvas.drawPath(mPath, paint)
//        canvas.drawLine(mRect.left, 12F + mVolumeH / 3, width * 1.0F, 12F + mVolumeH / 3, paint)
//        canvas.drawLine(mRect.left, 125 - mVolumeH / 3, width * 1.0F, 125 - mVolumeH / 3, paint)
//        left, mRect.top + mVolumeH / 3

        val rect = RectF(100f, 20f, 200f, 100f)
        paint.color = Color.BLACK
        // 矩形 左右边界 辅助线
//        canvas.drawLine(rect.left, 0f, rect.left, height * 1.0f, paint)
//        canvas.drawLine(rect.right, 0f, rect.right, height * 1.0f, paint)
        // 矩形 上下边界 辅助线
//        canvas.drawLine(rect.left, rect.bottom, width * 1.0f, rect.bottom, paint)
//        canvas.drawLine(rect.left, rect.top, width * 1.0f, rect.top, paint)
        // 原点辅助线
        val centerX = rect.left + (rect.right - rect.left) / 2
        val centerY = rect.top + (rect.bottom - rect.top) / 2
        canvas.drawLine(centerX, centerY, centerX, height * 1.0f, paint)
        canvas.drawLine(centerX, centerY, width * 1.0f, centerY, paint)

        // 绘制矩形
        path.reset()
        paint.color = Color.GREEN
        canvas.drawRect(rect, paint)

        //
        path.reset()
        // 先移动到(0, 0), 由于 路径默认是连续的，所以会有一条线指向 椭圆绘制的起点。
        path.moveTo(0f, 0f)
        // 画弧线
        // 此弧线是以矩形的中心为椭圆的圆心
        // startAngle：开始角度，x轴正方向表示 0。
        // sweepAngle：旋转扫过的角度，>0 沿顺时针方向绘制椭圆。
        // >= 360 椭圆会不存在。
        path.arcTo(rect, 0f, 300f)
        path.close()
        paint.color = Color.RED
        canvas.drawPath(path, paint)

        //
        path.reset()
        path.moveTo(100f, 100f)
        // 强制将圆弧的起点作为绘制的起点
        path.arcTo(rect, 0f, 90f, true)
        paint.color = Color.BLUE
        canvas.drawPath(path, paint)

        paint.color = Color.BLACK
//        canvas.drawLine(10f, 0f, 10f, height * 1.0f, paint)
//        canvas.drawLine(0f, 10f, width * 1.0f, 10f, paint)


//        path.reset()
//        val layerid =
//            canvas.saveLayer(0F, 0F, width.toFloat(), height.toFloat(), null, Canvas.ALL_SAVE_FLAG)
//        canvas.drawBitmap(dstBmp, 0F, 0F, paint)
//        paint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.SRC_IN))
//        canvas.drawBitmap(srcBmp, 100F, 100F, paint)
//        paint.setXfermode(null)
//        canvas.restoreToCount(layerid)
    }


    private fun updatePath() {
//        val left = mRect.left
//        val top = mRect.top
//        val right = mRect.right
//        val bottom = mRect.bottom
//        val height = bottom - top
        mPath.reset()
//        mPath.moveTo(left, top + height / 3)
        mPath.arcTo(RectF(mRect.left, mRect.top, mRect.right, mRect.top / 3), 180F, 180F)
//        mPath.lineTo(right, top + mVolumeH / 3)
        mPath.arcTo(RectF(mRect.left, mRect.bottom / 3, mRect.right, mRect.bottom), 0F, 180F)
//        mPath.lineTo(left, top + mVolumeH / 3)
        mPath.close()
    }

    private fun makeDst(w: Int, h: Int): Bitmap {
        val bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val c = Canvas(bm)
        val p = Paint(Paint.ANTI_ALIAS_FLAG)
        p.color = 0xFFFFCC44.toInt()
        c.drawOval(RectF(0F, 0F, w.toFloat(), h.toFloat()), p)
        return bm
    }

    private fun makeSrc(w: Int, h: Int): Bitmap {
        val bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val c = Canvas(bm)
        val p = Paint(Paint.ANTI_ALIAS_FLAG)
        p.color = 0xFF66AAFF.toInt()
        c.drawRect(0F, 0F, w.toFloat(), h.toFloat(), p)
        return bm
    }


    /**
     * 实现 具体的滚动逻辑。
     */
    override fun computeScroll() {
        super.computeScroll()
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.currX, mScroller.currY)
//            postInvalidate()
        }

    }

    /**
     * startScroll() 内部仅仅是一些属性的赋值，并没有滚动逻辑
     * 需要调用 invalidate()，触发重绘。
     * 滚动逻辑在 computeScroll() 中实现。
     */
    fun customScrollTo(dx: Int, dy: Int) {
        // 从当前位置开始移动。
        // x 右移 100
        // y 下移 100
        mScroller.startScroll(scrollX, scrollY, -100, -100, 1000)
        // 需要调用 invalidate()，触发重绘。
        invalidate()
        // 惯性滚动时 velocityX，velocityY 可以通过使用VelocityTracker获取
        // scroller.fling(startX, startY, velocityX, velocityY, minX, maxX, minY, maxY)
    }
}