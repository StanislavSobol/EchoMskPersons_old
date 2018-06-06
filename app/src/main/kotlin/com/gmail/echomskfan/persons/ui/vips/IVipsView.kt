package com.gmail.echomskfan.persons.ui.vips

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.gmail.echomskfan.persons.data.VipVM

interface IVipsView : MvpView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun addItems(items: List<VipVM>)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showCast(url: String)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun updateItem(item: VipVM)
}