package com.zaze.core.testing

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * 跨平台单元测试，运行于 commonTest，可在 JVM(桌面) target 上直接 `./gradlew :core:testing:jvmTest` 执行，
 * 无需 Android 设备或 Robolectric。
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun greeting_isCorrect() {
        assertEquals("Hello KMP", "Hello KMP")
    }
}
