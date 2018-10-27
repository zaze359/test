package com.zaze.common.widget.head;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zaze.common.R;
import com.zaze.common.util.ActivityUtil;
import com.zaze.utils.ZOnClickHelper;

import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.StringRes;

/**
 * Description :
 * date : 2015-12-07 - 11:29
 *
 * @author : zaze
 * @version : 1.0
 */
public class HeadWidget extends BaseHeadView {
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

    private ImageView headRightAfterIcon;

    public HeadWidget(Context context, @LayoutRes int layoutResId) {
        super(context, layoutResId);
    }

    public HeadWidget(Context context, @LayoutRes int layoutResId, ViewGroup parent) {
        super(context, layoutResId, parent);
    }

    @Override
    public void initHeadView(View headView) {
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

    @Override
    public @LayoutRes
    int getHeadLayoutId() {
        return R.layout.layout_main_head;
    }

    /**
     * 设置文本
     *
     * @param resId
     * @param orientation
     * @return
     */
    @Override
    public HeadWidget setText(@StringRes int resId, @ZOrientation int orientation) {
        switch (orientation) {
            case ZOrientation.LEFT:
                setLeftText(resId);
                break;
            case ZOrientation.RIGHT:
                setRightText(resId);
                break;
            case ZOrientation.CENTER:
                setTitleText(resId);
                break;
            default:
                break;
        }
        return this;
    }

    /**
     * 设置文本
     *
     * @param text
     * @param orientation
     * @return
     */
    @Override
    public HeadWidget setText(String text, @ZOrientation int orientation) {
        switch (orientation) {
            case ZOrientation.LEFT:
                setLeftText(text);
                break;
            case ZOrientation.RIGHT:
                setRightText(text);
                break;
            case ZOrientation.CENTER:
                setTitleText(text);
                break;
            default:
                break;
        }
        return this;
    }

    @Override
    public HeadFace setIcon(@DrawableRes int resIcon, @ZOrientation int orientation) {
        switch (orientation) {
            case ZOrientation.LEFT:
                setLeftIcon(resIcon);
                break;
            case ZOrientation.RIGHT:
                setRightIcon(resIcon);
                break;
            case ZOrientation.CENTER:
                setTitleIcon(0, 0, resIcon, 0);
                break;
            default:
                break;
        }
        return this;
    }

    @Override
    public HeadFace setOnClickListener(View.OnClickListener listener, @ZOrientation int orientation) {
        switch (orientation) {
            case ZOrientation.LEFT:
                setLeftOnClickListener(listener);
                break;
            case ZOrientation.RIGHT:
                setRightOnClickListener(listener);
                break;
            case ZOrientation.CENTER:
                setTitleOnClickListener(listener);
                break;
            default:
                break;
        }
        return this;
    }

    @Override
    public HeadWidget setBackClickListener(final Activity activity) {
        setLeftVisibility(View.VISIBLE);
        ZOnClickHelper.setOnClickListener(headLeftLayout, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                InputMethodUtil.dismiss(activity);
                ActivityUtil.finish(activity);
            }
        });
        return this;
    }

    @Override
    public HeadFace setVisibility(int visibility, @ZOrientation int orientation) {
        switch (orientation) {
            case ZOrientation.LEFT:
                setLeftVisibility(visibility);
                break;
            case ZOrientation.RIGHT:
                setRightVisibility(visibility);
                break;
            case ZOrientation.CENTER:
//                setTitleOnClickListener(visibility);
                break;
            default:
                break;
        }
        return this;
    }

    @Override
    public View getView(@ZOrientation int orientation) {
        switch (orientation) {
            case ZOrientation.LEFT:
                return getLeftChild();
            case ZOrientation.RIGHT:
                return getRightChild();
            case ZOrientation.CENTER:
                return getCenterChild();
            default:
                return getContainerView();
        }
    }

    // ----------------------- CENTER ----------------------------
    private HeadWidget setTitleText(String text) {
        if (text != null) {
            headTitleText.setText(text);
        }
        return this;
    }

    private HeadWidget setTitleText(int resId) {
        headTitleText.setText(getResString(resId));
        return this;
    }

    public HeadWidget setTitleIcon(int left, int top, int right, int bottom) {
        headTitleText.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
        return this;
    }

    private HeadWidget setTitleOnClickListener(View.OnClickListener onClickListener) {
        if (onClickListener != null) {
            ZOnClickHelper.setOnClickListener(headTitleText, onClickListener);
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

    // ----------------------- LEFT ----------------------------
    private HeadWidget setLeftText(String text) {
        if (text != null) {
            headLeftText.setText(text);
        }
        return this;
    }

    private HeadWidget setLeftText(int resId) {
        headLeftText.setText(getResString(resId));
        return this;
    }

    private HeadWidget setLeftRed(String text) {
        if (text != null) {
            headLeftRed.setText(text);
        }
        return this;
    }

    private HeadWidget setLeftRedVisibility(int visibility) {
        headLeftRed.setVisibility(visibility);
        return this;
    }

    private HeadWidget setLeftVisibility(int visibility) {
        headLeftLayout.setVisibility(visibility);
        return this;
    }

    private HeadWidget setLeftOnClickListener(View.OnClickListener onClickListener) {
        if (onClickListener != null) {
            ZOnClickHelper.setOnClickListener(headLeftLayout, onClickListener);
        }
        return this;
    }

    public HeadWidget setLeftIcon(Bitmap bmp) {
        Drawable drawable = null;
        if (bmp != null) {
            drawable = new BitmapDrawable(getResources(), bmp);
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

    // ----------------------- RIGHT ----------------------------
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
            ZOnClickHelper.setOnClickListener(headRightText, onClickListener);
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
            ZOnClickHelper.setOnClickListener(headRightBeforeIcon, onClickListener);
        }
        return this;
    }

    public HeadWidget setRightAfterIconOnClickListener(View.OnClickListener onClickListener) {
        if (onClickListener != null) {
            ZOnClickHelper.setOnClickListener(headRightAfterIcon, onClickListener);
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
            return getContainerView().getContext().getString(resId);
        }
        return "";
    }

    // ----------
//    public Toolbar getToolbar() {
//        return headToolbar;
//    }

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