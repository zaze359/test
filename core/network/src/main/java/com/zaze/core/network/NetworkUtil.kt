package com.zaze.core.network

import com.zaze.common.thread.DefaultFactory
import com.zaze.common.thread.ThreadExecutorStub
import com.zaze.common.thread.ThreadPlugins
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit


val ThreadPlugins.requestExecutorStub by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
    ThreadExecutorStub(
        ThreadPoolExecutor(
            0,
            Runtime.getRuntime().availableProcessors() * 2,
            60L,
            TimeUnit.SECONDS,
            LinkedBlockingQueue(),
            DefaultFactory("zRequest")
        )
    )
}