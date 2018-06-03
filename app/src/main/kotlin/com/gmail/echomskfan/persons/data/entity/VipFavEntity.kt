package com.gmail.echomskfan.persons.data.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.gmail.echomskfan.persons.data.IData

@Entity(tableName = VipFavEntity.TABLE)
data class VipFavEntity(
        @PrimaryKey
        @ColumnInfo(name = FIELD_URL) val url: String,
        @ColumnInfo(name = FIELD_FAV, index = true) val fav: Boolean = false
) {
    fun getFullUrl(pageNum: Int = 1) = "${IData.MAIN_URL}$url${IData.URL_ARCHIVE_PATH}/$pageNum.html"

    companion object {
        const val TABLE = "vips_fav"
        const val FIELD_URL = "url"
        const val FIELD_FAV = "fav"
    }
}