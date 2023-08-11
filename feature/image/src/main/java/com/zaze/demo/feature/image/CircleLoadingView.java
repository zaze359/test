package com.zaze.demo.feature.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;


/**
 * This code is copyright (c) 2015 juhuiwan.cn
 * Created by zerob13 on 10/29/15.
 */
public class CircleLoadingView extends View {


    private Bitmap mOriginBitmap;
    private Bitmap mResult;
    /**
     * 带遮罩的logo
     */
    private Paint mGrayPaint;
    /**
     * 画扇形
     */
    private Paint mArcPaint;
    /**
     * 画圆
     */
    private Paint mRingPaint;
    private Paint mCirclePaint;
    private Paint mNormalPaint;
    private Paint mPaintBg;
    /**
     * 画扇形用的矩阵
     */
    private RectF mRectF;
    /**
     * 画进度条用的矩阵
     */
    private RectF mRectF2;
    private int mRingRadius;
    private int mCircleRadius;
    /**
     * 对角线的一半长度
     */
    private float mCircleRadiusMax;
    private float mCircleStep;
    private float mCenterX;
    private float mCenterY;
    private float mAnimationDuration;
    private int mCircleStrokeSize;
    private int mPercent;
    /**
     * 动画状态
     */
    private int mAnimationState;

    private static final int MAX = 100;


    public CircleLoadingView(Context context) {
        this(context, null);
    }

