package com.zaze.demo.matrix.config

import android.util.Log
import com.tencent.mrs.plugin.IDynamicConfig
import com.tencent.mrs.plugin.IDynamicConfig.ExptEnum
import java.util.concurrent.TimeUnit

/**
 * Description :
 * @author : zaze
 * @version : 2022-05-17 23:12
 */
class DynamicConfigImplDemo : IDynamicConfig {

    fun isFPSEnable(): Boolean {
        return true
    }

    fun isTraceEnable(): Boolean {
        return true
    }

    fun isSignalAnrTraceEnable(): Boolean {
        return true
    }

    fun isMatrixEnable(): Boolean {
        return true
    }


    override fun get(key: String?, defStr: String?): String? {
        // for Activity leak detect
        if (ExptEnum.clicfg_matrix_resource_detect_interval_millis.name == key || ExptEnum.clicfg_matrix_resource_detect_interval_millis_bg.name == key) {
            Log.d(
                "DynamicConfig",
                "Matrix.ActivityRefWatcher: clicfg_matrix_resource_detect_interval_millis 10s"
            )
            return TimeUnit.SECONDS.toMillis(5).toString()
        }
        return defStr
    }

    override fun get(key: String?, defInt: Int): Int {
        return defInt
    }

    override fun get(key: String?, defLong: Long): Long {
        return defLong
    }

    override fun get(key: String?, defBool: Boolean): Boolean {
        return defBool
    }

    override fun get(key: String?, defFloat: Float): Float {
        return defFloat
    }
}