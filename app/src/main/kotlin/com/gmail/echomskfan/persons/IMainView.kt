package com.gmail.echomskfan.persons

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType


interface IMainView : MvpView {
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun initViews()
}