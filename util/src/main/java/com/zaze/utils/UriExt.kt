package com.zaze.utils

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.DocumentsContract
import android.provider.DocumentsProvider
import android.provider.MediaStore
import java.util.*


private fun te(packageName: String, resId: Int) {
    // 项目资源，例如R.mipmap.ic_launcher
    val resourceUri = Uri.parse("android.resource://$packageName/$resId")
//    val resourceUri = Uri.parse("res:///$resId")
    // 项目名 assets 下
    val assetUri = Uri.parse("file:///android_asset/aa.txt")
    // ContentProvider
    // schema: content
    // authority: ContentProvider.authority
    // sdcard下的 Download 目录。
    val contentUri = Uri.parse("content://downloads/public_downloads")
    // authority: com.zaze.demo.fileProvider
    // path: external_storage_root/Android/data/com.zaze.demo/cache/image_1692273904967.jpg
    val appContentUri =
        Uri.parse("content://com.zaze.demo.fileProvider/external_storage_root/Android/data/com.zaze.demo/cache/image_1692273904967.jpg")
}

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
    val schema = this.scheme?.lowercase(Locale.getDefault())
    return when {
        DocumentsContract.isDocumentUri(context, this) -> { // document类型，通过documentId 查询
            val docId = DocumentsContract.getDocumentId(this)
            when (this.authority) {
                "com.android.providers.media.documents" -> {
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI.queryPathFromContentUri(
                        context, "${MediaStore.Images.Media._ID}=${docId.split(":")[1]}"
                    )
                }
                "com.android.providers.downloads.documents" -> {
                    ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        docId.split(":")[1].toLong()
                    ).queryPathFromContentUri(context)
                }

                else -> {
                    null
                }
            }
        }
        "content" == schema -> { // content类型, 正常方式查询
            this.queryPathFromContentUri(context)

        }
        "file" == schema -> { // file类型, 直接获取
            this.path
        }
        else -> {
            null
        }
    }
}

private fun Uri?.queryPathFromContentUri(context: Context, selection: String? = null): String? {
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