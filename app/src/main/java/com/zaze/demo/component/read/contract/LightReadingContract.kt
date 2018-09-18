package com.zaze.demo.component.read.contract

import com.zaze.common.base.BasePresenter
import com.zaze.common.base.BaseView

/**
 * Description :
 * @author : zaze
 * @version : 2018-09-09 09:02 1.0
 */
interface LightReadingContract {

    interface View : BaseView {

    }

    interface Presenter : BasePresenter<View> {

    }

}
