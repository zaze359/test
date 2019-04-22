package com.zaze.common.base.ext

import android.util.Log
import com.zaze.utils.log.ZTag

/**
 * Description :
 * @author : ZAZE
 * @version : 2018-12-24 - 14:50
 */
fun printInfo(message: String) = Log.i(ZTag.TAG_DEBUG, message)

fun printError(error: String) = Log.e(ZTag.TAG_DEBUG, error)