package com.zaze.aarrepo.commons.widget;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zaze.aarrepo.R;
import com.zaze.aarrepo.utils.ActivityUtil;
import com.zaze.aarrepo.utils.helper.OnClickHelper;

/**
 * Description :
 * date : 2015-12-07 - 11:29
 *
 * @author : zaze
 * @version : 1.0
 */
public class HeadWidget {
    public static final int LEFT = 2;
    public static final int CENTER = 5;
    public static final int RIGHT = 3;
    //
    private View headBottomLine;

    private TextView headLeftRed;
    private TextView headRightRed;

    private TextView headLeftText;
    private LinearLayout headLeftLayout;
    //
    private TextView headRightText;
    private ImageView headRightBeforeIcon;
    private View headRightLayout;
    //
    private TextView headTitleText;
    private LinearLayout headTitleLayout;

    //    private Toolbar headToolbar;
    //
    private LayoutInflater inflater;
    private FrameLayout containerView;
    private View headView;
    private ImageView headRightAfterIcon;

    //
    public HeadWidget(Context context, int layoutResId) {
        this(context, layoutResId, null);
    }

    public HeadWidget(Context context, int layoutResId, ViewGroup parent) {
        inflater = LayoutInflater.from(context);
        containerView = (FrameLayout) inflater.inflate(R.layout.layout_head_container, parent, false);
        initUserView(context, layoutResId);
        initHeadView();
    }

    private void initHeadView() {
        headView = inflater.inflate(R.layout.layout_main_head, containerView);
//        headToolbar = ViewUtil.findView(headView, R.id.head_tool_bar);
//        headBackIcon = (ImageView) view.findViewById(R.id.head_back_icon);
        headLeftText = (TextView) headView.findViewById(R.id.head_left_text);
        headLeftRed = (TextView) headView.findViewById(R.id.head_left_red);
        headRightRed = (TextView) headView.findViewById(R.id.head_right_red);
        headLeftLayout = (LinearLayout) headView.findViewById(R.id.head_left_layout);
        headTitleText = (TextView) headView.findViewById(R.id.head_title_text);
        headRightText = (TextView) headView.findViewById(R.id.head_right_text);
        headRightBeforeIcon = (ImageView) headView.findViewById(R.id.head_right_before_icon);
        headRightLayout = headView.findViewById(R.id.head_right_layout);
        headTitleLayout = (LinearLayout) headView.findViewById(R.id.head_title_layout);
        headBottomLine = headView.findViewById(R.id.head_bottom_line);
        headRightAfterIcon = (ImageView) headView.findViewById(R.id.head_right_after_icon);
        headRightAfterIcon.setVisibility(View.GONE);
        headRightBeforeIcon.setVisibility(View.GONE);
        setLeftVisibility(View.INVISIBLE);
        setLeftRedVisibility(View.INVISIBLE);
    }

    private void initUserView(Context context, int layoutResID) {
        View userView = inflater.inflate(layoutResID, containerView, false);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        params.topMargin = (int) context.getResources().getDimension(R.dimen.head_height);
        containerView.addView(userView, params);
    }

    /**
     * 设置文本
     *
     * @param text
     * @param orientation
     * @return
     */
    public HeadWidget setText(String text, int orientation) {
        switch (orientation) {
            case LEFT:
                setLeftText(text);
                break;
            case RIGHT:
                setRightText(text);
                break;
            case CENTER:
                setTitleText(text);
                break;
            default:
                break;
        }

        return this;
    }

    /**
     * 设置文本
     *
     * @param resId
     * @param orientation
     * @return
     */
    public HeadWidget setText(int resId, int orientation) {
        switch (orientation) {
            case LEFT:
                setLeftText(resId);
                break;
            case RIGHT:
                setRightText(resId);
                break;
            case CENTER:
                setTitleText(resId);
                break;
            default:
                break;
        }

        return this;
    }


    // ----------------------- center ----------------------------
    public HeadWidget setTitleText(String text) {
        if (text != null) {
            headTitleText.setText(text);
        }
        return this;
    }

    public HeadWidget setTitleText(int resId) {
        headTitleText.setText(getResString(resId));
        return this;
    }

    public HeadWidget setTitleIcon(int left, int top, int right, int bottom) {
        headTitleText.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
        return this;
    }

    public HeadWidget setTitleOnClickListener(View.OnClickListener onClickListener) {
        if (onClickListener != null) {
            OnClickHelper.setOnClickListener(headTitleText, onClickListener);
        }
        return this;
    }

    public HeadWidget setTitleRightIcon(int resId) {
        headTitleText.setCompoundDrawablesWithIntrinsicBounds(0, 0, resId, 0);
        return this;
    }

    public TextView getCenterChild() {
        return headTitleText;
    }

    // ----------------------- left ----------------------------
    public HeadWidget setLeftText(String text) {
        if (text != null) {
            headLeftText.setText(text);
        }
        return this;
    }

