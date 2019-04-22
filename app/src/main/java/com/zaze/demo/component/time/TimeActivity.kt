package com.zaze.demo.component.time

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import com.zaze.common.base.BaseActivity
import com.zaze.demo.R
import com.zaze.utils.date.DateUtil
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.time_activity.*
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Description : 时间转换之类
 *
 * @author : ZAZE
 * @version : 2016-12-22 - 11:10
 */
class TimeActivity : BaseActivity() {
    val compositeDisposable = CompositeDisposable()

    var current = System.currentTimeMillis()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.time_activity)
        timeExecuteBtn.setOnClickListener {
            showMessage()
        }
        timeOutTv.movementMethod = ScrollingMovementMethod()
        Observable.interval(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Long> {
                    override fun onComplete() {
                    }

                    override fun onSubscribe(d: Disposable) {
                        compositeDisposable.add(d)
                    }

                    override fun onNext(t: Long) {
                        current = System.currentTimeMillis()
                        timeExecuteBtn.text = "当前时间戳 : $current"
                    }

                    override fun onError(e: Throwable) {
                    }
                })
    }


    fun showMessage() {
        val dataTimeZone = TimeZone.getTimeZone("Asia/Shanghai")
        val localTimeZone = TimeZone.getDefault()
        timeOutTv.append(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n")
        timeOutTv.append(">>>>>>>>>>>>>>>>>>>>>>>>>设置数据处理>>>>>>>>>>>>>>>>>>>>>>>>>>>\n")
        timeOutTv.append("时间戳 : $current\n")
        timeOutTv.append("本地时区 : ${localTimeZone.displayName}-${localTimeZone.id}(GMT+${localTimeZone.rawOffset / DateUtil.HOUR})\n")
        timeOutTv.append("本地时间 : ${DateUtil.timeMillisToString(current, "yyyy-MM-dd HH:mm:ss")}\n")
        timeOutTv.append("----------------------------------------------------\n")
        timeOutTv.append("目标时区 : ${dataTimeZone.displayName}-${dataTimeZone.id}(GMT+${dataTimeZone.rawOffset / DateUtil.HOUR})\n")
        timeOutTv.append("直接转换时间 : ${DateUtil.timeMillisToString(current, "yyyy-MM-dd HH:mm:ss", dataTimeZone)}\n")
        val uploadTime = current + localTimeZone.dstSavings + localTimeZone.rawOffset - dataTimeZone.rawOffset
        timeOutTv.append("去除时区影响(东8) : ${DateUtil.timeMillisToString(uploadTime, "yyyy-MM-dd HH:mm:ss", dataTimeZone)}\n")
        timeOutTv.append(">>>>>>>>>>>>>>>>>>>>>>>>>获取数据处理>>>>>>>>>>>>>>>>>>>>>>>>>>>\n")
        timeOutTv.append("直接转换显示 : ${DateUtil.timeMillisToString(uploadTime, "yyyy-MM-dd HH:mm:ss")}\n")
        timeOutTv.append("转换到东八区 : ${DateUtil.timeMillisToString(uploadTime, "yyyy-MM-dd HH:mm:ss", dataTimeZone)}\n")
        timeOutTv.append(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n")
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}
