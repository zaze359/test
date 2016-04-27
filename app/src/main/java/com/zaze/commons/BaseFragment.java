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
public class BaseFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ContextWrapper contextWrapper = new ContextThemeWrapper(getActivity(), getTheme());
        inflater = inflater.cloneInContext(contextWrapper);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    protected int getTheme() {
        return R.style.BlackTheme;
    }
}
