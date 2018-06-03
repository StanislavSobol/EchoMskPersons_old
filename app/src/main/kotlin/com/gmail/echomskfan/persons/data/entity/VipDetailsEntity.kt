package com.gmail.echomskfan.persons.data.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = VipDetailsEntity.TABLE)
data class VipDetailsEntity(
        @PrimaryKey
        @ColumnInfo(name = FIELD_URL) val url: String,
        @ColumnInfo(name = FIELD_FAV, index = true) val fav: Boolean = false,
        @ColumnInfo(name = FIELD_NOTIFICATION) val notification: Boolean = false
) {
    companion object {
        const val TABLE = "vips_details"
        const val FIELD_URL = "url"
        const val FIELD_FAV = "fav"
        const val FIELD_NOTIFICATION = "notification"
    }
}