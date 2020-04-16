package com.zaze.demo.debug.kotlin

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2020-02-04 - 11:33
 */
object Infix {
    fun debug() {
        val m = mapOf(
                1 to "one",
                2 to "two"
        )
        //
        val a = InfixA()
        a called "中缀表达式调用"
        a.called("普通方式调用")
    }
}

class InfixA {
    infix fun called(name: String) {
        println("A called : $name")
    }

}