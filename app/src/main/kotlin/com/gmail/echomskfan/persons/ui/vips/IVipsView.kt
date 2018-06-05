package com.gmail.echomskfan.persons.ui.vips

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.gmail.echomskfan.persons.data.VipVM

interface IVipsView : MvpView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showVips(vips: List<VipVM>)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showVip(url: String)
}