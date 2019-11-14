package com.zaze.demo

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.net.Uri
import android.os.*
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.zaze.common.base.AbsFragment
import com.zaze.common.base.BaseActivity
import com.zaze.common.base.ext.setImmersion
import com.zaze.common.base.ext.setupActionBar
import com.zaze.common.permission.PermissionUtil
import com.zaze.common.widget.IntervalButtonWidget
import com.zaze.demo.app.MyApplication
import com.zaze.demo.component.table.TableFragment
import com.zaze.demo.debug.KotlinDebug
import com.zaze.demo.debug.LogDirListener
import com.zaze.demo.debug.MessengerService
import com.zaze.demo.debug.TestDebug
import com.zaze.demo.debug.wifi.WifiCompat
import com.zaze.utils.FileUtil
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import kotlinx.android.synthetic.main.activity_main.*


/**
 * Description :
 * @author : ZAZE
 * @version : 2017-05-19 - 01:41
 */

class MainActivity : BaseActivity() {

    private lateinit var drawerToggle: ActionBarDrawerToggle

    private val fragmentList = ArrayList<AbsFragment>()

    private var intervalButton: IntervalButtonWidget? = null
    private val dirListener = LogDirListener(FileUtil.getSDCardRoot() + "/xuehai")

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
        setImmersion()
        setupActionBar(R.id.main_toolbar) {
            setTitle(R.string.app_name)
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
        }
        dirListener.startWatching()
        setupPermission()
        bindService(Intent(this, MessengerService::class.java), serviceConnection, Context.BIND_AUTO_CREATE)
        // --------------------------------------------------
        intervalButton = IntervalButtonWidget(main_test_2_button, "倒计时")
        main_test_2_button.setOnClickListener {
            intervalButton?.start()
        }
        main_test_button.setOnClickListener {
            WifiCompat.listenerByConn()
            WifiCompat.listenerByJob(MyApplication.getInstance())
            //            ThreadPlugins.runInIoThread(Runnable {
//                if (main_test_button.text == Thread.currentThread().name) {
////                    main_test_button.text = "测试"
//                } else {
////                    main_test_button.text = Thread.currentThread().name
//                }
//                main_test_button.invalidate()
//            })
            KotlinDebug.test(this)
            TestDebug.test(this)
//            startService(Intent(this, LogcatService::class.java))
            val msg = Message.obtain()
            msg.replyTo = messenger
            sendMessenger?.send(msg)
            // --------------------------------------------------
//            TestJni.newInstance().stringFromJNI()
            // --------------------------------------------------
        }
        // ------------------------------------------------------
        drawerToggle = ActionBarDrawerToggle(this, main_drawer_layout, R.string.app_name, R.string.app_name).apply {
            syncState()
        }
        main_drawer_layout.addDrawerListener(drawerToggle)

        main_left_nav.run {
            setNavigationItemSelectedListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.drawer_github_menu_item -> {
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse("https://github.com/zaze359/test.git")
                        startActivity(intent)
                    }
                }
                true
            }
        }
        // ------------------------------------------------------
        fragmentList.clear()
        fragmentList.add(TableFragment.newInstance("0"))
        fragmentList.add(TableFragment.newInstance("1"))
        // --------------------------------------------------
        main_viewpager.adapter = MyPagerAdapter(supportFragmentManager, fragmentList)
        main_viewpager.setOnHoverListener { v, event ->
            ZLog.i(ZTag.TAG_DEBUG, "main_viewpager on hover")
            true
        }
//        val obj = DeviceStatus()
//        weakReference = WeakReference(obj)
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return item?.run {
            return when (itemId) {
                android.R.id.home -> {
                    main_drawer_layout.openDrawer(GravityCompat.START)
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
        } ?: return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (main_drawer_layout.isDrawerOpen(main_left_nav)) {
            main_drawer_layout.closeDrawer(main_left_nav)
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dirListener.stopWatching()
        unbindService(serviceConnection)
        intervalButton?.stop()
    }

    /**
     * 申请权限
     */
    private fun setupPermission() {
        val array = arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CHANGE_NETWORK_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_FINE_LOCATION)
        PermissionUtil.checkAndRequestUserPermission(this, array, 1)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            PermissionUtil.checkAndRequestUserPermission(this, Manifest.permission.PACKAGE_USAGE_STATS, 2)
//        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val isGranted = grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
        if (!isGranted) {
            finish()
        }
    }

    // --------------------------------------------------
    inner class MyPagerAdapter(fm: FragmentManager, list: ArrayList<AbsFragment>?) : FragmentPagerAdapter(fm) {
        private val fragmentList = ArrayList<AbsFragment>()

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