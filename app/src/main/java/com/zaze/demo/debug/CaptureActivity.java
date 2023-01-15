package com.zaze.demo.debug;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.widget.TextView;

import com.zaze.common.base.AbsActivity;
import com.zaze.demo.R;
import com.zaze.utils.FileUtil;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2019-07-02 - 17:23
 */
public class CaptureActivity extends AbsActivity {
    private MediaProjectionManager mMediaProjectionManager;
    private MediaProjection mMediaProjection;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ok_http_activity);
        mMediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        startActivityForResult(mMediaProjectionManager.createScreenCaptureIntent(), 1);
        TextView textView = new TextView(this);
        textView.setText("aaaaa");
        Dialog dialog = new Dialog(this);
        dialog.setContentView(textView);
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, requestCode, data);
        int mWidth = 300;
        int mHeight = 300;
        mMediaProjection = mMediaProjectionManager.getMediaProjection(resultCode, data);
//            final ImageReader mImageReader = ImageReader.newInstance(mWidth, mHeight, PixelFormat.RGBA_8888, 1);
        final ImageReader mImageReader = ImageReader.newInstance(mWidth, mHeight, ImageFormat.RGB_565, 1);
        VirtualDisplay mVirtualDisplay = mMediaProjection.createVirtualDisplay("screen-mirror", mWidth, mHeight, Resources.getSystem().getDisplayMetrics().densityDpi, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, mImageReader.getSurface(), null, null);
        Image image = mImageReader.acquireLatestImage();
        if (image != null) {
            final Image.Plane[] planes = image.getPlanes();
            if (planes.length > 0) {
                final ByteBuffer buffer = planes[0].getBuffer();
                int pixelStride = planes[0].getPixelStride();
                int rowStride = planes[0].getRowStride();
                int rowPadding = rowStride - pixelStride * mWidth;
                // create bitmap
                Bitmap bmp = Bitmap.createBitmap(mWidth + rowPadding / pixelStride, mHeight, Bitmap.Config.ARGB_8888);

                if (bmp != null) {
                    bmp.copyPixelsFromBuffer(buffer);
                    Bitmap croppedBitmap = Bitmap.createBitmap(bmp, 0, 0, mWidth, mHeight);
                    String fileName = "/sdcard/test.jpg";
                    FileUtil.reCreateFile(fileName);
                    File file = new File(fileName);
                    try {
                        FileOutputStream fos = new FileOutputStream(file);
                        bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                        fos.flush();
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    if (croppedBitmap != null) {
                        croppedBitmap.recycle();
                    }
                    bmp.recycle();
                }
            }
        }
        mVirtualDisplay.release();
    }
}
