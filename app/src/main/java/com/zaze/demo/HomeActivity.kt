package com.zaze.demo

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.zaze.aarrepo.commons.base.ZBaseActivity
import com.zaze.aarrepo.commons.base.ZBaseFragment
import com.zaze.aarrepo.commons.widget.head.ZOrientation
import com.zaze.demo.component.table.ui.TableFragment
import com.zaze.demo.debug.KotlinDebug
import kotlinx.android.synthetic.main.activity_home.*

/**
 * Description :
 * @author : ZAZE
 * @version : 2017-05-19 - 01:41
 */

class HomeActivity : ZBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        headWidget.setText("主页", ZOrientation.CENTER)
        //        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            val explode = TransitionInflater.from(this).inflateTransition(R.transition.explode)
//            window.exitTransition = explode
//        }
        home_test_button.text = "测试"
        val fragmentList = ArrayList<ZBaseFragment>()
        fragmentList.add(TableFragment.newInstance("1"))

        home_viewpager.adapter = MyPagerAdapter(supportFragmentManager, fragmentList)
        home_test_button.setOnClickListener {
            val debug = KotlinDebug()
            debug.test()
            // --------------------------------------------------
//            TestJni.newInstance().stringFromJNI()
            // --------------------------------------------------
//            val uri: Uri = Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS)
//            Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS, 24)
//            contentResolver.notifyChange(uri, null)
            // --------------------------------------------------
//            val list_1 = AppUtil.getInstalledApp(this)
//            ZLog.i(ZTag.TAG_DEBUG, "${list_1.size}")
//            val list_2 = AppUtil.getInstalledPackage(this, 0)
//            ZLog.i(ZTag.TAG_DEBUG, "${list_2.size}")
//            val list_3 = AppUtil.getInstalledApplications(this, 0)
//            ZLog.i(ZTag.TAG_DEBUG, "${list_3.size}")

        }
    }

    inner class MyPagerAdapter(fm: FragmentManager, list: ArrayList<ZBaseFragment>?) : FragmentPagerAdapter(fm) {
        private val fragmentList = ArrayList<ZBaseFragment>()

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