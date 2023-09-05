package com.zaze.demo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zaze.core.designsystem.util.SplashHelper

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SplashHelper.init(this)
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        startActivity(Intent(this, MainActivity::class.java))
//        startActivity(Intent(this, ComposeActivity::class.java))
        finish()
    }
}