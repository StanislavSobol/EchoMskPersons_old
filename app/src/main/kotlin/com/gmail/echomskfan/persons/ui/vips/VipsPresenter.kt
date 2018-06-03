package com.gmail.echomskfan.persons.ui.vips

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.gmail.echomskfan.persons.MApplication
import com.gmail.echomskfan.persons.data.VipVM
import com.gmail.echomskfan.persons.interactor.IInteractor
import com.gmail.echomskfan.persons.utils.ThrowableManager
import com.gmail.echomskfan.persons.utils.fromIoToMain
import javax.inject.Inject

@InjectViewState
class VipsPresenter : MvpPresenter<IVipsView>() {

    @Inject
    lateinit var interactor: IInteractor

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        MApplication.getDaggerComponents().inject(this)
        loadVipsFromJsonToDb()
    }

    private fun loadVipsFromJsonToDb() {
        interactor.copyVipsFromJsonToDb(MApplication.getAppContext())
                .map {
                    val vmList = mutableListOf<VipVM>()
                    it.forEach { vmList.add(VipVM.fromEntity(it)) }
                    vmList.toList()
                }
                .fromIoToMain()
                .subscribe(
                        {
                            viewState.loadVips(it)
                        },
                        {
                            ThrowableManager.process(it)
                        }
                )
    }

}