package com.zaze.demo.component.progress;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zaze.common.base.ZBaseFragment;
import com.zaze.demo.R;

import butterknife.ButterKnife;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-10-29 - 02:47
 */
public class ProgressFragment extends ZBaseFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_progress;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private static final String[] INDICATORS = new String[]{
            "BallPulseIndicator",
            "BallGridPulseIndicator",
            "BallClipRotateIndicator",
            "BallClipRotatePulseIndicator",
            "SquareSpinIndicator",
            "BallClipRotateMultipleIndicator",
            "BallPulseRiseIndicator",
            "BallRotateIndicator",
            "CubeTransitionIndicator",
            "BallZigZagIndicator",
            "BallZigZagDeflectIndicator",
            "BallTrianglePathIndicator",
            "BallScaleIndicator",
            "LineScaleIndicator",
            "LineScalePartyIndicator",
            "BallScaleMultipleIndicator",
            "BallPulseSyncIndicator",
            "BallBeatIndicator",
            "LineScalePulseOutIndicator",
            "LineScalePulseOutRapidIndicator",
            "BallScaleRippleIndicator",
            "BallScaleRippleMultipleIndicator",
            "BallSpinFadeLoaderIndicator",
            "LineSpinFadeLoaderIndicator",
            "TriangleSkewSpinIndicator",
            "PacmanIndicator",
            "BallGridBeatIndicator",
            "SemiCircleSpinIndicator",
            "com.wang.avi.sample.MyCustomIndicator"
    };
}
