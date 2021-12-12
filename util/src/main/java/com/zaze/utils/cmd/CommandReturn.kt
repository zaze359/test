package com.zaze.utils.cmd

/**
 * Description :
 * @author : zaze
 * @version : 2021-09-30 - 11:14
 */
data class CommandReturn(
    val code: Int,
    val success: String? = null,
    val error: String? = null
) {

    fun isSuccess(): Boolean {
        return code == 0
    }
}