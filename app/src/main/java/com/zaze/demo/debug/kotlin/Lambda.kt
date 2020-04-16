package com.zaze.demo.debug.kotlin

import com.tencent.bugly.proguard.aa
import com.tencent.bugly.proguard.p
import com.tencent.bugly.proguard.x
import com.tencent.bugly.proguard.y

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2020-02-02 - 16:52
 */


//val sum: (Int, Int) -> Int = { x: Int, y: Int ->
//    x + y
//}
//val sum: (Int, Int) -> Int = { x, y ->
//    x + y
//}
val sum = { x: Int, y: Int ->
    x + y
}

//fun foo(int: Int): () -> Unit = {
//    println(int)
//}

fun foo(x: Int): (y: Int) -> Unit = { y ->
    println("foo : ${sum(x, y)}")
}

object Lambda {
    fun debug() {
        { x: Int -> println("aaa : $x") }(1)
        sum(1, 2)
        sum.invoke(1, 2)
        arrayOf(1, 2, 3).forEach {
            //            foo(it).invoke(1)
            foo(it)(1)
        }
    }
}
