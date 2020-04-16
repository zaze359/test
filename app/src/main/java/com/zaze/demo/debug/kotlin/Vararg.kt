package com.zaze.demo.debug.kotlin

import com.tencent.bugly.proguard.aa

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2020-02-04 - 11:31
 */
object Vararg {
    fun debug() {
        printLetters("1", "2", "3", count = 3)
        printLetters(*arrayOf("1", "2", "3"), count = 3)
    }

    fun printLetters(vararg letters: String, count: Int) {
        println("letter count : $count")
        for ((index, value) in letters.withIndex()) {
            println("letter $index : $value")
        }

    }
}