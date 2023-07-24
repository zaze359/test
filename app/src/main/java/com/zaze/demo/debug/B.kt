package com.zaze.demo.debug


fun topB(s: String) {}

class B {
    val c by lazy {
        ""
    }

    internal fun b() {

    }

    private fun bb() {
        println("$c")
    }
}


class BB() {
    fun b() {
        val b = B()
        b.b()
//        b.bb()
    }
}

object ObjB {

}