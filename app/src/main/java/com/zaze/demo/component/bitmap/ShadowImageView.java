package com.zaze.demo.component.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;

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
        paint = new Paint();
        setLayerType(LAYER_TYPE_SOFTWARE, paint);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
    }

    public void setImageBitmap(Bitmap bitmap) {
        ZLog.i(ZTag.TAG_DEBUG, "setImageBitmap bmp: " + bitmap.getWidth() + "x" + bitmap.getHeight() + " >> " + width + "x" + height);
        this.bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
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

//        Matrix m = new Matrix();
//        RectF src = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());
//        RectF dst = new RectF(0, 0, this.getWidth(), this.getHeight());
//        m.setRectToRect(src, dst, Matrix.ScaleToFit.CENTER);
//        Shader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP,
//                Shader.TileMode.CLAMP);
//        shader.setLocalMatrix(m);
//        paint.setShader(shader);


//        canvas.translate(-350f, 350f);

//        paint.setMaskFilter(new BlurMaskFilter(10f, BlurMaskFilter.Blur.NORMAL));

//        canvas.drawCircle(200f, 200f, 100f, paint);
//
//        canvas.translate(350f, 0f);
//        paint.setMaskFilter(new BlurMaskFilter(100f, BlurMaskFilter.Blur.OUTER));
//        canvas.drawCircle(200f, 200f, 100f, paint);
//
//        canvas.translate(-350f, 350f);
//        paint.setMaskFilter(new BlurMaskFilter(100f, BlurMaskFilter.Blur.SOLID));


//
//
//        Matrix matrix = new Matrix();
//        matrix.preScale(1, -1);
//
//        //这里的高度，可以自己修改，我这里不做通用。
//        int reflectionHeight = 194;

        //快速高速模糊，倍数最大，但是效率也是最慢的
//        Bitmap blur = ImageUtils.stackBlur(bitmap, 25);
//        Bitmap bitmapWithReflection = Bitmap.createBitmap(width, reflectionHeight, Bitmap.Config.ARGB_8888);
//        Canvas canvas1 = new Canvas(blur);
//        canvas1.drawColor(ContextCompat.getColor(context, R.color.transparent5));
//
//        Canvas canvas = new Canvas(bitmapWithReflection);
//        Paint mPaint = new Paint();
//        mPaint.setAlpha(150);//设置透明度
//        mPaint.setMaskFilter(new BlurMaskFilter(50, BlurMaskFilter.Blur.NORMAL));//设置发光样式,NORMAL是内外发光
//        //阴影部分左右都缩小40dpi，令左右边距圆滑
//        canvas.drawBitmap(blur, null, new RectF(ConvertUtils.dp2px(40), 0, width - ConvertUtils.dp2px(40), reflectionHeight - ConvertUtils.dp2px(20)), mPaint);
//        //设置阴影图
//        iv_shadow_image.setImageBitmap(bitmapWithReflection);

//        paint.setShadowLayer(20f, 0, 0, Color.BLACK);
//        Bitmap bmp = bitmap.extractAlpha();
        ZLog.i(ZTag.TAG_DEBUG, "onDraw bmp: " + bitmap.getWidth() + "x" + bitmap.getHeight());
//        canvas.drawBitmap(bmp, 50f, 50f, paint);
//        paint.reset();
        canvas.drawBitmap(bitmap, 0F, 0F, paint);
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
