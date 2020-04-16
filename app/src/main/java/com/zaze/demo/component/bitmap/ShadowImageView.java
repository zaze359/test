package com.zaze.demo.component.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2019-08-17 - 17:44
 */
public class ShadowImageView extends View {
    private Paint paint = new Paint();
    private Bitmap bitmap;
    private int width;
    private int height;

    public ShadowImageView(Context context) {
        super(context);
    }

    public ShadowImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ShadowImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init() {
//            paint.setAntiAlias(true);
//            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        paint = new Paint();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
    }

    public void setImageBitmap(Bitmap bitmap) {
        this.bitmap = Bitmap.createScaledBitmap(bitmap, width - 100, height - 100, true);
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (bitmap == null) {
            return;
        }
//        canvas.drawCircle(200f, 200f, 100f, paint);
//        canvas.translate(350f, 0f);
//        paint.setMaskFilter(new BlurMaskFilter(100f, BlurMaskFilter.Blur.SOLID));
        paint.setShadowLayer(10f, 0, 0, Color.BLACK);
//        canvas.drawCircle(200f, 200f, 100f, paint);

//        canvas.translate(-350f, 350f);
//        paint.setMaskFilter(new BlurMaskFilter(100f, BlurMaskFilter.Blur.NORMAL));
//        canvas.drawCircle(200f, 200f, 100f, paint);
//
//        canvas.translate(350f, 0f);
//        paint.setMaskFilter(new BlurMaskFilter(100f, BlurMaskFilter.Blur.OUTER));
//        canvas.drawCircle(200f, 200f, 100f, paint);
//
//        canvas.translate(-350f, 350f);
//        paint.setMaskFilter(new BlurMaskFilter(100f, BlurMaskFilter.Blur.SOLID));
        canvas.drawBitmap(bitmap, 50f, 50f, paint);
    }

    Rect getRect(Canvas canvas) {
        Rect rect = canvas.getClipBounds();
        rect.bottom -= getPaddingBottom();
        rect.right -= getPaddingRight();
        rect.left += getPaddingLeft();
        rect.top += getPaddingTop();
        return rect;
    }

}
