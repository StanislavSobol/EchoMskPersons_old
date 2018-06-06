package com.gmail.echomskfan.persons.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = ItemDTO.TABLE)
data class ItemDTO(
        @PrimaryKey
        @ColumnInfo(name = FIELD_FULL_TEXT_URL) val fullTextURL: String,
        @ColumnInfo(name = FIELD_VIP_URL) val vipUrl: String, // FK
        @ColumnInfo(name = FIELD_TYPE) val type: String, // Интервью
        @ColumnInfo(name = FIELD_SUBTYPE) val subtype: String, // Персонально Ваш
        @ColumnInfo(name = FIELD_PHOTO_URL) val photoUrl: String,
        @ColumnInfo(name = FIELD_SHORT_TEXT) val shortText: String,
        @ColumnInfo(name = FIELD_MP3_URL) val mp3Url: String,
        @ColumnInfo(name = FIELD_MP3_DURATION) val mp3Duration: Int,
        @ColumnInfo(name = FIELD_FORMATTED_DATE) val formattedDate: String,
        @ColumnInfo(name = FIELD_PAGE_NUM) val pageNum: Int,
        @ColumnInfo(name = FIELD_ORDER_WITHIN_PAGE) var orderWithinPage: Int = 0,
        @ColumnInfo(name = FIELD_FAV, index = true) var fav: Boolean = false
) {
    companion object {
        const val TABLE = "casts"
        const val FIELD_FULL_TEXT_URL = "full_text_url"
        const val FIELD_VIP_URL = "vip_url"
        const val FIELD_TYPE = "type"
        const val FIELD_SUBTYPE = "subtype"
        const val FIELD_PHOTO_URL = "photo_url"
        const val FIELD_SHORT_TEXT = "short_text"
        const val FIELD_MP3_URL = "mp3_url"
        const val FIELD_MP3_DURATION = "mp3_duration"
        const val FIELD_FORMATTED_DATE = "formatted_date"
        const val FIELD_PAGE_NUM = "page_num"
        const val FIELD_ORDER_WITHIN_PAGE = "order_within_page"
        const val FIELD_FAV = "fav"
    }
}
