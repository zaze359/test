package com.zaze.common.ext

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * @Description:
 * @Author zhaozhen
 * @Date 2024/6/18 09:49
 */
class FlowExt {
}


fun <T> Flow<T>.launchAndCollect(
    owner: LifecycleOwner,
    state: Lifecycle.State = Lifecycle.State.STARTED,
    block: (T) -> Unit
) {
    owner.lifecycleScope.launch {
        owner.repeatOnLifecycle(state) {
            this@launchAndCollect.collect {
                block(it)
            }
        }
    }
}