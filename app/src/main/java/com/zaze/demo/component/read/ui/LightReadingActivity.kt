package com.zaze.demo.component.read.ui;

import com.zaze.demo.R;
import android.os.Bundle;
import com.zaze.demo.component.read.contract.LightReadingContract;
import com.zaze.demo.component.read.presenter.LightReadingPresenter;
import com.zaze.common.base.mvp.BaseMvpActivity;

import kotlinx.android.synthetic.main.activity_light_reading.*

/**
 * Description :
 * @author : zaze
 * @version : 2018-09-09 09:02 1.0
 */
open class LightReadingActivity : BaseMvpActivity<LightReadingContract.View, LightReadingContract.Presenter>(), LightReadingContract.View {

    override fun isNeedHead() :Boolean {
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_light_reading)
    }

    override fun getPresenter(): LightReadingContract.Presenter {
        return LightReadingPresenter (this)
    }

}