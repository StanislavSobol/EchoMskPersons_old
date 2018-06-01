package com.gmail.echomskfan.persons.ui.vips

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.gmail.echomskfan.persons.MApplication
import com.gmail.echomskfan.persons.interactor.IInteractor
import javax.inject.Inject

@InjectViewState
class VipsPresenter:MvpPresenter<IVipsView>() {

    @Inject
    lateinit var interactor: IInteractor

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        MApplication.getDaggerComponents().inject(this)

        Log.d("SSS", "interactor = $interactor")
    }
}