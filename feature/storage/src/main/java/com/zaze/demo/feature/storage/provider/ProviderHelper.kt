package com.zaze.demo.component.provider

import android.content.Context
import android.database.Cursor
import android.provider.BaseColumns
import android.provider.ContactsContract
import android.provider.MediaStore
import com.zaze.utils.CursorHelper
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag

object ProviderHelper {

    /**
     * 查询音频
     */
    fun queryAudios(
        context: Context,
        selection: String? = null,
        selectionArgs: Array<String>? = null,
        sortOrder: String = MediaStore.Audio.Media.DEFAULT_SORT_ORDER
    ): Cursor? {
        return context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            arrayOf(
                BaseColumns._ID,
                MediaStore.Audio.AudioColumns.TITLE,
                MediaStore.Audio.AudioColumns.TRACK,
                MediaStore.Audio.AudioColumns.YEAR,
                MediaStore.Audio.AudioColumns.DURATION,
                MediaStore.Audio.AudioColumns.DATA,
                MediaStore.Audio.AudioColumns.DATE_MODIFIED,
                MediaStore.Audio.AudioColumns.ALBUM_ID,
                MediaStore.Audio.AudioColumns.ALBUM,
                MediaStore.Audio.AudioColumns.ARTIST_ID,
                MediaStore.Audio.AudioColumns.ARTIST
            ), selection, selectionArgs, sortOrder
        )
    }

    /**
     * 查询联系人数据
     */
    fun readContacts(
        context: Context,
        selection: String? = null,
        selectionArgs: Array<String>? = null,
    ): Cursor? {
        return context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            selection,
            selectionArgs,
            null
        )
    }

    fun dealContacts(cursor: Cursor?) {
        if(cursor == null) return

        while (cursor.moveToNext()) {
            // 姓名
            val displayName =
                CursorHelper.getString(cursor, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            // 手机号
            val number =
                CursorHelper.getString(cursor, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            ZLog.d(ZTag.TAG_DEBUG, "find contacts: $displayName $number")
        }
        cursor.close()
    }


}