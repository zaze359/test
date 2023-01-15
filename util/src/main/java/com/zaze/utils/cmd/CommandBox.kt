package com.zaze.utils.cmd

import java.io.File
import java.lang.Exception

/**
 * Description :
 * @author : zaze
 * @version : 2021-09-30 - 13:32
 */
object CommandBox {
    var globalLog = false
    private var isRoot: Boolean? = null
    private val suPathname = arrayOf(
        "/data/local/su",
        "/data/local/bin/su",
        "/data/local/xbin/su",
        "/system/xbin/su",
        "/system/bin/su",
        "/system/bin/.ext/su",
        "/system/bin/failsafe/su",
        "/system/sd/xbin/su",
        "/system/usr/we-need-root/su",
        "/sbin/su",
        "/su/bin/su"
    )

    fun isRoot(redo: Boolean = false): Boolean {

        return when {
            isRoot == null || redo -> {
                if (searchCommand("su").isSuccess()) {
                    isRoot = true
                    return true
                }
                try {
                    suPathname.forEach {
                        if (File(it).exists()) {
                            isRoot = true
                            return true
                        }
                    }
                } catch (ignored: Exception) {
                }
                isRoot = false
                false
            }
            else -> {
                isRoot == true
            }
        }
    }

    /**
     * 查询指令是否存在
     */
    fun searchCommand(cmdName: String): CommandReturn {
        return doExecute(Command.NormalCommand(listOf("command -v $cmdName"), globalLog))
    }

    fun simpleExecute(cmdLine: String): CommandReturn {
        return execute(Command.NormalCommand(listOf(cmdLine), globalLog))
    }

    fun execute(command: Command): CommandReturn {
        if (command is Command.NormalCommand && isRoot()) {
            return doExecute(Command.RootCommand(command.cmdList, command.needLog))
        }
        return doExecute(command)
    }

    private fun doExecute(command: Command): CommandReturn {
        return CommandProcessor.execute(command)
    }
}