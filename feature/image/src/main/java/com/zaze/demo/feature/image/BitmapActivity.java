package com.zaze.demo.feature.image;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.zaze.common.base.AbsActivity;
import com.zaze.common.base.ext.AppCompatActivityExtKt;
import com.zaze.common.thread.ThreadPlugins;
import com.zaze.utils.BmpUtil;
import com.zaze.utils.ext.BitmapExt;
import com.zaze.utils.ext.BitmapExtKt;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2019-06-09 - 1:54
 */
public class BitmapActivity extends AbsActivity {
    private float r = 1F;
    private float g = 1F;
    private float b = 1F;
    private float a = 1F;
    private Paint paint;
    private Canvas canvas;
    private Bitmap originBmp;
    private Bitmap processedBmp;
    private ImageView processImageView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bitmap_act);
        AppCompatActivityExtKt.setupActionBar(this, findViewById(R.id.bitmap_toolbar), new Function2<ActionBar, Toolbar, Unit>() {
            @Override
            public Unit invoke(ActionBar actionBar, Toolbar toolbar) {
                actionBar.setTitle("Bitmap");
                return Unit.INSTANCE;
            }
        });
        originBmp = BitmapExt.INSTANCE.decodeToBitmap(100, 100, new Function1<BitmapFactory.Options, Bitmap>() {
            @Override
            public Bitmap invoke(BitmapFactory.Options options) {
                return BitmapFactory.decodeResource(getResources(), R.drawable.jljt, options);
            }
        });

//        originBmp = BitmapFactory.decodeResource(getResources(), R.drawable.jljt, options);


//        Bitmap bm = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);
//        Canvas sCanvas = new Canvas(bm);
//        sCanvas.drawColor(Color.parseColor("#FFFFFF"));
//        originBmp = BmpUtil.toRoundRectBitmap(originBmp, DisplayUtil.pxFromDp(30));
//        originBmp = CircleBmpKt.innerRound2(originBmp);
        processedBmp = Bitmap.createBitmap(originBmp.getWidth(), originBmp.getHeight(), originBmp.getConfig());

        processImageView = findViewById(R.id.bmp_content_iv);
        processImageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        processImageView.post(new Runnable() {
            @Override
            public void run() {
                processImageView.setImageBitmap(originBmp);
            }
        });
        findViewById(R.id.bmp_compress_btn).setOnClickListener(v -> {
            ThreadPlugins.runInIoThread(new Runnable() {
                @Override
                public void run() {
                    File pngFile = new File(BitmapActivity.this.getFilesDir(), System.currentTimeMillis() + "_precessed.png");
                    File jpgFile = new File(BitmapActivity.this.getFilesDir(), System.currentTimeMillis() + "_precessed.jpg");
                    BitmapExtKt.saveToFile(originBmp, pngFile, Bitmap.CompressFormat.PNG, 100 * 1024L);
                    BitmapExtKt.saveToFile(originBmp, jpgFile, Bitmap.CompressFormat.JPEG, 100 * 1024L);
                }
            });
        });

        ((SeekBar) findViewById(R.id.r_seekbar)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                r = progress / 128f;
                refresh();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        ((SeekBar) findViewById(R.id.g_seekbar)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                g = progress / 128f;
                refresh();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        ((SeekBar) findViewById(R.id.b_seekbar)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                b = progress / 128f;
                refresh();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        ((SeekBar) findViewById(R.id.a_seekbar)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                a = progress / 128f;
                refresh();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void refresh() {
        if (paint == null) {
            paint = new Paint();
        }
        if (canvas == null) {
            canvas = new Canvas(processedBmp);
        }
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.set(new float[]{
                r, 0, 0, 0, 0,
                0, g, 0, 0, 0,
                0, 0, b, 0, 0,
                0, 0, 0, a, 0,
        });
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
//        paint.setColorFilter(new LightingColorFilter(0xffffff, 0));
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(originBmp, new Matrix(), paint);
        processImageView.setImageBitmap(processedBmp);
    }
}
