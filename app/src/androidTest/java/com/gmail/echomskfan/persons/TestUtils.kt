package com.gmail.echomskfan.persons

import com.gmail.echomskfan.persons.data.BlockDTO
import com.gmail.echomskfan.persons.data.ItemDTO
import com.gmail.echomskfan.persons.data.PersonDTO
import com.gmail.echomskfan.persons.data.entity.VipEntity


internal const val VIPS_AMOUNT = 2

internal fun VipEntity.isValid() = url.isNotEmpty() &&
        firstName.isNotEmpty() &&
        lastName.isNotEmpty() &&
        profession.isNotEmpty() &&
        info.isNotEmpty() &&
        photoUrl.isNotEmpty()

internal fun ItemDTO.isValid() = fullTextURL.isNotEmpty() &&
        type.isNotEmpty() &&
        formattedDate.isNotEmpty()

internal fun BlockDTO.isValid() = text.isNotEmpty() && (quote && personDTO == null || !quote)

internal fun PersonDTO.isValid() = name.isNotEmpty()