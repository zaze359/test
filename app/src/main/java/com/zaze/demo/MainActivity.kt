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
import com.zaze.common.base.BaseActivity
import com.zaze.common.base.BaseFragment
import com.zaze.common.permission.PermissionUtil
import com.zaze.common.widget.IntervalButtonWidget
import com.zaze.demo.component.table.ui.TableFragment
import com.zaze.demo.debug.KotlinDebug
import com.zaze.demo.debug.MessengerService
import com.zaze.demo.debug.TestDebug
import com.zaze.demo.util.setImmersion
import com.zaze.demo.util.setupActionBar
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
        setImmersion()
        setupActionBar(R.id.main_toolbar) {
            setTitle(R.string.app_name)
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
        }
        setupPermission()
        bindService(Intent(this, MessengerService::class.java), serviceConnection, Context.BIND_AUTO_CREATE)
        // --------------------------------------------------
        intervalButton = IntervalButtonWidget(main_test_2_button, "倒计时")
        main_test_2_button.setOnClickListener {
            intervalButton?.start()
        }
        main_test_button.text = "测试"
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

//        val obj = DeviceStatus()
//        weakReference = WeakReference(obj)
    }

    fun aa() {

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
        unbindService(serviceConnection)
        intervalButton?.stop()
    }


    /**
     * 申请权限
     */
    private fun setupPermission() {
        PermissionUtil.checkAndRequestUserPermission(this, arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.INTERNET
        ), 1)
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