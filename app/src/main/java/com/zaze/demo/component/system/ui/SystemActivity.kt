package com.zaze.demo.component.system.ui


import android.app.KeyguardManager
import android.content.Context
import android.os.Bundle
import android.os.PowerManager
import com.zaze.common.base.ZBaseActivity
import com.zaze.demo.R
import com.zaze.demo.component.system.presenter.SystemPresenter
import com.zaze.demo.component.system.presenter.impl.SystemPresenterImpl
import com.zaze.demo.component.system.view.SystemView

/**
 * Description :
 * @author : zaze
 * @version : 2017-11-08 09:37 1.0
 */
open class SystemActivity : ZBaseActivity(), SystemView {
    var presenter: SystemPresenter? = null;
    // 键盘管理器
    private var mKeyguardManager: KeyguardManager? = null
    // 电源管理器
    private var mPowerManager: PowerManager? = null
    // 键盘锁
    private var mKeyguardLock: KeyguardManager.KeyguardLock? = null
    // 唤醒锁
    private var mWakeLock: PowerManager.WakeLock? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_system)
        presenter = SystemPresenterImpl(this)
        mKeyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        mPowerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        // 点亮亮屏
        mWakeLock = mPowerManager?.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP or PowerManager.SCREEN_DIM_WAKE_LOCK, "Tag")
        mWakeLock?.acquire()
        // 初始化键盘锁
        mKeyguardLock = mKeyguardManager?.newKeyguardLock("")
        // 键盘解锁
        mKeyguardLock?.disableKeyguard()
    }


    //一定要释放唤醒锁和恢复键盘
    override fun onDestroy() {
        super.onDestroy()
        if (mWakeLock != null) {
            println("----> 终止服务,释放唤醒锁")
            mWakeLock?.release()
            mWakeLock = null
        }
        if (mKeyguardLock != null) {
            println("----> 终止服务,重新锁键盘")
            mKeyguardLock?.reenableKeyguard()
        }
    }

}