package com.zaze.demo.component.animation.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;

import com.zaze.common.base.mvp.BaseMvpActivity;
import com.zaze.demo.R;
import com.zaze.demo.component.animation.adapter.AnimationAdapter;
import com.zaze.demo.component.animation.presenter.AnimationPresenter;
import com.zaze.demo.component.animation.presenter.impl.AnimationPresenterImpl;
import com.zaze.demo.component.animation.view.AnimationView;
import com.zaze.demo.model.entity.AnimationEntity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-08-23 - 19:31
 */
public class AnimationActivity extends BaseMvpActivity<AnimationView, AnimationPresenter> implements AnimationView {
    // --------------------------------------------------

    @Bind(R.id.animation_toolbar)
    Toolbar animationToolbar;
    @Bind(R.id.animation_recycler)
    RecyclerView animationRecycler;
    private AnimationAdapter adapter;

    @Override
    protected boolean isNeedHead() {
        return false;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        ButterKnife.bind(this);
        setupWindowAnimations();
        setupToolbar();
        presenter.getAnimationList();
    }

    @Override
    protected AnimationPresenter getPresenter() {
        return new AnimationPresenterImpl();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void setupWindowAnimations() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Slide slideTransition = new Slide();
            slideTransition.setSlideEdge(Gravity.LEFT);
            slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
            // 盖在上层activity的退出时
            getWindow().setReenterTransition(slideTransition);
            // 打开一个新activity时
            getWindow().setExitTransition(slideTransition);
        }
    }

    private void setupToolbar() {
        setSupportActionBar(animationToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
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
}