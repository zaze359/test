package com.zaze.demo.debug.test

import android.content.Context
import com.zaze.utils.DeviceUtil
import com.zaze.utils.ZCommand
import com.zaze.utils.cmd.CommandBox
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag

/**
 * Description :
 * @author : zaze
 * @version : 2021-09-30 - 16:04
 */
class TestCommand : ITest {
    override fun doTest(context: Context) {
        CommandBox.globalLog = true
        ZLog.i(ZTag.TAG, "ZCommand isRoot: ${ZCommand.isRoot()}")
        ZLog.i(ZTag.TAG, "isRoot: ${CommandBox.isRoot(true)}")
        ZLog.i(ZTag.TAG, "ls: ${CommandBox.searchCommand("ls")}")
        ZLog.i(ZTag.TAG, "ls: ${CommandBox.simpleExecute("which ls")}")
        ZLog.i(ZTag.TAG, "ls: ${CommandBox.simpleExecute("ls /sdcard")}")
//        ZLog.i(ZTag.TAG, "isRoot:${ZCommand.isRoot()}")
//        getProperties(context)
    }

    private fun getProperties(context: Context) {
        System.getProperties().forEach {
            ZLog.i(ZTag.TAG, "System.getProperties: ${it.key}=${it.value}")
        }
        val builder = StringBuilder()
        CommandBox.simpleExecute("getprop").let {
            builder.append(it.success)
        }
        ZLog.i(ZTag.TAG, "getDeviceId: ${DeviceUtil.getDeviceId(context)}")
        ZLog.i(ZTag.TAG, "getUUID: ${DeviceUtil.getUUID(context)}")
        ZLog.i(
            ZTag.TAG,
            "serialno: ${CommandBox.simpleExecute("getprop ro.serialno").success}"
        )

        builder.toString()
            .replace("[", "")
            .replace("]", "")
            .split("\n").forEach {
                val kv = it.split(": ")
                if (kv.size >= 2) {
                    repeat(kv.size) {
                        ZLog.i(ZTag.TAG, "getProperties: : ${kv[0]}: ${kv[1]}")
                    }
                }
            }
    }
}