package com.zaze.demo.debug;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2019-05-30 - 09:29
 */
public class MyButton extends Button {

    public MyButton(Context context) {
        super(context);
    }

    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
//        Log.i("MyButton", "onDraw : " + Thread.currentThread());
//        for (StackTraceElement element : stackTraceElements) {
//            Log.i("MyButton", "onDraw element : " + element);
//        }
    }

    @Override
    public void requestLayout() {
        super.requestLayout();
//        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
//        Log.i("MyButton", "requestLayout : " + Thread.currentThread());
//        for (StackTraceElement element : stackTraceElements) {
//            Log.i("MyButton", "requestLayout element : " + element);
//        }
    }

    @Override
    public boolean post(Runnable action) {
//        Log.i("MyButton", "post : " + Thread.currentThread());
        return super.post(action);
    }
}
