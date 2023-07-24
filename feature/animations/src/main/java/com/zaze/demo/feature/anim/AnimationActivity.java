package com.zaze.demo.feature.anim;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Toast;

import com.zaze.common.base.AbsActivity;
import com.zaze.common.base.mvp.BaseMvpActivity;
import com.zaze.core.model.data.AnimationEntity;
import com.zaze.demo.feature.anim.R;
import com.zaze.demo.feature.anim.adapter.AnimationAdapter;
import com.zaze.demo.feature.anim.databinding.AnimationActivityBinding;
import com.zaze.demo.feature.anim.presenter.AnimationPresenter;
import com.zaze.demo.feature.anim.presenter.impl.AnimationPresenterImpl;
import com.zaze.demo.feature.anim.view.AnimationView;
import com.zaze.utils.ToastUtil;
import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-08-23 - 19:31
 */
public class AnimationActivity extends AbsActivity {
    private AnimationAdapter adapter;
    private AnimationActivityBinding binding;

    private int progress = 0;

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            progress++;
            binding.animationObjectBar.setMax(1_000);
            ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 100);
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.setDuration(200);
            valueAnimator.addUpdateListener(animation -> {
                int value = (int) animation.getAnimatedValue();
                binding.animationObjectBar.setProgress(value);
            });

            ObjectAnimator animator = ObjectAnimator.ofInt(binding.animationObjectBar, "progress", progress);
            animator.setDuration(200);
            animator.setInterpolator(new LinearInterpolator());
            animator.start();
            // 循环动画
            binding.animationObjectBar.postDelayed(runnable, 100L);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AnimationActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupWindowAnimations();
        setupToolbar();
        binding.animationStartBtn.setOnClickListener(v -> {
            // 执行补间动画
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.test_anim);
            binding.animationViewBtn.startAnimation(animation);
//            TranslateAnimation translateAnimation = new TranslateAnimation(0, 100, 0, 100);
//            translateAnimation.setInterpolator(new LinearInterpolator());
//            translateAnimation.setDuration(1000);
//            binding.animationTestBtn.startAnimation(translateAnimation);

            // 执行帧动画
            ((AnimationDrawable) binding.animationFrameIv.getDrawable()).start();
            //
            binding.animationObjectBar.post(runnable);
            binding.animationObjectBar.post(runnable);
            ObjectAnimator animator = ObjectAnimator.ofFloat(binding.animationObjectBar, "translationY", 100);
            animator.setDuration(200);
            animator.setInterpolator(new LinearInterpolator());
            animator.start();
        });
        binding.animationStopBtn.setOnClickListener(v -> {
            // 执行补间动画
            binding.animationViewBtn.clearAnimation();
            // 执行帧动画
            ((AnimationDrawable) binding.animationFrameIv.getDrawable()).stop();
            binding.animationObjectBar.removeCallbacks(runnable);
            progress = 0;
        });

        binding.animationViewBtn.setOnClickListener(v -> {
            ToastUtil.toast(this, "补间动画");
        });

        binding.animationFrameIv.setOnClickListener(v -> {
            ToastUtil.toast(this, "帧动画");
        });
        binding.animationObjectBar.setOnClickListener(v -> {
            ToastUtil.toast(this, "属性动画");
        });
        binding.animationObjectBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ZLog.i(ZTag.TAG_DEBUG, "onTouch: " + event);
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * 添加 window 动画
     */
    private void setupWindowAnimations() {
        Slide slideTransition = new Slide();
        slideTransition.setSlideEdge(Gravity.LEFT);
        slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
        // 盖在上层activity的退出时
        getWindow().setReenterTransition(slideTransition);
        // 打开一个新activity时
        getWindow().setExitTransition(slideTransition);
    }

    private void setupToolbar() {
        setSupportActionBar(binding.animationToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }

    public void showAnimationList(List<AnimationEntity> list) {
        if (adapter == null) {
            adapter = new AnimationAdapter(this, list);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            binding.animationRecycler.setLayoutManager(linearLayoutManager);
            binding.animationRecycler.setAdapter(adapter);
        } else {
            adapter.setDataList(list);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding.animationObjectBar.removeCallbacks(runnable);
    }
}