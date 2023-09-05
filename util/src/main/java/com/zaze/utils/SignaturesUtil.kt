package com.zaze.utils

import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.os.Build
import java.security.MessageDigest


/**
 * Description :
 * @author : ZAZE
 * @version : 2018-11-01 - 19:54
 */

object SignaturesUtil {

    //    private val DEBUG = X500Principal("CN=Android Debug,O=Android,C=US")
    @JvmStatic
    fun getSignatures(context: Context, algorithm: String?): String? {
        val signatures = AppUtil.getSignatures(context)
        if (signatures.isNullOrEmpty()) {
            return null
        }
        if (algorithm.isNullOrEmpty()) {
            return EncryptionUtil.byteArrayToHex(signatures[0].toByteArray())
        }
        val messageDigest = MessageDigest.getInstance(algorithm)
        signatures.forEach {
            messageDigest.update(it.toByteArray())
        }
        return EncryptionUtil.byteArrayToHex(messageDigest.digest())

    }

}