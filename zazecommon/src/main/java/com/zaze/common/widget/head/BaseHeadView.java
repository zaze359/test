package com.zaze.common.widget.head;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.zaze.common.R;


/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-01-11 - 09:20
 */
public abstract class BaseHeadView implements HeadFace {
    private LayoutInflater inflater;
    private Resources resources;
    private FrameLayout containerView;
    private View headView;

    public BaseHeadView(Context context, @LayoutRes int layoutResId) {
        this(context, layoutResId, null);
    }

    public BaseHeadView(Context context, @LayoutRes int layoutResId, ViewGroup parent) {
        inflater = LayoutInflater.from(context);
        resources = context.getResources();
        containerView = (FrameLayout) inflater.inflate(R.layout.layout_head_container, parent, false);
        init(layoutResId);
    }

    private void init(@LayoutRes int layoutResID) {
        View userView = inflater.inflate(layoutResID, containerView, false);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        params.topMargin = (int) resources.getDimension(R.dimen.head_height);
        containerView.addView(userView, params);
        //
        headView = inflater.inflate(getHeadLayoutId(), containerView);
        initHeadView(headView);
    }


    public View getHeadView() {
        return headView;
    }

    public FrameLayout getContainerView() {
        return containerView;
    }

    public LayoutInflater getInflater() {
        return inflater;
    }

    public Resources getResources() {
        return resources;
    }

    // --------------------------------------------------

    /**
     * 初始化HeadView
     *
     * @param view view
     */
    public abstract void initHeadView(View view);

    /**
     * 获取HeadLayout
     *
     * @return int
     */
    public abstract @LayoutRes
    int getHeadLayoutId();
}