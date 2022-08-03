package com.zaze.demo.component.handler

import android.os.Looper

/**
 * Description :
 * @author : zaze
 * @version : 2022-04-26 17:03
 */
class FakeHandlerThread : Thread() {

    override fun run() {
        super.run()
        Looper.prepare()
//        Looper.myLooper()
        Looper.loop()
    }


}