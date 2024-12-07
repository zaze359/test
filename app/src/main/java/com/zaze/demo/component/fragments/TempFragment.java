package com.zaze.demo.component.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
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
    private BottomSheetDialog bottomSheetDialog;
    private BottomSheetBehavior mDialogBehavior;

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
        tempTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheet();
            }
        });
        return rootView;
    }


    private void bottomSheet() {
        if (bottomSheetDialog == null) {
            //创建布局
            View view = LayoutInflater.from(requireContext()).inflate(R.layout.activity_media, null, false);
            bottomSheetDialog = new BottomSheetDialog(requireContext(), R.style.Base_Theme_Material3_Dark_BottomSheetDialog);
            //设置点击dialog外部不消失
            bottomSheetDialog.setCanceledOnTouchOutside(false);
            //核心代码 解决了无法去除遮罩问题
            bottomSheetDialog.getWindow().setDimAmount(0f);
            //设置布局
            bottomSheetDialog.setContentView(view);
            //用户行为
            mDialogBehavior = BottomSheetBehavior.from((View) view.getParent());
            //dialog的高度
            mDialogBehavior.setPeekHeight(400);
        }
        //展示
        bottomSheetDialog.show();
        //重新用户的滑动状态
        mDialogBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int newState) {
                //监听BottomSheet状态的改变
//                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
//                    bottomSheetDialog.dismiss();
//                    mDialogBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {
                //监听拖拽中的回调，根据slideOffset可以做一些动画
            }
        });

    }
}