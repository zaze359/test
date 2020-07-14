package com.zaze.demo.component.logcat.ui


import android.os.Bundle
import com.zaze.common.base.BaseActivity
import com.zaze.demo.R
import com.zaze.demo.component.logcat.presenter.LogcatPresenter
import com.zaze.demo.component.logcat.presenter.impl.LogcatPresenterImpl
import com.zaze.demo.component.logcat.view.LogcatView
import com.zaze.utils.ThreadManager
import com.zaze.utils.AppUtil
import com.zaze.utils.log.LogcatUtil
import kotlinx.android.synthetic.main.logcat_activity.*

/**
 * Description :
 * @author : zaze
 * @version : 2017-07-05 10:03 1.0
 */
open class LogcatActivity : BaseActivity(), LogcatView {
    var presenter: LogcatPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.logcat_activity)
        presenter = LogcatPresenterImpl(this)
        logcat_start_catch.setOnClickListener {
            ThreadManager.getInstance().runInMultiThread {
                LogcatUtil.startCatchLog("logcat -v time process |grep ${AppUtil.getAppPid("com.zaze.demo")}",
                        "/sdcard/zaze/cach.log", 1L shl 20)
            }
        }

        logcat_stop_catch.setOnClickListener {
            LogcatUtil.stopCatchLog()
        }
    }
}