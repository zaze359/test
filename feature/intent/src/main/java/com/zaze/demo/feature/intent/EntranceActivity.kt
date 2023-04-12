package com.zaze.demo.feature.intent

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.zaze.common.base.AbsActivity

class EntranceActivity : AbsActivity() {

    companion object {
        const val KEY = "key"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entrance)
        findViewById<TextView>(R.id.entrance_desc_tv).text = intent?.getStringExtra(KEY)?: "测试启动页面"
        findViewById<Button>(R.id.entrance_exit_btn).setOnClickListener {
            finish()
        }
    }
}