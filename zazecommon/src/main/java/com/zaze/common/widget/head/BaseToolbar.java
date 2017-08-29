package com.zaze.common.widget.head;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-01-10 - 15:33
 */
public class BaseToolbar extends BaseHeadView {

    public BaseToolbar(Context context, int layoutResId) {
        super(context, layoutResId);
    }

    public BaseToolbar(Context context, int layoutResId, ViewGroup parent) {
        super(context, layoutResId, parent);
    }

    @Override
    public void initHeadView(View view) {

    }

    @Override
    public int getHeadLayoutId() {
        return 0;
    }

    @Override
    public HeadFace setText(int resId, @ZOrientation int orientation) {
        return null;
    }

    @Override
    public HeadFace setText(String text, @ZOrientation int orientation) {
        return null;
    }

    @Override
    public HeadFace setIcon(int resIcon, @ZOrientation int orientation) {
        return null;
    }

    @Override
    public HeadFace setOnClickListener(View.OnClickListener listener, @ZOrientation int orientation) {
        return null;
    }

    @Override
    public HeadFace setBackClickListener(Activity activity) {
        return null;
    }

    @Override
    public HeadFace setVisibility(int visibility, @ZOrientation int orientation) {
        return null;
    }

    @Override
    public View getView(@ZOrientation int orientation) {
        return null;
    }

}