    public CircleLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ColorMatrix cm = new ColorMatrix();
        cm.setScale(0.382f, 0.382f, 0.382f, 1f);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        mGrayPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mGrayPaint.setColorFilter(f);
        mArcPaint = new Paint((Paint.ANTI_ALIAS_FLAG));
        mRingPaint = new Paint((Paint.ANTI_ALIAS_FLAG));
        mCirclePaint = new Paint((Paint.ANTI_ALIAS_FLAG));
        mNormalPaint = new Paint((Paint.ANTI_ALIAS_FLAG));
        mPaintBg = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintBg.setColor(Color.BLACK);
        mAnimationState = 2;
//        final TypedArray typedArray = context
//                .obtainStyledAttributes(attrs, R.styleable.CircleLoadingView);
//        try {
//            Drawable drawable = typedArray
//                    .getDrawable(R.styleable.CircleLoadingView_cl_src);
//            mCircleRadius = typedArray
//                    .getDimensionPixelSize(R.styleable.CircleLoadingView_cl_circleRadius, -1);
//            mAnimationDuration = typedArray
//                    .getFloat(R.styleable.CircleLoadingView_cl_fillAnimationDuration, 800);
//            mCircleStrokeSize = typedArray
//                    .getDimensionPixelSize(R.styleable.CircleLoadingView_cl_circleStrokeSize, -1);
//            if (drawable instanceof BitmapDrawable) {
//                mOriginBitmap = ((BitmapDrawable) drawable).getBitmap();
//                initRect();
//            }
//        } finally {
//            typedArray.recycle();
//        }
    }

    /**
     * 设置bitmap
     *
     * @param bm bitmap
     */
    public void setImageBitmap(Bitmap bm) {
        this.mOriginBitmap = bm;
        initRect();
        postInvalidate();
    }

    /**
     * 初始化矩阵
     */
    private void initRect() {
        int w = this.getMeasuredWidth();
        int h = this.getMeasuredHeight();
        if (w == 0 || h == 0) {
            return;
        }
        if (w > 0 && h > 0) {
            if (mCircleRadius < 0) {
                mCircleRadius = mRingRadius = w / 4;
            } else {
                mRingRadius = mCircleRadius;
            }
           /* mCenterX = w / 2f;
            mCenterY = h / 2f;*/
            mResult = Bitmap.createScaledBitmap(mOriginBitmap, w, h, true);
            //中心圆的矩阵
            mRectF = getRect1();
            mRectF2 = getRect2();
            mRingPaint.setStyle(Paint.Style.STROKE);
            if (mCircleStrokeSize < 0) {
                mCircleStrokeSize = w / 36;
            }
            mRingPaint.setStrokeWidth(mCircleStrokeSize);
            mCircleRadiusMax = (float) Math.sqrt((double) w * w + (double) h * h) / 2f;
            mCircleStep = (mCircleRadiusMax - mCircleRadius) / (mAnimationDuration / 25);
            Matrix m = new Matrix();
            RectF src = new RectF(0, 0, mOriginBitmap.getWidth(), mOriginBitmap.getHeight());
            RectF dst = new RectF(0, 0, this.getWidth(), this.getHeight());
            m.setRectToRect(src, dst, Matrix.ScaleToFit.CENTER);
            Shader shader = new BitmapShader(mOriginBitmap, Shader.TileMode.CLAMP,
                    Shader.TileMode.CLAMP);
            shader.setLocalMatrix(m);
            mArcPaint.setShader(shader);
            mRingPaint.setShader(shader);
            mCirclePaint.setShader(shader);
        }
    }


    /**
     * dp转px
     *
     * @param dp dp
     * @return px
     */
    private int dp2px(float dp) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5F);
    }


    /**
     * 返回圆形图像矩阵
     *
     * @return 圆形图像矩阵
     */
    private RectF getRect1() {
        if (mRectF == null) {
            mRectF = new RectF((dp2px(20)), dp2px(10), dp2px(52), dp2px(42));
        }
        return mRectF;
    }

    /**
     * 返回圆角图像矩阵
     *
     * @return 圆角图像矩阵
     */
    private RectF getRect2() {
        if (mRectF2 == null) {
            mRectF2 = new RectF(dp2px(12), dp2px(29), dp2px(60), dp2px(35));
        }
        return mRectF2;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (mOriginBitmap == null) {
            return;
        }
        final int percent = mPercent;
        int saveCount = canvas.save();
        drawProcess(canvas, percent);
        canvas.restoreToCount(saveCount);
    }


    /**
     * 绘制进度条
     *
     * @param canvas  画布
     * @param percent 百分比  0~100
     */
    private void drawProcess(Canvas canvas, int percent) {
        if (percent == 0) {
            //等待中,仅绘制遮罩层
            canvas.drawBitmap(mResult, 0, 0, mGrayPaint);
        } else if (percent < MAX) {
            //绘制动画
            canvas.drawBitmap(mResult, 0, 0, mGrayPaint);
            realDraw(canvas, percent);
            if (mAnimationState == 2) {
                mAnimationState = 0;
            }
        } else if (percent == MAX && mAnimationState == 0) {
            //状态满了,圆扩大到铺满
            canvas.drawBitmap(mResult, 0, 0, mGrayPaint);
            canvas.drawCircle(mCenterX, mCenterY, mCircleRadius, mCirclePaint);
            mCircleRadius += mCircleStep;
            mAnimationState = 1;
            postInvalidateDelayed(100);
        } else if (mAnimationState == 1) {
            //铺满动画
            canvas.drawBitmap(mResult, 0, 0, mGrayPaint);
            canvas.drawCircle(mCenterX, mCenterY, mCircleRadius, mCirclePaint);
            mCircleRadius += mCircleStep;
            if (mCircleRadius >= mCircleRadiusMax) {
                mAnimationState = 2;
                mCircleRadius = mRingRadius;
            } else {
                postInvalidateDelayed(25);
            }
        } else {
            //单画背景
            canvas.drawBitmap(mResult, 0, 0, mNormalPaint);
        }
    }


    public static final int TYPE_ARC = 1;
    public static final int TYPE_ROUND_RECT = 2;

    private int animationType = TYPE_ARC;

    /**
     * 设置动画类型
     *
     * @param type 动画类型
     */
    public void setAnimationType(int type) {
        animationType = type;
    }

    /**
     * 绘制进度
     *
     * @param canvas  画布
     * @param percent 百分比
     */
    private void realDraw(Canvas canvas, int percent) {
        switch (animationType) {
            case TYPE_ARC:
                canvas.drawCircle(dp2px(36), dp2px(26), dp2px(16), mRingPaint);
                canvas.drawArc(mRectF, -90, (float) ((1.0f * percent / MAX) * 360.0), true, mArcPaint);
                break;
            case TYPE_ROUND_RECT:
                RectF rectF = new RectF(mRectF2.left, mRectF2.top, mRectF2.left + (mRectF2.right - mRectF2.left) * percent / 100, mRectF2.bottom);
                canvas.drawRoundRect(mRectF2, dp2px(6), dp2px(6), mPaintBg);
                canvas.drawRoundRect(rectF, dp2px(6), dp2px(6), mArcPaint);
                break;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w != oldw || h != oldh) {
            if (mResult != null) {
                mResult = null;
            }
            if (mOriginBitmap != null) {
                initRect();
            }
        }
    }

    /**
     * 设置下载进度
     *
     * @param percent 百分比   0~100
     */
    public void setPercent(int percent) {
        if (mPercent != percent) {
            mPercent = percent;
            postInvalidate();
        }
    }


}

