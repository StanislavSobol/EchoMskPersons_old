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
                  val photoUrl: String,
                  val shortText: String,
                  val mp3Url: String,
                  val mp3Duration: Int,
                  val fav: Boolean
) {
    companion object {
        fun fromEntity(entity: ItemDTO) =
                CastVM(
                        entity.fullTextURL,
                        entity.type,
                        entity.subtype,
                        entity.photoUrl,
                        entity.shortText,
                        entity.mp3Url,
                        entity.mp3Duration,
                        entity.fav
                )
    }
}