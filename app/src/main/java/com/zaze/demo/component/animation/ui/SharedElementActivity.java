package com.zaze.demo.component.animation.ui;


import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeBounds;
import android.transition.Slide;
import android.view.Gravity;

import com.zaze.demo.R;
import com.zaze.demo.component.animation.presenter.SharedElementPresenter;
import com.zaze.demo.databinding.ActivitySharedElementBinding;
import com.zaze.demo.model.entity.AnimationEntity;


/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-11-20 03:46 1.0
 */
public class SharedElementActivity extends AppCompatActivity {
    private SharedElementPresenter presenter;
    public static final String EXTRA_ENTITY = "entity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        presenter = new SharedElementPresenterImpl(this);
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    private void setupWindowAnimations() {
        // We are not interested in defining a new Enter Transition. Instead we change default transition duration
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getEnterTransition().setDuration(getResources().getInteger(R.integer.anim_duration_long));
        }
    }

    private void setupLayout(AnimationEntity entity) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Slide slideTransition = new Slide(Gravity.LEFT);
            slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
//             Create fragment and define some of it transitions
            ElementOneFragment elementOneFragment = ElementOneFragment.newInstance(entity);
            elementOneFragment.setReenterTransition(slideTransition);
            elementOneFragment.setExitTransition(slideTransition);
            elementOneFragment.setSharedElementEnterTransition(new ChangeBounds());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.share_element_frame, elementOneFragment)
                    .commit();
        }

    }
}