package com.zaze.demo.component.logcat.presenter.impl

import com.zaze.common.base.mvp.BaseMvpPresenter
import com.zaze.demo.component.logcat.presenter.LogcatPresenter
import com.zaze.demo.component.logcat.view.LogcatView

/**
 * Description :
 * @author : zaze
 * @version : 2017-07-05 10:03 1.0
 */
open class LogcatPresenterImpl(view: LogcatView) : BaseMvpPresenter<LogcatView>(view), LogcatPresenter {

}
