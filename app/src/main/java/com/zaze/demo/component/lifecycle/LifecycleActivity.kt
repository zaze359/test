package com.zaze.demo.component.lifecycle

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.zaze.common.base.AbsActivity
import com.zaze.common.base.ext.setupActionBar
import com.zaze.demo.databinding.ActivityLifecycleBinding
import com.zaze.demo.debug.LogViewWrapper
import com.zaze.utils.log.ZLog
import kotlinx.coroutines.launch

/**
 * 测试生命周期用
 */
class LifecycleActivity : AbsActivity() {
    private lateinit var binding: ActivityLifecycleBinding

    private val viewModel: MyLifecycleViewModel by viewModels()

    private val isNeedStack = ZLog.isNeedStack()
    private val defaultLogFace = ZLog.getLogFace();

    override val showLifecycle: Boolean
        get() = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLifecycleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBar(binding.toolbar) { toolbar ->
            title = "生命周期观测"
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
            toolbar.setNavigationOnClickListener {
                finish()
            }
        }
        //
        ZLog.setNeedStack(false)
        val logViewWrapper = LogViewWrapper(binding.lifecycleMessageTv)
        ZLog.setLogFace(viewModel)
        //
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    when (it) {
                        is LifecycleUiState.Refresh -> {
                            logViewWrapper.setText(it.log)
                        }
                    }
                }
            }
        }
        viewModel.showLog()
    }


    override fun onDestroy() {
        super.onDestroy()
        ZLog.setNeedStack(isNeedStack)
        ZLog.setLogFace(defaultLogFace)
    }
}