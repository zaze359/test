package com.zaze.demo.debug

import android.os.Binder
import android.os.Parcel
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag

/**
 * Description :
 * @author : zaze
 * @version : 2020-10-14 - 17:15
 */
class TestBinder : Binder() {
    override fun onTransact(code: Int, data: Parcel, reply: Parcel?, flags: Int): Boolean {
        ZLog.i(ZTag.TAG_DEBUG, "onTransact: $code")
        if (code == 0) {
            reply?.writeInt(data.readInt())
            return true
        }
        return super.onTransact(code, data, reply, flags)
    }
}