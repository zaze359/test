package com.zaze.demo

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.zaze.common.base.BaseActivity
import com.zaze.common.base.BaseFragment
import com.zaze.common.widget.IntervalButtonWidget
import com.zaze.common.widget.head.ZOrientation
import com.zaze.demo.component.table.ui.TableFragment
import com.zaze.demo.debug.KotlinDebug
import com.zaze.demo.debug.TestDebug
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
//    private lateinit var weakReference: WeakReference<DeviceStatus>

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
            KotlinDebug.test()
            TestDebug.test(this)
            // --------------------------------------------------
//            TestJni.newInstance().stringFromJNI()
            // --------------------------------------------------
        }
        setupPermission()
//        val obj = DeviceStatus()
//        weakReference = WeakReference(obj)
    }

    override fun onDestroy() {
        super.onDestroy()
        intervalButton?.stop()
    }

    /**
     * 申请权限
     */
    private fun setupPermission() {
        PermissionUtil.checkAndRequestUserPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, PermissionCode.WRITE_EXTERNAL_STORAGE)
        //        Utilities.checkAndRequestUserPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE, PermissionCode.READ_EXTERNAL_STORAGE);
        //        Utilities.checkAndRequestUserPermission(this, Manifest.permission.CALL_PHONE, PermissionCode.REQUEST_PERMISSION_CALL_PHONE);
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