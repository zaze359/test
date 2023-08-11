package com.zaze.core.nativelib

object MyJvmtiAgent {

    external fun test(): String?
    external fun init()
    external fun release()
}