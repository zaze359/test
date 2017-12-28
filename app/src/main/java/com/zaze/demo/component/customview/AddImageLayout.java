package com.zaze.demo.component.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.zaze.demo.R;
import com.zaze.utils.ZDisplayUtil;

import java.util.ArrayList;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-12-21 - 22:32
 */
public class AddImageLayout extends ViewGroup {
    private static int LINE_IMAGE_COUNT = 4;
    private static int MAX_IMAGE_LINE = 2;

    private int hSpace = ZDisplayUtil.dp2px(12);
    private int vSpace = ZDisplayUtil.dp2px(12);

    private int childWidth;
    private int childHeight;

    private int addImageResId = android.R.drawable.ic_input_add;

    public void setAddImageResId(int addImageResId) {
        this.addImageResId = addImageResId;
    }

    private ArrayList<Integer> imageResList = new ArrayList<>(LINE_IMAGE_COUNT * MAX_IMAGE_LINE);


    public AddImageLayout(Context context) {
        this(context, null);
    }

    public AddImageLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AddImageLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AddImageLayout, 0, 0);
        hSpace = typedArray.getDimensionPixelSize(R.styleable.AddImageLayout_hSpace, hSpace);
        vSpace = typedArray.getDimensionPixelSize(R.styleable.AddImageLayout_vSpace, vSpace);
        typedArray.recycle();
        // 首先添加一个空view 用于显示添加图标
        addView(new View(getContext()));
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        // --------------------------------------------------
        childWidth = (width - (LINE_IMAGE_COUNT - 1) * hSpace) / LINE_IMAGE_COUNT;
        childHeight = childWidth;
        // --------------------------------------------------
        int childCount = this.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = this.getChildAt(i);
            LayoutParams childLayoutParams = (LayoutParams) child.getLayoutParams();
            childLayoutParams.leftOffset = (childWidth + hSpace) * (i % LINE_IMAGE_COUNT);
            childLayoutParams.topOffset = (childHeight + vSpace) * (i / LINE_IMAGE_COUNT);
            child.setLayoutParams(childLayoutParams);
        }
        int height = childHeight + (childCount / LINE_IMAGE_COUNT) * (childHeight + vSpace);
        setMeasuredDimension(width, height);
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = this.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = this.getChildAt(i);
            LayoutParams childLayoutParams = (LayoutParams) child.getLayoutParams();
            child.layout(childLayoutParams.leftOffset, childLayoutParams.topOffset,
                    childLayoutParams.leftOffset + childWidth,
                    childLayoutParams.topOffset + childHeight);

            if (hasVacancy() && i == childCount - 1) {
                child.setBackgroundResource(addImageResId);
                child.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        addImageRes(R.mipmap.ic_launcher);
                    }
                });
            } else if (i < imageResList.size()) {
                child.setBackgroundResource(imageResList.get(i));
                child.setOnClickListener(null);
            }
        }
    }


    public void addImageRes(@DrawableRes int resId) {
        if (hasVacancy()) {
            addView(new View(getContext()));
            imageResList.add(resId);
            requestLayout();
            invalidate();
        }
    }

    /**
     * 是否有空位
     *
     * @return true 有空位
     */
    private boolean hasVacancy() {
        return imageResList.size() < LINE_IMAGE_COUNT * MAX_IMAGE_LINE;
    }

    // --------------------------------------------------

    class LayoutParams extends ViewGroup.LayoutParams {
        int leftOffset;
        int topOffset;


        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }
}
