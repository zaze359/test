package com.zz.library.commons.base;

import android.content.ContextWrapper;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zz.library.R;

import java.lang.reflect.Field;


public abstract class BaseFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ContextWrapper contextWrapper = new ContextThemeWrapper(getActivity(), getTheme());
        LayoutInflater localInflater = inflater.cloneInContext(contextWrapper);
        View view = localInflater.inflate(getLayoutResource(), container, false);
        init(view);
        return view;
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

    protected void init(View view){}

    protected abstract int getLayoutResource();

    protected int getTheme() {
        return R.style.BlackTheme;
    }


}
