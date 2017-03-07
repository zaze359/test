package com.zaze.aarrepo.commons.base;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zaze.aarrepo.commons.widget.head.BaseHeadView;
import com.zaze.aarrepo.commons.widget.head.HeadFace;
import com.zaze.aarrepo.commons.widget.head.HeadWidget;
import com.zaze.aarrepo.commons.widget.LoadingWidget;
import com.zaze.aarrepo.utils.ActivityUtil;
import com.zaze.aarrepo.utils.TipUtil;
import com.zaze.aarrepo.utils.ViewUtil;

import java.lang.reflect.Field;


/**
 * Description :
 *
 * @author : zaze
 * @version : 2015-09-22 - 19:38
 */
public abstract class ZBaseFragment extends Fragment implements ZBaseView {
    private BaseHeadView headFace;
    private boolean loadViewFinish = false;
    private LoadingWidget loadProgress;
    private View rootView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadViewFinish = false;
        loadProgress = new LoadingWidget(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        inflater = changeThem(inflater);
        if (isNeedHead()) {
            headFace = new HeadWidget(getActivity(), getLayoutId());
            rootView = headFace.getContainerView();
        } else {
            rootView = inflater.inflate(getLayoutId(), null);
        }
        init(rootView);
        return rootView;
    }

    @Deprecated
    protected void init(View view) {
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadViewFinish = true;
    }

    @Override
    public void onDestroyView() {
        loadViewFinish = false;
        super.onDestroyView();
    }

    /**
     * Description : 解决fragment 嵌套fragment 时产生的Bug : 报错 no activity
     * author : zaze
     * version : 2015年1月5日 上午10:23:26 (non-Javadoc)
     *
     * @see android.support.v4.app.Fragment#onDetach()
     */
    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示一个普通等待弹窗
     */
    @Override
    public void showProgress() {
        loadProgress.showProgress();
    }

    @Override
    public void showProgress(String msg) {
        loadProgress.setText(msg);
        loadProgress.showProgress();
    }

    @Override
    public void showProgress(String msg, View.OnClickListener onClickListener) {
        showProgress(msg);
        loadProgress.setTextOnClick(onClickListener);
    }

    @Override
    public void hideProgress() {
        loadProgress.dismissProgress();
    }

    @Override
    public void showToast(int msg) {
        TipUtil.toast(getString(msg));
    }

    @Override
    public void showToast(String msg) {
        TipUtil.toast(msg);
    }

    @Override
    public void close() {
        ActivityUtil.finish(getActivity());
    }

    @Override
    public void jumpToOtherUI(Intent intent, Class<?> cls) {
        ActivityUtil.startActivity(this, cls, intent);
    }

    @Override
    public void jumpToOtherUI(Class<?> cls) {
        ActivityUtil.startActivity(this, cls);
    }

    @Override
    public void jumpToOtherUI(Class<?> cls, int code) {
        ActivityUtil.startActivityForResult(this, cls, code);
    }

    @Override
    public void jumpToOtherUI(Intent intent, Class<?> cls, int code) {
        ActivityUtil.startActivityForResult(this, cls, intent, code);
    }

    @Override
    public void toFinish() {
        getActivity().finish();
    }

    @Override
    public void toFinish(Intent intent, int code) {
        getActivity().setResult(code, intent);
        getActivity().finish();
    }

    // --------------------

    /**
     * 更换theme
     *
     * @param inflater
     */
    protected LayoutInflater changeThem(LayoutInflater inflater) {
        return inflater;
    }

    @Override
    public int getColor(int colorRes) {
        return getResources().getColor(colorRes);
    }

    @Override
    public int getDimen(int id) {
        return getResources().getDimensionPixelSize(id);
    }

    public <T extends View> T findView(int resId) {
        return ViewUtil.findView(rootView, resId);
    }

    public <T extends View> T findView(View view, int resId) {
        return ViewUtil.findView(view, resId);
    }
    // --------------------

    /**
     * @return 布局layout.xml
     */
    protected abstract int getLayoutId();

    // --------------------

    /**
     * 是否需要使用 通用的header
     */
    protected boolean isNeedHead() {
        return true;
    }

    // -------------------

    /**
     * @return 返回hearer 操作类
     */
    public HeadFace getHeadWidget() {
        return headFace;
    }

    // --------------------
    private NotifyActivity notifyActivity;

    public interface NotifyActivity {
        void fromFragment(int arg, Object object);
    }

    public void setNotifyActivity(NotifyActivity notifyActivity) {
        this.notifyActivity = notifyActivity;
    }

    protected void onNotify(int arg, Object object) {
        if (notifyActivity != null) {
            notifyActivity.fromFragment(arg, object);
        }
    }

    public void notifyByActivity() {
    }

}
