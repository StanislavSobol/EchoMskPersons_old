package com.gmail.echomskfan.persons.data.entity

import com.gmail.echomskfan.persons.data.IData

data class VipEntity(
        val url: String, // casts URL
        val firstName: String,
        val lastName: String,
        val profession: String,
        val info: String,
        val photoUrl: String,
        var fav: Boolean = false,
        var notification: Boolean = false
) {
    fun getCastFullUrl(pageNum: Int = 1) = IData.getCastFullUrl(url, pageNum)
}