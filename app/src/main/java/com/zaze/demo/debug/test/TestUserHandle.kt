package com.zaze.demo.debug.test

import android.content.Context
import android.os.Build
import android.os.UserHandle
import android.os.UserManager
import android.util.Log
import androidx.annotation.RequiresApi
import com.zaze.utils.log.ZTag

/**
 * Description :
 * @author : zaze
 * @version : 2021-09-30 - 15:55
 */
class TestUserHandle : ITest {

    override fun doTest(context: Context) {
        val set = HashSet<Int>()
        for (i in 0..100) {
            val uid = getUserHandleForUid(context, i)
            if (uid >= 0) {
                set.add(uid)
            }
            Log.e("aaaa", "getUserHandleForUid : $uid")
        }
    }

    private fun getUserHandleForUid(context: Context, uid: Int): Int {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                return UserHandle.getUserHandleForUid(uid)?.hashCode() ?: -1
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    return UserManager::class.java.getMethod(
                        "getUserSerialNumber",
                        Int::class.javaPrimitiveType
                    ).invoke(getUserManager(context), uid) as Int
                }
            }
        } catch (e: Throwable) {
            Log.e(ZTag.TAG_ERROR, "getUserHandleForUid", e)
        }
        return -1
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun getUserManager(context: Context): UserManager? {
        return context.getSystemService(Context.USER_SERVICE) as UserManager?
    }

}