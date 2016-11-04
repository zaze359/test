package com.zaze.component.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zaze.R;
import com.zz.library.commons.base.BaseFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-10-29 - 02:47
 */
/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-10-29 - 02:47
 */
public class TempFragment extends BaseFragment {

    @Bind(R.id.temp_tv)
    TextView tempTv;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_temp;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        tempTv.setText(getArguments().getString("title"));
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}