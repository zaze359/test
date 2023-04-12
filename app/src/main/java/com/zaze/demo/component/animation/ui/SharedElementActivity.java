package com.zaze.demo.component.animation.ui;


import android.os.Build;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;

import com.zaze.core.model.data.AnimationEntity;
import com.zaze.demo.R;
import com.zaze.demo.component.animation.presenter.SharedElementPresenter;
import com.zaze.demo.databinding.SharedElementActivityBinding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;


/**
 * Description :
 *
 * @author : zaze
 * @version : 2017-11-20 03:46 1.0
 */
public class SharedElementActivity extends AppCompatActivity {
    private SharedElementPresenter presenter;
    public static final String EXTRA_ENTITY = "entity";
    AnimationEntity animationEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        presenter = new SharedElementPresenterImpl(this);
        animationEntity = (AnimationEntity) getIntent().getExtras().getSerializable(EXTRA_ENTITY);
        bindData();
        setupWindowAnimations();
        setupLayout();
        setupToolbar();
        findViewById(R.id.share_element_test_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationEntity.setName(animationEntity.getName() + "updated");
            }
        });
    }

    private void bindData() {
        SharedElementActivityBinding binding = DataBindingUtil.setContentView(this, R.layout.shared_element_activity);
        binding.setSharedSample(animationEntity);
    }

    void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
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

    private void setupLayout() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Slide slideTransition = new Slide(Gravity.LEFT);
            slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
//             Create fragment and define some of it transitions
            ElementOneFragment elementOneFragment = ElementOneFragment.newInstance(animationEntity);
            elementOneFragment.setReenterTransition(slideTransition);
            elementOneFragment.setExitTransition(slideTransition);
            elementOneFragment.setSharedElementEnterTransition(new ChangeBounds());
//            addToBackStack()
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.share_element_frame, elementOneFragment)
                    .commit();
        }

    }
}