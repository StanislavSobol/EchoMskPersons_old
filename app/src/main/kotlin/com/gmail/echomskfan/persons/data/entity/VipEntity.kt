package com.gmail.echomskfan.persons.data.entity

import com.gmail.echomskfan.persons.data.IData

data class VipEntity(
        val url: String,
        val firstName: String,
        val lastName: String,
        val profession: String,
        val info: String,
        val photoUrl: String,
        var fav: Boolean = false,
        var notification: Boolean = false
) {
    fun getFullUrl(pageNum: Int = 1) = "${IData.MAIN_URL}$url${IData.URL_ARCHIVE_PATH}/$pageNum.html"
}