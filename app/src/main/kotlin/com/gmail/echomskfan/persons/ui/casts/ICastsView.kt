package com.gmail.echomskfan.persons.ui.casts

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.gmail.echomskfan.persons.data.CastVM

interface ICastsView : MvpView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showCasts(casts: List<CastVM>)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun updateItem(item: CastVM)
}