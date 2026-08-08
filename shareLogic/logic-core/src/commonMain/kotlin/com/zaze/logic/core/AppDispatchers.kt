package com.zaze.logic.core

import kotlinx.coroutines.CoroutineDispatcher

/**
 * 跨平台的协程调度器抽象，通过 expect/actual 为各平台提供具体实现。
 * 参考 teacher_work_v4 的 logic-core/util/AppDispatchers.kt
 */
expect class AppDispatchers() {
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
    val default: CoroutineDispatcher
    val unconfined: CoroutineDispatcher
}
