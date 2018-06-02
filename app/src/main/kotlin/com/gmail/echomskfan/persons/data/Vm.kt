package com.gmail.echomskfan.persons.data

import com.gmail.echomskfan.persons.data.entity.VipEntity

data class VipVM(val url: String,
                 val name: String,
                 val profession: String,
                 val info: String,
                 val photoUrl: String) {

    companion object {
        fun fromEntity(entity: VipEntity): VipVM {
            return VipVM(
                    entity.url,
                    entity.firstName + " " + entity.lastName,
                    entity.profession,
                    entity.info,
                    entity.photoUrl)
        }
    }
}