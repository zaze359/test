package com.zaze.logic.core

import kotlinx.coroutines.CoroutineDispatcher

import kotlinx.coroutines.Dispatchers

actual class AppDispatchers actual constructor() {
    actual val main: CoroutineDispatcher = Dispatchers.Main
    actual val io: CoroutineDispatcher = Dispatchers.IO
    actual val default: CoroutineDispatcher = Dispatchers.Default
    actual val unconfined: CoroutineDispatcher = Dispatchers.Unconfined
}
