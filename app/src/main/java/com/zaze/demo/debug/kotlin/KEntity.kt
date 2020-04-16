package com.zaze.demo.debug.kotlin

import android.util.Log

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2020-02-04 - 13:42
 */
class KEntity(val name: String = "KEntity", val tag: String = "tag", val desc: String = "desc") {

    init {
        print("init 1 $this")
    }

    init {
        print("init 2 $this")
    }

    constructor(aa: String) : this(name = aa) {

    }


    inner class E {

    }
}