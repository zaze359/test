package com.zaze.utils

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.DocumentsContract
import android.provider.MediaStore
import com.zaze.utils.CursorHelper
import java.util.*


fun <T> Uri?.query(
    context: Context, projection: Array<String>? = null, selection: String? = null,
    selectionArgs: Array<String>? = null, sortOrder: String? = null, onQuery: (Cursor) -> T
): T? {
    if (this == null) {
        return null
    }
    return context.contentResolver.query(this, projection, selection, selectionArgs, sortOrder)
        ?.let {
            val result = onQuery(it)
            it.close()
            result
        }
}

fun Uri.getImagePath(context: Context): String? {
    if (DocumentsContract.isDocumentUri(context, this)) {
        // document类型，通过documentId 查询
        val docId = DocumentsContract.getDocumentId(this)
        return when (this.authority) {
            "com.android.providers.media.documents" -> {
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI.queryImagePath(
                    context, "${MediaStore.Images.Media._ID}=${docId.split(":")[1]}"
                )
            }
            "com.android.providers.downloads.documents" -> {
                ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"),
                    docId.toLong()
                ).queryImagePath(context)
            }
            else -> {
                null
            }
        }
    }
    return when (this.scheme?.lowercase(Locale.getDefault())) {
        "content" -> { // content类型, 正常方式查询
            this.queryImagePath(context)
        }
        "file" -> { // file类型, 直接获取
            this.path
        }
        else -> {
            null
        }
    }
}

private fun Uri?.queryImagePath(context: Context, selection: String? = null): String? {
    if (this == null) {
        return null
    }
    return this.query(context = context, selection = selection) { cursor ->
        if (cursor.moveToFirst()) {
            CursorHelper.getString(cursor, MediaStore.Images.Media.DATA)
        } else {
            null
        }
    }
}