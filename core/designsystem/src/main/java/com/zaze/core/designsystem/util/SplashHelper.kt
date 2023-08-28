package com.zaze.core.designsystem.util

import android.animation.ObjectAnimator
import android.app.Activity
import android.os.Build
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.AnticipateInterpolator
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.splashscreen.SplashScreenViewProvider

object SplashHelper {
    fun init(activity: Activity, listener: SplashScreen.OnExitAnimationListener? = null) {
        val splashScreen = activity.installSplashScreen()
        setupExitAnimator(splashScreen, listener)
    }

    private fun setupExitAnimator(
        splashScreen: SplashScreen,
        listener: SplashScreen.OnExitAnimationListener?
    ) {
        splashScreen.setOnExitAnimationListener { provider ->
            splashIconExitAnimator(provider)
            splashExitAnimator(provider)
            listener?.onSplashScreenExit(provider)
        }
    }

    private fun splashExitAnimator(provider: SplashScreenViewProvider) {
        Log.d("SplashHelper", "splashExitAnimator start")
        val slideDown = ObjectAnimator.ofFloat(
            provider.view,
            View.TRANSLATION_Y,
            0f,
            provider.view.height.toFloat()
        )
        slideDown.doOnEnd {
            Log.d("SplashHelper", "splashExitAnimator doOnEnd")
            provider.remove()
        }
//        slideDown.interpolator = AnticipateInterpolator()
//        slideDown.duration = getIconAnimationRemainingDuration(provider) + 100
        slideDown.start()
    }

    private fun splashIconExitAnimator(provider: SplashScreenViewProvider) {
        Log.d("SplashHelper", "splashIconExitAnimator start")
        //        val path = Path()
//        path.moveTo(1.0f, 1.0f)
//        path.lineTo(0f, 0f)
//        val scaleOut =
//            ObjectAnimator.ofFloat(provider.view, View.SCALE_X, View.SCALE_Y, path)
        val slideUp = ObjectAnimator.ofFloat(
            provider.iconView,
            View.TRANSLATION_Y,
            0f,
            -provider.iconView.height * 2.toFloat()
        )
        slideUp.duration = 300L
        slideUp.start()
    }

    private fun getIconAnimationRemainingDuration(provider: SplashScreenViewProvider): Long {
        // 剩余时间 = 开始时间 + 持续时间 - 当前时间
        Log.d("SplashHelper", "SDK_INT : ${Build.VERSION.SDK_INT}")
        // 一直获取到0？
        Log.d(
            "SplashHelper",
            "rovider.iconAnimationStartMillis : ${provider.iconAnimationStartMillis}"
        )
        Log.d(
            "SplashHelper",
            "rovider.iconAnimationDurationMillis : ${provider.iconAnimationDurationMillis}"
        )
        Log.d("SplashHelper", "rovider.uptimeMillis : ${SystemClock.uptimeMillis()}")
        return (provider.iconAnimationStartMillis + provider.iconAnimationDurationMillis - SystemClock.uptimeMillis()).coerceAtLeast(
            0L
        )
    }


    fun wait(activity: Activity, isReady: () -> Boolean) {
        activity.findViewById<View>(android.R.id.content).let {
            it.viewTreeObserver.addOnPreDrawListener(
                object : ViewTreeObserver.OnPreDrawListener {
                    override fun onPreDraw(): Boolean {
                        // 每隔一帧回调依次，直到 返回 true。
                        return if (isReady()) {
                            it.viewTreeObserver.removeOnPreDrawListener(this)
                            true
                        } else {
                            false
                        }
                    }
                }
            )
        }
    }
}