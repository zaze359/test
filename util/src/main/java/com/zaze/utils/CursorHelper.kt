package com.zaze.utils

import android.database.Cursor

object CursorHelper {
    @JvmStatic
    fun getString(cursor: Cursor, columnName: String): String {
        val index = cursor.getColumnIndex(columnName)
        return if (index < 0) {
            ""
        } else cursor.getString(index)
    }

    @JvmStatic
    fun getLong(cursor: Cursor, columnName: String): Long {
        val index = cursor.getColumnIndex(columnName)
        return if (index < 0) {
            -1L
        } else cursor.getLong(index)
    }

    @JvmStatic
    fun getInt(cursor: Cursor, columnName: String): Int {
        val index = cursor.getColumnIndex(columnName)
        return if (index < 0) {
            -1
        } else cursor.getInt(index)
    }
}

