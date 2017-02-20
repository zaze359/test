package com.zaze.aarrepo.commons.widget.head;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.zaze.aarrepo.R;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-01-11 - 09:20
 */
public abstract class BaseHeadView implements HeadFace {
    private LayoutInflater inflater;
    private FrameLayout containerView;
    private View headView;

    //
    public BaseHeadView(Context context, int layoutResId) {
        this(context, layoutResId, null);
    }

    public BaseHeadView(Context context, int layoutResId, ViewGroup parent) {
        inflater = LayoutInflater.from(context);
        containerView = (FrameLayout) inflater.inflate(R.layout.layout_head_container, parent, false);
        init(context, layoutResId);
    }

    private void init(Context context, int layoutResID) {
        View userView = inflater.inflate(layoutResID, containerView, false);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        params.topMargin = (int) context.getResources().getDimension(R.dimen.head_height);
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

    // ------
    public abstract void initHeadView(View view);

    public abstract int getHeadLayoutId();


}