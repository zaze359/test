package com.zaze.utils.ext

import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag

/// -------------------------------

fun printInfo(message: String) = ZLog.i(ZTag.TAG_DEBUG, message)

fun printError(error: String) = ZLog.e(ZTag.TAG_DEBUG, error)

