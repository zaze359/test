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
        lifecycleScope.launchWhenCreated {  }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    when (it) {
                        is LifecycleUiState.Refresh -> {
//                            logViewWrapper.setText(it.log)
                            logViewWrapper.setText("简答题\n" +
                                    "1、CSMA/CD的工作原理？\n" +
                                    "<img>\n" +
                                    "<rplc>CSMA/CD协议的要点：（1）、多点接入，许多计算机以多点接入的方式连接在一根总线上。协议的实质是“载波监听”和“多路访问”。(2)、载波监听指每一个站在发送数据之前要检测一下总线上是否有其他计算机在发送数据，如果有，则暂时不要发送数据，以免发生碰撞     ")
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