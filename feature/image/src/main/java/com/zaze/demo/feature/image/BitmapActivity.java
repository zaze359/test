package com.zaze.demo.feature.image;


import android.app.WallpaperManager;
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
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.ViewSwitcher;


import com.zaze.common.base.AbsActivity;
import com.zaze.common.base.ext.AppCompatActivityExtKt;
import com.zaze.common.thread.ThreadPlugins;
import com.zaze.demo.feature.image.databinding.BitmapActBinding;
import com.zaze.utils.ext.BitmapExtKt;

import org.jetbrains.annotations.Nullable;

import java.io.File;

import kotlin.Unit;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2019-06-09 - 1:54
 */
public class BitmapActivity extends AbsActivity implements ViewSwitcher.ViewFactory {
    private float r = 1F;
    private float g = 1F;
    private float b = 1F;
    private float a = 1F;
    private Paint paint;
    private Canvas canvas;
    private Bitmap originBmp;
    private Bitmap processedBmp;
    private BitmapActBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = BitmapActBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AppCompatActivityExtKt.initToolbar(this, binding.bitmapToolbar, (actionBar, toolbar) -> {
            actionBar.setTitle("Bitmap");
            return Unit.INSTANCE;
        });

        originBmp = BitmapFactory.decodeResource(getResources(), R.drawable.jljt);
//        originBmp = BitmapExt.INSTANCE.decodeToBitmap(300, 300, new Function1<BitmapFactory.Options, Bitmap>() {
//            @Override
//            public Bitmap invoke(BitmapFactory.Options options) {
//                return BitmapFactory.decodeResource(getResources(), R.drawable.jljt, options);
//            }
//        });
//
//        Bitmap bm = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);
//        Canvas sCanvas = new Canvas(bm);
//        sCanvas.drawColor(Color.parseColor("#FFFFFF"));
//        originBmp = BmpUtil.toRoundRectBitmap(originBmp, DisplayUtil.pxFromDp(30));
//        originBmp = CircleBmpKt.innerRound2(originBmp);
        processedBmp = Bitmap.createBitmap(originBmp.getWidth(), originBmp.getHeight(), originBmp.getConfig());
        binding.bmpSwitcher.setFactory(this);

        binding.bmpContentIv.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        binding.bmpContentIv.post(new Runnable() {
            @Override
            public void run() {
                binding.bmpContentIv.setImageBitmap(originBmp);
            }
        });

        binding.bmpCompressBtn.setOnClickListener(v -> {
            ThreadPlugins.runInIoThread(new Runnable() {
                @Override
                public void run() {
                    File pngFile = new File(BitmapActivity.this.getFilesDir(), System.currentTimeMillis() + "_precessed.png");
                    File jpgFile = new File(BitmapActivity.this.getFilesDir(), System.currentTimeMillis() + "_precessed.jpg");
                    BitmapExtKt.writeToFileLimited(originBmp, pngFile, Bitmap.CompressFormat.PNG, 100 * 1024L);
                    BitmapExtKt.writeToFileLimited(originBmp, jpgFile, Bitmap.CompressFormat.JPEG, 100 * 1024L);
                }
            });
        });


        binding.rSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
        binding.gSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
        binding.bSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
        binding.aSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
        binding.bmpContentIv.setImageBitmap(processedBmp);

//        if(processedBmp != null) {
//            binding.bmpSwitcher.setImageURI(Uri.parse(""));
//        }
    }

    @Override
    public View makeView() {
        final ImageView i = new ImageView(this);
        i.setBackgroundColor(0xff000000);
        i.setScaleType(ImageView.ScaleType.CENTER_CROP);
        i.setLayoutParams(new ImageSwitcher.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        return i ;
    }
}
