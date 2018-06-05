package com.gmail.echomskfan.persons.data

import java.util.*


data class PersonDTO(val id: Int = 0,
                     val name: String)

data class ItemPersonDTO(val id: Int = 0,
                         val itemDTO: ItemDTO? = null)

data class BlockDTO(val id: Int = 0,
                    val itemDTO: ItemDTO? = null,
                    val personDTO: PersonDTO? = null,
                    val text: String,
                    val quote: Boolean = false)


class TextDTO {
    val personDTOs = ArrayList<PersonDTO>()
    val blockDTOs = ArrayList<BlockDTO>()

    val parentItemDTO: ItemDTO? = null

    fun addTextWithAuthor(text: String, author: String) {
        if (text.isNotBlank()) {
            blockDTOs.add(
                    if (author.isBlank()) BlockDTO(text = text)
                    else BlockDTO(text = text, personDTO = PersonDTO(name = author))
            )
        }
    }

    fun addQuote(text: String) {
        if (text.isNotBlank()) {
            blockDTOs.add(BlockDTO(
                    text = text,
                    quote = true
            ))
        }
    }
}