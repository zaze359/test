package com.zaze.demo.component.system.presenter.impl

import com.zaze.demo.component.system.presenter.SystemPresenter
import com.zaze.common.base.ZBasePresenter
import com.zaze.demo.component.system.view.SystemView

/**
 * Description :
 * @author : zaze
 * @version : 2017-11-08 09:37 1.0
 */
open class SystemPresenterImpl(view: SystemView) : ZBasePresenter<SystemView>(view), SystemPresenter {

}
