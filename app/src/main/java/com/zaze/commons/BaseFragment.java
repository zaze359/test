package com.zaze.commons;

import android.content.ContextWrapper;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zaze.R;

/**
 * Created by zaze on 16/4/27.
 */
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

    protected abstract void init(View view);
    protected abstract int getLayoutResource();
    protected int getTheme() {
        return R.style.BlackTheme;
    }
}
