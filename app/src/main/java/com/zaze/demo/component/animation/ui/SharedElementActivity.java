package com.zaze.demo.component.animation.ui;


import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.Gravity;

import com.zaze.common.base.ZBaseActivity;
import com.zaze.demo.R;
import com.zaze.demo.component.animation.presenter.SharedElementPresenter;
import com.zaze.demo.component.animation.presenter.impl.SharedElementPresenterImpl;
import com.zaze.demo.component.animation.view.SharedElementView;
import com.zaze.demo.databinding.ActivitySharedElementBinding;
import com.zaze.demo.model.entity.AnimationEntity;


/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-11-20 03:46 1.0
 */
public class SharedElementActivity extends ZBaseActivity implements SharedElementView {
    private SharedElementPresenter presenter;
    public static final String EXTRA_ENTITY = "entity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new SharedElementPresenterImpl(this);
        AnimationEntity animationEntity = (AnimationEntity) getIntent().getExtras().getSerializable(EXTRA_ENTITY);
        bindData(animationEntity);
        setupWindowAnimations();
        setupLayout(animationEntity);
        setupToolbar();
    }

    private void bindData(AnimationEntity sample) {
        ActivitySharedElementBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_shared_element);
        binding.setSharedSample(sample);
    }

    void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }


    private void setupWindowAnimations() {
        // We are not interested in defining a new Enter Transition. Instead we change default transition duration
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getEnterTransition().setDuration(getResources().getInteger(R.integer.anim_duration_long));
        }
    }

    private void setupLayout(AnimationEntity sample) {
        // Transition for fragment1
        Slide slideTransition = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            slideTransition = new Slide(Gravity.LEFT);
            slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
            // Create fragment and define some of it transitions
//            SharedElementFragment1 sharedElementFragment1 = SharedElementFragment1.newInstance(sample);
//            sharedElementFragment1.setReenterTransition(slideTransition);
//            sharedElementFragment1.setExitTransition(slideTransition);
//            sharedElementFragment1.setSharedElementEnterTransition(new ChangeBounds());
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.sample2_content, sharedElementFragment1)
//                    .commit();
        }

    }
}