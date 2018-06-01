package com.gmail.echomskfan.persons.interactor.parser

import android.content.Context
import com.gmail.echomskfan.persons.data.ItemDTO
import com.gmail.echomskfan.persons.data.TextDTO
import com.gmail.echomskfan.persons.data.VipDTO

interface IParser {

    fun getVips(context: Context):List<VipDTO>

    fun getVip(url: String): VipDTO?

    fun getCasts(url: String, vipDTO: VipDTO): List<ItemDTO>

    fun getTextData(url: String): TextDTO?
}