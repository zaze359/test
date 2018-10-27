package com.zaze.demo.component.read.ui;

import android.os.Bundle
import com.zaze.common.base.mvp.BaseMvpActivity
import com.zaze.demo.R
import com.zaze.demo.component.read.contract.LightReadingContract
import com.zaze.demo.component.read.presenter.LightReadingPresenter

/**
 * Description :
 * @author : zaze
 * @version : 2018-09-09 09:02 1.0
 */
open class LightReadingActivity : BaseMvpActivity<LightReadingContract.View, LightReadingContract.Presenter>(), LightReadingContract.View {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_light_reading)
    }

    override fun getPresenter(): LightReadingContract.Presenter {
        return LightReadingPresenter (this)
    }

}