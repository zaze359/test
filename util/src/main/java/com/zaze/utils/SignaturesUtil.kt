package com.zaze.utils

import android.content.pm.Signature
import java.security.MessageDigest


/**
 * Description :
 * @author : ZAZE
 * @version : 2018-11-01 - 19:54
 */

object SignaturesUtil {

//    private val DEBUG = X500Principal("CN=Android Debug,O=Android,C=US")

    /**
     * 获取md5
     */
    @JvmStatic
    fun getMd5(signatures: Array<Signature>?): String {
        if (signatures == null) {
            return ""
        }
        val digest = MessageDigest.getInstance("MD5")
        signatures.forEach {
            digest.update(it.toByteArray())
        }
        return EncryptionUtil.byteArrayToHex(digest.digest())
    }
}