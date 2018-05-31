package com.gmail.echomskfan.persons.utils

import java.text.SimpleDateFormat
import java.util.*


object StringUtils {

    fun isEmpty(s: String?): Boolean {
        return s == null || s.isEmpty()
    }

    fun getFirstName(name: String): String {
        return getStringForIndex(name, 0)
    }

    fun getLastName(name: String): String {
        return getStringForIndex(name, 1)
    }

    private fun getStringForIndex(name: String, index: Int): String {
        if (isEmpty(name)) {
            return ""
        }

        val mName = name.trim { it <= ' ' }
        val arr = mName.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        var i = 0
        for (arrS in arr) {
            if (!isEmpty(arrS.trim { it <= ' ' })) {
                if (index == i++) {
                    return arrS
                }
            }
        }

        return ""
    }

    fun emptyIfNull(s: String?): String {
        return s ?: ""
    }

    fun getAudioDuration(sec: Int): String {
        if (sec >= 3600) {
            val tz = TimeZone.getDefault()
            val cal = GregorianCalendar.getInstance(tz)
            val offsetInMillis = tz.getOffset(cal.timeInMillis)
            val date = Date((sec * 1000 - offsetInMillis).toLong())
            return SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(date)
        } else {
            return SimpleDateFormat("mm:ss", Locale.getDefault()).format(Date((sec * 1000).toLong()))
        }
    }
}