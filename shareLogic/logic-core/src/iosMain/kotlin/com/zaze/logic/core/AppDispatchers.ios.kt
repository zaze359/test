package com.zaze.logic.core

import kotlinx.coroutines.CoroutineDispatcher

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext

actual class AppDispatchers actual constructor() {
    actual val main: CoroutineDispatcher = Dispatchers.Main
    actual val io: CoroutineDispatcher = Dispatchers.Default
    actual val default: CoroutineDispatcher = Dispatchers.Default
    actual val unconfined: CoroutineDispatcher = Dispatchers.Unconfined
}
