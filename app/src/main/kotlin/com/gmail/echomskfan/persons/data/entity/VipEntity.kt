package com.gmail.echomskfan.persons.data.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.gmail.echomskfan.persons.data.IData

@Entity(tableName = VipEntity.TABLE)
data class VipEntity(
        @PrimaryKey
        @ColumnInfo(name = FIELD_URL) val url: String,
        @ColumnInfo(name = FIELD_FIRST_NAME) val firstName: String,
        @ColumnInfo(name = FIELD_LAST_NAME) val lastName: String,
        @ColumnInfo(name = FIELD_PROFESSION) val profession: String,
        @ColumnInfo(name = FIELD_INFO) val info: String,
        @ColumnInfo(name = FIELD_PHOTO_URL) val photoUrl: String,
        @ColumnInfo(name = FIELD_FAV, index = true) val fav: Boolean = false
) {
    fun getFullUrl(pageNum: Int = 1) = "${IData.MAIN_URL}$url${IData.URL_ARCHIVE_PATH}/$pageNum.html"

    companion object {
        const val TABLE = "vips"
        const val FIELD_URL = "url"
        const val FIELD_FIRST_NAME = "first_name"
        const val FIELD_LAST_NAME = "last_name"
        const val FIELD_PROFESSION = "profession"
        const val FIELD_INFO = "info"
        const val FIELD_PHOTO_URL = "photo_url"
        const val FIELD_FAV = "fav"
    }
}