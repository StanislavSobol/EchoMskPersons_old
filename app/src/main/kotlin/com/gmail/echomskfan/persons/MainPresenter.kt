package com.gmail.echomskfan.persons

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter

@InjectViewState
class MainPresenter : MvpPresenter<IMainView>() {
    override fun attachView(view: IMainView?) {
        super.attachView(view)
        viewState.initViews()
    }
}