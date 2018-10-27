package com.zaze.common.base.header;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zaze.common.base.BaseFragment;
import com.zaze.common.base.BaseView;
import com.zaze.common.util.ViewUtil;
import com.zaze.common.widget.head.BaseHeadView;
import com.zaze.common.widget.head.HeadFace;
import com.zaze.common.widget.head.HeadWidget;


/**
 * Description :
 *
 * @author : zaze
 * @version : 2015-09-22 - 19:38
 */
public abstract class BaseHeaderFragment extends BaseFragment implements BaseView {
    private BaseHeadView headFace;
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        inflater = changeTheme(inflater);
        if (rootView == null) {
            if (isNeedHead()) {
                headFace = new HeadWidget(getActivity(), getLayoutId());
                rootView = headFace.getContainerView();
            } else {
                rootView = inflater.inflate(getLayoutId(), container, false);
            }
            init(rootView);
        }
        return rootView;
    }

    protected void init(View view) {
    }


    // --------------------------------------------------
    public <T extends View> T findView(int resId) {
        return ViewUtil.findView(rootView, resId);
    }

    public <T extends View> T findView(View view, int resId) {
        return ViewUtil.findView(view, resId);
    }
    // --------------------------------------------------

    /**
     * 是否需要使用 通用的header
     */
    protected boolean isNeedHead() {
        return true;
    }

    /**
     * hearer 操作类
     *
     * @return HeadFace
     */
    public HeadFace getHeadWidget() {
        return headFace;
    }

    // --------------------------------------------------

    /**
     * 布局layout.xml
     *
     * @return int
     */
    protected abstract int getLayoutId();
}
