package com.zaze.demo

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.os.*
import android.view.KeyEvent
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.zaze.common.base.AbsActivity
import com.zaze.common.base.AbsFragment
import com.zaze.common.base.ext.setImmersion
import com.zaze.common.base.ext.setupActionBar
import com.zaze.common.widget.IntervalButtonWidget
import com.zaze.demo.debug.LogDirListener
import com.zaze.demo.debug.MessengerService
import com.zaze.demo.debug.TestDebug
import com.zaze.demo.debug.kotlin.KotlinDebug
import com.zaze.utils.DeviceUtil
import com.zaze.utils.FileUtil
import com.zaze.utils.ToastUtil
import com.zaze.utils.ZCommand
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import kotlinx.android.synthetic.main.activity_main.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.PermissionRequest
import java.lang.StringBuilder


/**
 * Description :
 * @author : ZAZE
 * @version : 2017-05-19 - 01:41
 */
class MainActivity : AbsActivity(), EasyPermissions.PermissionCallbacks {
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private val fragmentList = ArrayList<AbsFragment>()
    private var intervalButton: IntervalButtonWidget? = null
    private val dirListener = LogDirListener(FileUtil.getSDCardRoot() + "/")
    val write = Parcel.obtain().apply {
        writeInt(2233)
    }
    val reply = Parcel.obtain()
    private val messenger = Messenger(object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            ZLog.i(ZTag.TAG_DEBUG, "receiver messenger reply message")
            try {
                val serviceManager = Class.forName("android.os.ServiceManager");
                val method = serviceManager.getMethod("getService", String::class.java)
                val testBinder = method.invoke(null, "testBinder") as Binder
                ZLog.v(ZTag.TAG_DEBUG, "get testBinder success :$testBinder");
                testBinder.transact(0, write, reply, 0)
                val replayMessage = reply.readInt()
                ZLog.v(ZTag.TAG_DEBUG, "receiver testBinder message :$replayMessage");
            } catch (e: Exception) {
                ZLog.e(ZTag.TAG_DEBUG, "get testBinder fail");
            }
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
        setupActionBar(findViewById(R.id.main_toolbar)) {
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
//            val msg = Message.obtain()
//            msg.replyTo = messenger
//            sendMessenger?.send(msg)
//            //
            KotlinDebug.test(this)
            TestDebug.test(this)
            setupPermission()

            System.getProperties().forEach {
                ZLog.i(ZTag.TAG, "System.getProperties: ${it.key}=${it.value}")
            }


            val builder = StringBuilder()
            ZCommand.execCmdForRes(arrayOf("getprop")).successList.forEach {
                builder.append(it + "\n")
            }

            ZLog.i(ZTag.TAG, "getDeviceId: ${DeviceUtil.getDeviceId(this)}")
            ZLog.i(ZTag.TAG, "getUUID: ${DeviceUtil.getUUID(this)}")
            ZLog.i(ZTag.TAG, "serialno: ${ZCommand.execCmdForRes("getprop ro.serialno").successMsg}")
//
//            builder.toString()
//                    .replace("[", "")
//                    .replace("]", "")
//                    .split("\n").forEach {
//                        val kv = it.split(": ")
//                        if (kv.size >= 2) {
//                            repeat(kv.size) {
//                                ZLog.i(ZTag.TAG, "getProperties: : ${kv[0]}: ${kv[1]}")
//                            }
//                        }
//                    }

//            val wakeLockTask = PowerLockTask(this@MainActivity)
//            Thread(wakeLockTask).start()
//            Thread(AlarmTask(this)).start()
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                val pm = getSystemService(Context.POWER_SERVICE) as PowerManager
//                AppUtil.getInstalledApplications(this).forEach {
//                    ZLog.i(ZTag.TAG, "${it.packageName} isIgnoringBatteryOptimizations: ${pm.isIgnoringBatteryOptimizations(it.packageName)}")
//                }
//            }
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                startActivity(Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).also {
//                    it.data = Uri.parse("package:" + this.getPackageName())
//                })
//            }

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
        fragmentList.add(DemoFragment.newInstance("0"))
        fragmentList.add(DemoFragment.newInstance("1"))
        // --------------------------------------------------
        main_viewpager.adapter = MyPagerAdapter(supportFragmentManager, fragmentList)
        main_viewpager.setOnHoverListener { v, event ->
            ZLog.i(ZTag.TAG_DEBUG, "main_viewpager on hover")
            true
        }
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

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_PAGE_DOWN -> {
                ToastUtil.toast(this, "onKeyDown >> 向下翻页键 :$keyCode")
            }
            KeyEvent.KEYCODE_PAGE_UP -> {
                ToastUtil.toast(this, "onKeyDown >> 向上翻页键 :$keyCode")
            }
            else -> {
            }
        }
        return super.onKeyDown(keyCode, event)
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

        EasyPermissions.requestPermissions(PermissionRequest.Builder(this, 1, *array)
//                .setRationale("123456")
//                .setPositiveButtonText("确定")
//                .setNegativeButtonText("取消")
                .build())

//        PermissionUtil.checkAndRequestUserPermission(this, array, 1)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            PermissionUtil.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        ToastUtil.toast(this, "onPermissionsDenied :$perms")
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        ToastUtil.toast(this, "onPermissionsGranted :$perms")
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
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