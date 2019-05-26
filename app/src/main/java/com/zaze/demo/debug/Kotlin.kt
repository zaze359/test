//package com.zaze.demo.debug
//
///**
// * Description :
// *
// * @author : ZAZE
// * @version : 2018-12-04 - 0:11
// */
//fun main(args: Array<String>) {
//    val strList = listOf("a", "ab", "abc","abcd","abcde","abcdef","abcdefg")
//    // 非常好用的流式 API filter，flat，map 等等
//    val mstrList = strList.filter(h(::g, ::f))
//    println(mstrList)
//    mstrList.forEachIndexed{
//        index,value ->
//        println("$value = ${value.length}")
//    }
//    args.forEach(::println)
//    val p = Test::println
//
//    val t = Test()
//    args.forEach(t::println)
//
//    //扩展方法有一个隐含的参数--实例
//    //Kotlin1.1才开始支持
//    args.filter(String::isEmpty)
//
//}
//// ------------------------------------------------------
//class Test {
//    fun println(any: Any) {
//        kotlin.io.println(any)
//    }
//}
//
//// ------------------------------------------------------
//class MyInt(val value: Int) {
//    fun show() {
//        println(value)
//    }
//}
//
//// ------------------------------------------------------
//typealias G = (String) -> Int
//typealias F = (Int) -> Boolean
//typealias H = (String) -> Boolean
//
//
//fun g(s: String) = s.length
//fun f(x: Int) = x % 2 != 0
//
//
//fun h(g: G, f: F): H {
//    return { x -> f(g(x)) }
//}
//
//
//val display: (MyInt) -> Unit = MyInt::show