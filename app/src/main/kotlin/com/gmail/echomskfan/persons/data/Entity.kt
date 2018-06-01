package com.gmail.echomskfan.persons.data

import android.support.annotation.VisibleForTesting

data class VipDTO(val url:String,
                  val firstName:String,
                  val lastName:String,
                  val profession:String,
                  val info:String,
                  val photoUrl:String
                  )
{
    fun getFullUrl(pageNum:Int = 1) = "${IData.MAIN_URL}$url${IData.URL_ARCHIVE_PATH}/$pageNum.html"
}

data class ItemDTO(val fullTextURL:String,
                  val type:String, // Интервью
                  val subtype:String, // Персонально Ваш
                  val photoURL:String,
                  val shortText:String,
                  val mp3URL:String,
                  val mp3Duration:Int,
                  val formattedDate:String,
                  val pageNum:Int,
                  val orderWithinPage:Int,
                  val favorite:Boolean
) {


    @VisibleForTesting
    fun isValid(): Boolean {
        return !fullTextURL.isEmpty() &&
                !type.isEmpty() &&
                !formattedDate.isEmpty()
    }


}