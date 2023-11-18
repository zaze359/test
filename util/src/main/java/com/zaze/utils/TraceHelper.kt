/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zaze.utils

import com.zaze.utils.TraceHelper
import com.zaze.utils.log.ZTag
import android.os.Build
import android.os.SystemClock
import android.os.Trace
import com.zaze.utils.log.ZLog
import kotlinx.coroutines.CoroutineScope
import java.util.HashMap
import kotlin.coroutines.CoroutineContext

/**
 * A wrapper around [Trace] to allow easier proguarding for production builds.
 *
 *
 * To enable any tracing log, execute the following command:
 * $ adb shell setprop log.tag.TAGNAME VERBOSE
 */
object TraceHelper {
    private var ENABLED = false
    private const val SYSTEM_TRACE = false
    private val sUpTimes: MutableMap<String, Long> = HashMap()
    private const val TAG = ZTag.TAG + "TraceHelper"

    fun enable(enable: Boolean) {
        ENABLED = enable
    }

    fun beginSection(sectionName: String) {
        if (ENABLED) {
            if (SYSTEM_TRACE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                Trace.beginSection(sectionName)
            }
            sUpTimes[sectionName] = SystemClock.uptimeMillis()
        }
    }

    fun partitionSection(sectionName: String, partition: String) {
        if (ENABLED) {
            val time = sUpTimes[sectionName]
            if (time != null && time >= 0) {
                if (SYSTEM_TRACE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    Trace.endSection()
                    Trace.beginSection(sectionName)
                }
                sUpTimes[sectionName] = SystemClock.uptimeMillis()
                ZLog.d(
                    TAG,
                    sectionName + ">> " + partition + " : " + (SystemClock.uptimeMillis() - time)
                )
            }
        }
    }

    fun endSection(sectionName: String) {
        if (ENABLED) {
            endSection(sectionName, "finish")
        }
    }

    fun endSection(sectionName: String, msg: String) {
        if (ENABLED) {
            val time = sUpTimes.remove(sectionName)
            if (time != null && time >= 0) {
                if (SYSTEM_TRACE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    Trace.endSection()
                }
                ZLog.d(TAG, sectionName + ">> " + msg + " : " + (SystemClock.uptimeMillis() - time))
            }
        }
    }

    inline fun <T> trace(label: String, crossinline block: () -> T): T {
        try {
            beginSection(label)
            return block()
        } finally {
            endSection(label)
        }
    }
}