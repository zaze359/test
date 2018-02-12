package com.zaze.demo.component.font.ui;


import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

import com.zaze.common.base.BaseActivity;
import com.zaze.demo.R;
import com.zaze.demo.component.font.presenter.FontPresenter;
import com.zaze.demo.component.font.presenter.impl.FontPresenterImpl;
import com.zaze.demo.component.font.view.FontView;

import java.lang.reflect.Field;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Description :
 *
 * @author : zaze
 * @version : 2018-02-07 03:11 1.0
 */
public class FontActivity extends BaseActivity implements FontView {
    @Bind(R.id.font_roboto_light_tv)
    TextView fontRobotoLightTv;
    @Bind(R.id.font_test_tv)
    TextView fontTestTv;
    @Bind(R.id.font_roboto_regular_tv)
    TextView fontRobotoRegularTv;

    private FontPresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_font);
        ButterKnife.bind(this);
        presenter = new FontPresenterImpl(this);
        try {
            Field staticField = Typeface.class.getDeclaredField("DEFAULT");
        } catch (Exception e) {
            e.printStackTrace();
        }

        fontTestTv.setTypeface(Typeface.createFromFile("/system/fonts/Roboto-Medium.ttf"));
        fontRobotoRegularTv.setTypeface(Typeface.createFromFile("/system/fonts/Roboto-Regular.ttf"));
//        fontTestTv.setTypeface(Typeface.createFromFile("/system/fonts/Roboto-Blod.ttf"));
        fontRobotoLightTv.setTypeface(Typeface.createFromFile("/system/fonts/Roboto-Light.ttf"));
    }

}