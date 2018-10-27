package com.zaze.common.base;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zaze.common.util.ActivityUtil;
import com.zaze.utils.ZTipUtil;
import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;

import java.lang.reflect.Field;


/**
 * Description :
 *
 * @author : zaze
 * @version : 2015-09-22 - 19:38
 */
public abstract class BaseFragment extends Fragment implements BaseView {

    @Override
    public void onAttach(Context context) {
        ZLog.v(ZTag.TAG_DEBUG, "onAttach : " + this.getClass().getName());
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ZLog.v(ZTag.TAG_DEBUG, "onCreate : " + this.getClass().getName());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ZLog.v(ZTag.TAG_DEBUG, "onCreateView : " + this.getClass().getName());
        View view = super.onCreateView(inflater, container, savedInstanceState);
        changeTheme(inflater);
        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ZLog.v(ZTag.TAG_DEBUG, "onViewCreated : " + this.getClass().getName());
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        ZLog.v(ZTag.TAG_DEBUG, "onStart : " + this.getClass().getName());
        super.onStart();
    }

    @Override
    public void onResume() {
        ZLog.v(ZTag.TAG_DEBUG, "onResume : " + this.getClass().getName());
        super.onResume();
    }


    @Override
    public void onPause() {
        ZLog.v(ZTag.TAG_DEBUG, "onPause : " + this.getClass().getName());
        super.onPause();
    }

    @Override
    public void onStop() {
        ZLog.v(ZTag.TAG_DEBUG, "onStop : " + this.getClass().getName());
        super.onStop();
    }

    /**
     * Description : 解决fragment 嵌套fragment 时产生的Bug : 报错 no activity
     * author : zaze
     * version : 2015年1月5日 上午10:23:26 (non-Javadoc)
     *
     * @see Fragment#onDetach()
     */
    @Override
    public void onDetach() {
        ZLog.v(ZTag.TAG_DEBUG, "onDetach : " + this.getClass().getName());
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

    @Override
    public void onDestroyView() {
        ZLog.v(ZTag.TAG_DEBUG, "onDestroyView : " + this.getClass().getName());
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        ZLog.v(ZTag.TAG_DEBUG, "onDestroy : " + this.getClass().getName());
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        ZLog.v(ZTag.TAG_DEBUG, "onSaveInstanceState : " + this.getClass().getName());
        super.onSaveInstanceState(outState);
    }

    // --------------------------------------------------
    @Override
    public void showToast(int msg) {
        ZTipUtil.toast(getContext(), getString(msg));
    }

    @Override
    public void showToast(String msg) {
        ZTipUtil.toast(getContext(), msg);
    }

    @Override
    public void finishSelf() {
        ActivityUtil.finish(getActivity());
    }

    @Override
    public void finishSelf(Intent intent, int code) {
        getActivity().setResult(code, intent);
        getActivity().finish();
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

    // --------------------------------------------------

    /**
     * 更换theme
     *
     * @param inflater inflater
     */
    protected LayoutInflater changeTheme(LayoutInflater inflater) {
        return inflater;
    }

    // --------------------------------------------------
    @Override
    public int getColor(int colorRes) {
        return ContextCompat.getColor(getContext(), colorRes);
    }

    @Override
    public int getDimen(int id) {
        return getResources().getDimensionPixelSize(id);
    }

    @Override
    public String[] getStringArray(int res) {
        return getResources().getStringArray(res);
    }

    // --------------------------------------------------
}
