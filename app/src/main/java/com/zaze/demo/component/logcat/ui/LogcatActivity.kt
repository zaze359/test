package com.zaze.demo.component.logcat.ui


import android.os.Bundle
import com.zaze.common.base.ZBaseActivity
import com.zaze.demo.R
import com.zaze.demo.component.logcat.presenter.LogcatPresenter
import com.zaze.demo.component.logcat.presenter.impl.LogcatPresenterImpl
import com.zaze.demo.component.logcat.view.LogcatView
import com.zaze.utils.ThreadManager
import com.zaze.utils.ZAppUtil
import com.zaze.utils.log.ZzLogCatUtil
import kotlinx.android.synthetic.main.activity_logcat.*

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
        logcat_start_catch.setOnClickListener {
            ThreadManager.getInstance().runInMultiThread {

                //                ZzLogCatUtil.startCatchLog("logcat ", "/sdcard/zaze/catch.log")
                ZzLogCatUtil.startCatchLog("logcat -v time process |grep ${ZAppUtil.getAppPid("com.xuehai.response_launcher_teacher")}", "/sdcard/zaze/cach.log", 1 shl 20)
            }
        }

        logcat_stop_catch.setOnClickListener {
            ZzLogCatUtil.stopCatchLog()
        }
    }
}