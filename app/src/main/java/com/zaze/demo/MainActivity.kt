package com.zaze.demo

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.text.TextUtils
import com.zaze.common.base.ZBaseActivity
import com.zaze.common.base.ZBaseFragment
import com.zaze.common.widget.head.ZOrientation
import com.zaze.demo.component.table.ui.TableFragment
import com.zaze.demo.debug.KotlinDebug
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import kotlinx.android.synthetic.main.activity_main.*
import java.util.regex.Pattern


/**
 * Description :
 * @author : ZAZE
 * @version : 2017-05-19 - 01:41
 */

class MainActivity : ZBaseActivity() {

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
        val fragmentList = ArrayList<ZBaseFragment>()
        fragmentList.add(TableFragment.newInstance("0"))
        // --------------------------------------------------
        val mClipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        mClipboardManager.addPrimaryClipChangedListener {
            ZLog.i(ZTag.TAG_DEBUG, "onPrimaryClipChanged")
            if (mClipboardManager.hasPrimaryClip()) {
                val clipData = mClipboardManager.primaryClip
                val count = clipData.itemCount
                for (i in 0 until count) {
                    val item = clipData.getItemAt(i)
                    ZLog.i(ZTag.TAG_DEBUG, "uri : ${item.uri}")
                    ZLog.i(ZTag.TAG_DEBUG, "text: ${item.text}")
                    ZLog.i(ZTag.TAG_DEBUG, "item: ${item.toString()}")
                    if (!TextUtils.equals(item.text, "学海教育")) {
                        val cd = ClipData.newPlainText("学海教育", "学海教育")
                        mClipboardManager.primaryClip = cd
                        break
                    }
                }
            }
        }
        // --------------------------------------------------
        main_viewpager.adapter = MyPagerAdapter(supportFragmentManager, fragmentList)
        main_test_button.setOnClickListener {
            val debug = KotlinDebug()
//            debug.test()
//            ZAppUtil.startApplicationSimple(this, "com.xh.aoscstu")
//            ZAppUtil.startApplicationSimple(this, "com.xh.assist")
//            val matchStr = "http[s]?://auth-\\d*.wifi.com/\\d+/mobile.html.*"
            val matchStr = "http[s]?://auth-\\d*.wifi.com/.*"
            val pattern = Pattern.compile(matchStr)
//            val matcher = pattern.matcher("https://auth-332211.wifi.com/1110/mobile.html")
            val matcher = pattern.matcher("http://auth-22080400.wifi.com/?x=8788950992870874614728340667105083&c=97996915&ref=http://data.yunzuoye.net/data/wifi.html")
            while (matcher.find()) {
                ZLog.i(ZTag.TAG_DEBUG, matcher.group())
            }

            // --------------------------------------------------

            // --------------------------------------------------
//            TestJni.newInstance().stringFromJNI()
            // --------------------------------------------------
//            val mBitmap = CodeUtils.createImage("aaaaa", 48, 48, null)
//            main_test_iv.setImageBitmap(mBitmap)
//            // --------------------------------------------------
//            // --------------------------------------------------
//            val clipData = mClipboardManager.primaryClip
//            val count = clipData.itemCount
//            ZLog.i(ZTag.TAG_DEBUG, "hasPrimaryClip : ${mClipboardManager.hasPrimaryClip()}")
//            for (i in 0 until count) {
//                val item = clipData.getItemAt(i)
//                ZLog.i(ZTag.TAG_DEBUG, "uri : ${item.uri}")
//                ZLog.i(ZTag.TAG_DEBUG, "text: ${item.text}")
//                ZLog.i(ZTag.TAG_DEBUG, "item: ${item.toString()}")
//
//            }
        }

    }

    // --------------------------------------------------
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