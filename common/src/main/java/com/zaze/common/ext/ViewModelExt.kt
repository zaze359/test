package com.zaze.common.ext

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * @Description:
 * @Author zhaozhen
 * @Date 2024/6/18 09:50
 */
class ViewModelExt {
}

fun ViewModel.launchWithException(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    onError: suspend (e: Throwable) -> Unit = {},
    doFinally: suspend () -> Unit = {},
    block: suspend CoroutineScope.() -> Unit
): Job {
    return viewModelScope.launch(context, start) {
        runCatching {
            block.invoke(this)
        }.onFailure {
            onError(it)
        }
        doFinally()
    }
}