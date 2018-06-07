package com.gmail.echomskfan.persons.data

import com.gmail.echomskfan.persons.data.entity.VipEntity

data class VipVM(val url: String,
                 val name: String,
                 val profession: String,
                 val info: String,
                 val photoUrl: String,
                 var fav: Boolean,
                 var notification: Boolean) {

    companion object {
        fun fromEntity(entity: VipEntity) =
                VipVM(
                        entity.url,
                        entity.firstName + " " + entity.lastName,
                        entity.profession,
                        entity.info,
                        entity.photoUrl,
                        entity.fav,
                        entity.notification)
    }
}

data class CastVM(val fullTextURL: String,
                  val type: String,
                  val subtype: String,
                  val formattedDate: String,
                  val photoUrl: String,
                  val shortText: String,
                  val mp3Url: String,
                  val mp3Duration: Int,
                  val pageNum: Int,
                  var fav: Boolean

) {
    fun getTypeSubtype(): String {
        var result = type
        if (!subtype.isEmpty()) {
            result += ": $subtype"
        }
        return result
    }

    companion object {

        fun fromEntitiesList(casts: List<ItemDTO>): List<CastVM> {
            val result = mutableListOf<CastVM>()
            casts.forEach { result.add(CastVM.fromEntity(it)) }
            return result.toList()
        }

        fun fromEntity(entity: ItemDTO) =
                CastVM(
                        entity.fullTextURL,
                        entity.type,
                        entity.subtype,
                        entity.formattedDate,
                        entity.photoUrl,
                        entity.shortText,
                        entity.mp3Url,
                        entity.mp3Duration,
                        entity.pageNum,
                        entity.fav
                )
    }
}