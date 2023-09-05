package com.zaze.demo.component.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zaze.common.base.AbsFragment;
import com.zaze.common.base.BaseFragment;
import com.zaze.demo.R;

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
public class TempFragment extends AbsFragment {

    public static TempFragment newInstance(String title) {
        Bundle args = new Bundle();
        args.putString("title", title);
        TempFragment fragment = new TempFragment();
        fragment.setArguments(args);
        return fragment;
    }

    TextView tempTv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.temp_fragment, container, false);
        tempTv = rootView.findViewById(R.id.temp_tv);
        tempTv.setText(getArguments().getString("title"));
        return rootView;
    }

}