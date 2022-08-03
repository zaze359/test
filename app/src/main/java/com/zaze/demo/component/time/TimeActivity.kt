package com.zaze.demo.component.time

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.databinding.DataBindingUtil
import com.zaze.common.base.BaseActivity
import com.zaze.demo.R
import com.zaze.demo.databinding.TimeActivityBinding
import com.zaze.utils.date.DateUtil
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
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
    private lateinit var binding: TimeActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView<TimeActivityBinding>(this, R.layout.time_activity)
        binding.timeExecuteBtn.setOnClickListener {
            showMessage()
        }
        binding.timeOutTv.movementMethod = ScrollingMovementMethod()
        Observable.interval(1, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Long> {
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onNext(t: Long) {
                    current = DateUtil.getDayStart(System.currentTimeMillis()) + DateUtil.MINUTE
                    binding.timeExecuteBtn.text = "当前时间戳 : $current"
                }

                override fun onError(e: Throwable) {
                }
            })
    }


    fun showMessage() {
        val dataTimeZone = TimeZone.getTimeZone("Europe/Helsinki")
        val localTimeZone = TimeZone.getDefault()
        binding.timeOutTv.append(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n")
        binding.timeOutTv.append(
            "transTimeZoneFrom8 : ${
                DateUtil.timeMillisToString(
                    transTimeZoneFrom8(current),
                    "yyyy-MM-dd HH:mm:ss",
                    localTimeZone
                )
            }\n"
        )
        binding.timeOutTv.append(
            "transTimeZoneTo8 : ${
                DateUtil.timeMillisToString(
                    transTimeZoneTo8(current),
                    "yyyy-MM-dd HH:mm:ss",
                    localTimeZone
                )
            }\n"
        )
        binding.timeOutTv.append(">>>>>>>>>>>>>>>>>>>>>>>>>设置数据处理>>>>>>>>>>>>>>>>>>>>>>>>>>>\n")
        binding.timeOutTv.append("时间戳 : $current\n")
        binding.timeOutTv.append("本地时区 : ${localTimeZone.displayName}-${localTimeZone.id}(GMT+${localTimeZone.rawOffset / DateUtil.HOUR})\n")
        binding.timeOutTv.append(
            "本地时间 : ${
                DateUtil.timeMillisToString(
                    current,
                    "yyyy-MM-dd HH:mm:ss"
                )
            }\n"
        )
        binding.timeOutTv.append("----------------------------------------------------\n")
        binding.timeOutTv.append("目标时区 : ${dataTimeZone.displayName}-${dataTimeZone.id}(GMT+${dataTimeZone.rawOffset / DateUtil.HOUR})\n")
        binding.timeOutTv.append(
            "直接转换时间 : ${
                DateUtil.timeMillisToString(
                    current,
                    "yyyy-MM-dd HH:mm:ss",
                    dataTimeZone
                )
            }\n"
        )
        binding.timeOutTv.append(">>>>>>>>>>>>>>>>>>>>>>>>>数据处理>>>>>>>>>>>>>>>>>>>>>>>>>>>\n")
        binding.timeOutTv.append("dstSavings : ${localTimeZone.dstSavings}/${dataTimeZone.dstSavings}\n")
        binding.timeOutTv.append("rawOffset : ${localTimeZone.rawOffset}/${dataTimeZone.rawOffset}\n")

        var dealRes = current + localTimeZone.rawOffset - dataTimeZone.rawOffset
        binding.timeOutTv.append(
            "去除时区影响 : ${
                DateUtil.timeMillisToString(
                    dealRes,
                    "yyyy-MM-dd HH:mm:ss",
                    dataTimeZone
                )
            }\n"
        )
        dealRes =
            current + localTimeZone.dstSavings + localTimeZone.rawOffset - dataTimeZone.rawOffset - dataTimeZone.dstSavings
        binding.timeOutTv.append(
            "去除夏令时影响 : ${
                DateUtil.timeMillisToString(
                    dealRes,
                    "yyyy-MM-dd HH:mm:ss",
                    dataTimeZone
                )
            }\n"
        )
        binding.timeOutTv.append(
            "直接转换显示 : ${
                DateUtil.timeMillisToString(
                    dealRes,
                    "yyyy-MM-dd HH:mm:ss"
                )
            }\n"
        )
        binding.timeOutTv.append(
            "转换到东八区 : ${
                DateUtil.timeMillisToString(
                    dealRes,
                    "yyyy-MM-dd HH:mm:ss",
                    dataTimeZone
                )
            }\n"
        )
        binding.timeOutTv.append(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n")
    }


    /**
     * 时区补偿，东8转本地
     *
     * @param timeMillis 东8区时间
     * @return 当前时区时间
     */
    fun transTimeZoneFrom8(timeMillis: Long): Long {
        return timeMillis - TimeZone.getDefault().dstSavings - TimeZone.getDefault().rawOffset + TimeZone.getTimeZone(
            "GMT+08:00"
        ).rawOffset
//        return timeMillis - TimeZone.getDefault().getOffset(System.currentTimeMillis()) + TimeZone.getTimeZone("GMT+08:00").getRawOffset();
    }

    /**
     * 时区补偿，本地时间转东8
     *
     * @param timeMillis 本地时间
     * @return 东8区时间
     */
    fun transTimeZoneTo8(timeMillis: Long): Long {
        return timeMillis + TimeZone.getDefault().dstSavings + TimeZone.getDefault().rawOffset - TimeZone.getTimeZone(
            "GMT+08:00"
        ).rawOffset
//        return timeMillis + TimeZone.getDefault().getOffset(System.currentTimeMillis()) - TimeZone.getTimeZone("GMT+08:00").getRawOffset();
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}
