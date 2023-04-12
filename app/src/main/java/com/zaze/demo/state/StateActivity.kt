package com.zaze.demo.state

import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.zaze.common.base.AbsActivity
import com.zaze.common.base.ext.setupActionBar
import com.zaze.demo.DemoViewModel
import com.zaze.demo.R
import com.zaze.demo.databinding.StateActBinding
import kotlinx.coroutines.launch


/**
 * 测试状态保存于恢复
 */
class StateActivity : AbsActivity() {

    private val viewModel: StateViewModel by viewModels()

    private lateinit var binding: StateActBinding

    override val showLifecycle: Boolean
        get() = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.state_act)
        setupActionBar(binding.stateToolbar) {
            title = "State"
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
            it.setNavigationOnClickListener {
                finish()
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    when (uiState) {
                        is StateUiState.None -> {
                            binding.stateInfoTv.text = "defualt"
                        }
                        is StateUiState.HasState -> {
                            binding.stateInfoTv.text = uiState.stateInfo
                        }
                    }
                }
            }
        }

        binding.stateSaveBtn.setOnClickListener {
            viewModel.saveState(binding.stateInputEt.text.toString())
        }
    }

    private fun refresh() {

    }

}