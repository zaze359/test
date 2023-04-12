package com.zaze.demo.component.animation.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zaze.core.model.data.AnimationEntity;
import com.zaze.demo.R;

import androidx.fragment.app.Fragment;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-11-24 - 15:55
 */
public class ElementOneFragment extends Fragment {
    private static final String EXTRA_SAMPLE = "element";

    public static ElementOneFragment newInstance(AnimationEntity entity) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_SAMPLE, entity);
        ElementOneFragment fragment = new ElementOneFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.element_one_fragment, container, false);
        return view;
    }
}
