package com.zaze.demo.component.logcat.ui


import android.os.Bundle
import com.zaze.aarrepo.commons.base.ZBaseActivity
import com.zaze.demo.R
import com.zaze.demo.component.logcat.presenter.LogcatPresenter
import com.zaze.demo.component.logcat.presenter.impl.LogcatPresenterImpl
import com.zaze.demo.component.logcat.view.LogcatView

/**
 * Description :
 * @author : zaze
 * @version : 2017-07-05 10:03 1.0
 */
open class LogcatActivity : ZBaseActivity(), LogcatView {
    var presenter: LogcatPresenter? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logcat)
        presenter = LogcatPresenterImpl(this)


    }
}