package com.zaze.demo

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.*
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.zaze.common.base.BaseActivity
import com.zaze.common.base.BaseFragment
import com.zaze.common.widget.IntervalButtonWidget
import com.zaze.common.widget.head.ZOrientation
import com.zaze.demo.component.table.ui.TableFragment
import com.zaze.demo.debug.KotlinDebug
import com.zaze.demo.debug.MessengerService
import com.zaze.demo.debug.TestDebug
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import com.zaze.utils.permission.PermissionCode
import com.zaze.utils.permission.PermissionUtil
import kotlinx.android.synthetic.main.activity_main.*


/**
 * Description :
 * @author : ZAZE
 * @version : 2017-05-19 - 01:41
 */

class MainActivity : BaseActivity() {
    private val fragmentList = ArrayList<BaseFragment>()

    private var intervalButton: IntervalButtonWidget? = null


    private val messenger = Messenger(object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            ZLog.i(ZTag.TAG_DEBUG, "handleMessage")
        }
    })

    private var sendMessenger: Messenger? = null

    private val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            sendMessenger = null
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            sendMessenger = Messenger(service)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        headWidget.setText("zZz", ZOrientation.CENTER)
        // --------------------------------------------------
        intervalButton = IntervalButtonWidget(main_test_2_button, "测试2")
        main_test_2_button.setOnClickListener({
            intervalButton?.start()
        })
        main_test_button.text = "测试"
        // --------------------------------------------------
        fragmentList.clear()
        fragmentList.add(TableFragment.newInstance("0"))
        fragmentList.add(TableFragment.newInstance("1"))
        fragmentList.add(TableFragment.newInstance("2"))
        // --------------------------------------------------
        main_viewpager.adapter = MyPagerAdapter(supportFragmentManager, fragmentList)

        main_test_button.setOnClickListener {
            KotlinDebug.test(this)
            TestDebug.test(this)
            val msg = Message.obtain()
            msg.replyTo = messenger
            sendMessenger?.send(msg)
            // --------------------------------------------------
//            TestJni.newInstance().stringFromJNI()
            // --------------------------------------------------
        }
        setupPermission()
        bindService(Intent(this, MessengerService::class.java), serviceConnection, Context.BIND_AUTO_CREATE)
//        val obj = DeviceStatus()
//        weakReference = WeakReference(obj)
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(serviceConnection)
        intervalButton?.stop()
    }

    /**
     * 申请权限
     */
    private fun setupPermission() {
        PermissionUtil.checkAndRequestUserPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, PermissionCode.WRITE_EXTERNAL_STORAGE)
        //        PermissionUtil.checkAndRequestUserPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE, PermissionCode.READ_EXTERNAL_STORAGE);
        //        PermissionUtil.checkAndRequestUserPermission(this, Manifest.permission.CALL_PHONE, PermissionCode.REQUEST_PERMISSION_CALL_PHONE);
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val isGranted = grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
        if (!isGranted) {
            finish()
        }
    }

    // --------------------------------------------------
    inner class MyPagerAdapter(fm: FragmentManager, list: ArrayList<BaseFragment>?) : FragmentPagerAdapter(fm) {
        private val fragmentList = ArrayList<BaseFragment>()

        init {
            fragmentList.clear()
            if (list != null) {
                fragmentList.addAll(list)
            }
        }

        override fun getItem(position: Int): Fragment {
            return fragmentList[position]
        }

        override fun getCount(): Int {
            return fragmentList.size
        }
    }
}