package com.zaze.common.util

import android.content.Context
import android.net.Uri
import android.os.ParcelFileDescriptor
import androidx.core.content.FileProvider
import com.zaze.utils.FileUtil
import java.io.File

object FileProviderHelper {
    private fun authority(context: Context) = "${context.packageName}.fileProvider"

    fun getUriForFile(
        context: Context,
        file: File
    ): Uri {
        FileUtil.createFileNotExists(file)
        return FileProvider.getUriForFile(
            context,
            authority(context),
            file
        )
    }

    fun openFileDescriptor(context: Context, uri: Uri, mode: String): ParcelFileDescriptor? {
        return context.contentResolver.openFileDescriptor(uri, mode)
    }
}