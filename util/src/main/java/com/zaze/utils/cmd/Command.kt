package com.zaze.utils.cmd


/**
 * Description :
 * @author : zaze
 * @version : 2021-09-30 - 10:24
 */
sealed class Command(
    val cmdList: List<String>,
    val needLog: Boolean,
) {
    class RootCommand(cmdList: List<String>, needLog: Boolean = false) : Command(cmdList, needLog)
    class NormalCommand(cmdList: List<String>, needLog: Boolean = false) : Command(cmdList, needLog)
}