package com.zaze.core.network

import com.zaze.utils.ThreadExecutorStub
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit


val requestExecutorStub by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
    ThreadExecutorStub(
        ThreadPoolExecutor(
            0,
            Runtime.getRuntime().availableProcessors() * 2,
            60L,
            TimeUnit.SECONDS,
            LinkedBlockingQueue(),
            NetworkThreadFactory()
        )
    )
}