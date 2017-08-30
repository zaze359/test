package com.zaze.demo.component.animation.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.zaze.common.base.ZBaseActivity;
import com.zaze.demo.R;
import com.zaze.demo.component.animation.adapter.AnimationAdapter;
import com.zaze.demo.component.animation.presenter.AnimationPresenter;
import com.zaze.demo.component.animation.presenter.impl.AnimationPresenterImpl;
import com.zaze.demo.component.animation.view.AnimationView;
import com.zaze.demo.model.entity.AnimationEntity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-08-23 - 19:31
 */
public class AnimationActivity extends ZBaseActivity implements AnimationView {
    @Bind(R.id.animation_title)
    TextView animationTitle;
    @Bind(R.id.animation_back)
    ImageButton animationBack;
    @Bind(R.id.animation_recycler)
    RecyclerView animationRecycler;

    private AnimationAdapter adapter;
    private AnimationPresenter presenter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        ButterKnife.bind(this);
        presenter = new AnimationPresenterImpl(this);
        presenter.getAnimationList();
    }

    @Override
    public void showAnimationList(List<AnimationEntity> list) {
        if (adapter == null) {
            adapter = new AnimationAdapter(this, list);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            animationRecycler.setLayoutManager(linearLayoutManager);
            animationRecycler.setAdapter(adapter);
        } else {
            adapter.setDataList(list);
            adapter.notifyDataSetChanged();
        }
    }


    @OnClick(R.id.animation_back)
    public void back() {
        finish();
    }

}
