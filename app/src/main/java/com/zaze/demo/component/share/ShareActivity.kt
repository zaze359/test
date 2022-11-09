package com.zaze.demo.component.share//package com.zaze.demo.share
//
//import android.os.Bundle
//import com.zaze.common.base.AbsActivity
//import com.zaze.common.base.ext.replaceFragmentInActivity
//import com.zaze.common.base.ext.setupActionBar
//import kotlinx.android.synthetic.main.share_act.*
//
///**
// * Description :
// * @author : zaze
// * @version : 2020-09-11 - 15:54
// */
//class ShareActivity : AbsActivity() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.share_act)
//        setupActionBar(shareToolbar) {
//            setTitle("分享")
//            setDisplayHomeAsUpEnabled(true)
//            setHomeButtonEnabled(true)
//        }
//        shareToolbar.setNavigationOnClickListener {
//            onBackPressed()
//        }
//        replaceFragmentInActivity(WXShareFragment(), R.id.shareFrameLayout)
//    }
//}