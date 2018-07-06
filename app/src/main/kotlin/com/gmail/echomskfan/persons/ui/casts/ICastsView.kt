package com.gmail.echomskfan.persons.ui.casts

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.gmail.echomskfan.persons.data.CastVM

@StateStrategyType(AddToEndSingleStrategy::class)
interface ICastsView : MvpView {

    fun addItems(items: List<CastVM>)

    fun updateItem(item: CastVM)

    fun removeListProgressBar()
}