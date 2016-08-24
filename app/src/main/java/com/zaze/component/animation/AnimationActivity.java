package com.zaze.component.animation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.zaze.R;
import com.zaze.component.animation.view.AnimationView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2016-08-23 - 19:31
 */
public class AnimationActivity extends AppCompatActivity implements AnimationView {
    @Bind(R.id.animation_title)
    TextView animationTitle;
    @Bind(R.id.animation_back)
    ImageButton animationBack;
    @Bind(R.id.animation_recycler)
    RecyclerView animationRecycler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        ButterKnife.bind(this);


    }

    @OnClick(R.id.animation_back)
    public void back() {
        finish();
    }
}
