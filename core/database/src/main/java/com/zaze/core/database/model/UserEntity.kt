package com.zaze.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2017-03-30 - 00:43
 */
@Entity(tableName = "t_user")
data class UserEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "user_id")
    val userId: Long,

    val username: String
)