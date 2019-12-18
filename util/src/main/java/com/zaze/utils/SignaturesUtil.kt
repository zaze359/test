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
    fun getSignatures(context: Context, algorithm: String): String? {
        return getSignatures(context)?.let { signatures ->
            val messageDigest = MessageDigest.getInstance(algorithm)
            signatures.forEach {
                messageDigest.update(it.toByteArray())
            }
            return EncryptionUtil.byteArrayToHex(messageDigest.digest())
        }

    }

    @JvmStatic
    private fun getSignatures(context: Context): Array<Signature>? {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                AppUtil.getPackageInfo(context, PackageManager.GET_SIGNING_CERTIFICATES)?.signingInfo?.apkContentsSigners
            } else {
                AppUtil.getPackageInfo(context, PackageManager.GET_SIGNATURES)?.signatures
            }
//            return getMd5(AppUtil.getPackageInfo(context, PackageManager.GET_SIGNATURES)?.signatures)
        } catch (e: Exception) {
            null
        }
    }


}