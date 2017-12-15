package com.zaze.demo

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.zaze.common.base.BaseActivity
import com.zaze.common.base.BaseFragment
import com.zaze.common.widget.head.ZOrientation
import com.zaze.demo.component.table.ui.TableFragment
import com.zaze.demo.debug.KotlinDebug
import com.zaze.demo.debug.TestDebug
import kotlinx.android.synthetic.main.activity_main.*




/**
 * Description :
 * @author : ZAZE
 * @version : 2017-05-19 - 01:41
 */

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            val explode = TransitionInflater.from(this).inflateTransition(R.transition.explode)
//            window.exitTransition = explode
//        }
        headWidget.setText("zZz", ZOrientation.CENTER)
        // --------------------------------------------------
        main_test_button.text = "测试"
        // --------------------------------------------------
        val fragmentList = ArrayList<BaseFragment>()
        fragmentList.add(TableFragment.newInstance("0"))
        // --------------------------------------------------
        main_viewpager.adapter = MyPagerAdapter(supportFragmentManager, fragmentList)
        main_test_button.setOnClickListener {
            val debug = KotlinDebug()
//            debug.test()
            TestDebug.a(this)
//            ZAppUtil.startApplicationSimple(this, "com.xh.aoscstu")
//            ZAppUtil.startApplicationSimple(this, "com.xh.assist")
            // --------------------------------------------------
//            TestJni.newInstance().stringFromJNI()
            // --------------------------------------------------
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