    public HeadWidget setLeftText(int resId) {
        headLeftText.setText(getResString(resId));
        return this;
    }

    public HeadWidget setLeftRed(String text) {
        if (text != null) {
            headLeftRed.setText(text);
        }
        return this;
    }

    public HeadWidget setLeftRedVisibility(int visibility) {
        headLeftRed.setVisibility(visibility);
        return this;
    }

    public HeadWidget setLeftVisibility(int visibility) {
        headLeftLayout.setVisibility(visibility);
        return this;
    }

    public HeadWidget setLeftOnClickListener(View.OnClickListener onClickListener) {
        if (onClickListener != null) {
            OnClickHelper.setOnClickListener(headLeftLayout, onClickListener);
        }
        return this;
    }

    public HeadWidget setBackClickListener(final Activity activity) {
        setLeftVisibility(View.VISIBLE);
        OnClickHelper.setOnClickListener(headLeftLayout, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                InputMethodUtil.dismiss(activity);
                ActivityUtil.finish(activity);
            }
        });
        return this;
    }

    public HeadWidget setLeftIcon(Bitmap bmp) {
        Drawable drawable = null;
        if (bmp != null) {
            drawable = new BitmapDrawable(bmp);
        }
        headLeftText.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
//        headTitleText.setImageBitmap(bmp);
        return this;
    }

    public HeadWidget setLeftIcon(int resId) {
        headLeftText.setCompoundDrawablesWithIntrinsicBounds(resId, 0, 0, 0);
//        headBackIcon.setImageResource(resId);
        return this;
    }

    public LinearLayout getLeftChild() {
        return headLeftLayout;
    }

    // ----------------------- right ----------------------------
    public HeadWidget setRightText(String text) {
        if (text != null) {
            headRightText.setText(text);
        }
        return this;
    }

    public HeadWidget setRightText(int resId) {
        headRightText.setText(getResString(resId));
        return this;
    }

    public HeadWidget setRightRed(String text) {
        if (text != null) {
            headRightRed.setText(text);
        }
        return this;
    }


    public HeadWidget setRightIcon(int left, int top, int right, int bottom) {
        headRightText.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
        return this;
    }

    public HeadWidget setRightIcon(int resId) {
        if (resId > 0) {
            headRightText.setCompoundDrawablesWithIntrinsicBounds(0, 0, resId, 0);
        }
        return this;
    }

    public HeadWidget setRightBeforeIcon(int resId) {
        if (resId > 0) {
            headRightBeforeIcon.setImageResource(resId);
            headRightBeforeIcon.setVisibility(View.VISIBLE);
        }
        return this;
    }

    public HeadWidget setRightAfterIcon(int resId) {
        if (resId > 0) {
            headRightAfterIcon.setImageResource(resId);
            headRightAfterIcon.setVisibility(View.VISIBLE);
        }
        return this;
    }

//    /**
//     * 头 右边整个点击
//     *
//     * @param onClickListener
//     * @return
//     */
//    public HeadWidget setRightOnClickListener(View.OnClickListener onClickListener) {
//        if (onClickListener != null) {
//            OnClickHelper.setOnClickListener(headRightLayout, onClickListener);
//        }
//        return this;
//    }

    /**
     * 头 右边文字点击
     *
     * @param onClickListener
     * @return
     */
    public HeadWidget setRightOnClickListener(View.OnClickListener onClickListener) {
        if (onClickListener != null) {
            OnClickHelper.setOnClickListener(headRightText, onClickListener);
        }
        return this;
    }

    /**
     * 头 右边图片点击
     *
     * @param onClickListener
     * @return
     */
    public HeadWidget setRightBeforeIconOnClickListener(View.OnClickListener onClickListener) {
        if (onClickListener != null) {
            OnClickHelper.setOnClickListener(headRightBeforeIcon, onClickListener);
        }
        return this;
    }

    public HeadWidget setRightAfterIconOnClickListener(View.OnClickListener onClickListener) {
        if (onClickListener != null) {
            OnClickHelper.setOnClickListener(headRightAfterIcon, onClickListener);
        }
        return this;
    }

    public HeadWidget setRightVisibility(int visibility) {
        headRightLayout.setVisibility(visibility);
        return this;
    }

    public HeadWidget setHeadBottomLineGone() {
        headBottomLine.setVisibility(View.GONE);
        return this;
    }


    // ----------
    private String getResString(int resId) {
        if (resId > 0) {
            return containerView.getContext().getString(resId);
        }
        return "";
    }

    // ----------
//    public Toolbar getToolbar() {
//        return headToolbar;
//    }

    public FrameLayout getContainerView() {
        return containerView;
    }

    public View getRightChild() {
        return headRightLayout;
    }

    public ImageView getHeadRightBeforeIcon() {
        return headRightBeforeIcon;
    }

    public TextView getHeadRightText() {
        return headRightText;
    }
}