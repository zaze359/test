package com.zaze.demo.component.kotlin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import butterknife.ButterKnife
import com.zaze.demo.R

/**
 * Description :
 * @author : ZAZE
 * @version : 2017-05-19 - 01:41
 */

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)


    }
}