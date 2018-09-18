package com.zaze.demo.component.read.presenter

import com.zaze.common.base.mvp.BaseMvpPresenter
import com.zaze.demo.component.read.contract.LightReadingContract

/**
 * Description :
 * @author : zaze
 * @version : 2018-09-09 09:02 1.0
 */
class LightReadingPresenter(view: LightReadingContract.View) : BaseMvpPresenter<LightReadingContract.View>(view), LightReadingContract.Presenter {

}
