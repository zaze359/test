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
package com.zaze.common.util;

import android.os.Build;
import android.os.SystemClock;
import android.os.Trace;

import com.zaze.utils.BuildConfig;
import com.zaze.utils.log.ZLog;
import com.zaze.utils.log.ZTag;

import java.util.HashMap;
import java.util.Map;

/**
 * A wrapper around {@link Trace} to allow easier proguarding for production builds.
 * <p>
 * To enable any tracing log, execute the following command:
 * $ adb shell setprop log.tag.TAGNAME VERBOSE
 */
public class TraceHelper {

    private static final boolean ENABLED = BuildConfig.DEBUG;

    private static final boolean SYSTEM_TRACE = false;
    private static final Map<String, Long> sUpTimes = ENABLED ? new HashMap<>() : null;

    private static final String TAG = ZTag.TAG + "TraceHelper";

    public static void beginSection(String sectionName) {
        if (ENABLED) {
            if (SYSTEM_TRACE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                Trace.beginSection(sectionName);
            }
            sUpTimes.put(sectionName, SystemClock.uptimeMillis());
        }
    }

    public static void partitionSection(String sectionName, String partition) {
        if (ENABLED) {
            Long time = sUpTimes.get(sectionName);
            if (time != null && time >= 0) {
                if (SYSTEM_TRACE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    Trace.endSection();
                    Trace.beginSection(sectionName);
                }
                sUpTimes.put(sectionName, SystemClock.uptimeMillis());
                ZLog.d(TAG, sectionName + ">> " + partition + " : " + (SystemClock.uptimeMillis() - time));
            }
        }
    }

    public static void endSection(String sectionName) {
        if (ENABLED) {
            endSection(sectionName, "finish");
        }
    }

    public static void endSection(String sectionName, String msg) {
        if (ENABLED) {
            Long time = sUpTimes.get(sectionName);
            if (time != null && time >= 0) {
                if (SYSTEM_TRACE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    Trace.endSection();
                }
                ZLog.d(TAG, sectionName + ">> " + msg + " : " + (SystemClock.uptimeMillis() - time));
            }
        }
    }
}

