package com.gmail.echomskfan.persons.interactor.parser

import android.content.Context
import com.gmail.echomskfan.persons.data.ItemDTO
import com.gmail.echomskfan.persons.data.TextDTO
import com.gmail.echomskfan.persons.data.entity.VipEntity

interface IParser {

    fun getVips(context: Context): List<VipEntity>

    fun getCasts(url: String, vipEntity: VipEntity): List<ItemDTO>

    fun getTextData(url: String): TextDTO?
}