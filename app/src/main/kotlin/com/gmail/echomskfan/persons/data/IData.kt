package com.gmail.echomskfan.persons.data

object IData {
    const val MAIN_URL = "https://echo.msk.ru"
        const val URL_ARCHIVE_PATH = "/archive"

    fun getCastFullUrl(shortUrl: String, pageNum: Int = 1) =
            "${IData.MAIN_URL}$shortUrl${IData.URL_ARCHIVE_PATH}/$pageNum.html"
}