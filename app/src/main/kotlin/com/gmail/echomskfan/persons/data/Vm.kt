package com.gmail.echomskfan.persons.data

data class VipVM(val url: String,
                 val name: String,
                 val profession: String,
                 val info: String,
                 val photoUrl: String) {

    companion object {
        fun fromEntity(entity: VipDTO): VipVM {
            return VipVM(
                    entity.url,
                    entity.firstName + " " + entity.lastName,
                    entity.profession,
                    entity.info,
                    entity.photoUrl)
        }
    }
}