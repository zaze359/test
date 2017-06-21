package com.zaze.demo.kotlin

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.View
import com.zaze.aarrepo.commons.base.ZBaseActivity
import com.zaze.aarrepo.commons.base.ZBaseFragment
import com.zaze.aarrepo.commons.widget.head.HeadFace
import com.zaze.demo.R
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
        headWidget.setText("主页", HeadFace.CENTER)

        //        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            val explode = TransitionInflater.from(this).inflateTransition(R.transition.explode)
//            window.exitTransition = explode
//        }

        home_test_button.text = "测试"
        val fragmentList = ArrayList<ZBaseFragment>()
        fragmentList.add(TableFragment.newInstance("1"))

        home_viewpager.adapter = MyPagerAdapter(supportFragmentManager, fragmentList)
    }

    fun testClick(view: View) {
//        val uri: Uri = Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS)
//        Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS, 24)
//        contentResolver.notifyChange(uri, null)

        val debug = KotlinDebug()
        debug.test()

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