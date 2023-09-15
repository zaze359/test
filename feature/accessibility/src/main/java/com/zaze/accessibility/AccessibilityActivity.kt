package com.zaze.accessibility

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.zaze.accessibility.databinding.ActivityAccessibilityBinding
import com.zaze.common.base.AbsActivity
import com.zaze.common.base.ext.initToolbar
import com.zaze.utils.IntentFactory

/**
 * Description :
 * @author : zaze
 * @version : 2023-09-08 00:49
 */
class AccessibilityActivity : AbsActivity() {
    private lateinit var binding: ActivityAccessibilityBinding

    private val accessibilitySettingsLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            refreshStatus()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccessibilityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initToolbar(binding.appBarLayout.toolbar)
        binding.openBtn.setOnClickListener {
            accessibilitySettingsLauncher.launch(IntentFactory.accessibilitySettings())
        }
        refreshStatus()
    }

    private fun refreshStatus() {
        binding.statusTv.text =
            if (AccessibilityHelper.isAccessibilityEnabled(this)) "已启动" else "未启动"
    }

}