package com.gmail.echomskfan.persons.ui.vips

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.gmail.echomskfan.persons.MApplication
import com.gmail.echomskfan.persons.data.VipVM
import com.gmail.echomskfan.persons.interactor.IInteractor
import com.gmail.echomskfan.persons.utils.ThrowableManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@InjectViewState
class VipsPresenter : MvpPresenter<IVipsView>() {

    @Inject
    lateinit var interactor: IInteractor

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        MApplication.getDaggerComponents().inject(this)
        loadVips()



        Log.d("SSS", "interactor = $interactor")
    }

    private fun loadVips() {
        val subs = interactor.loadVips(MApplication.getAppContext())
                .map {
                    val vmList = mutableListOf<VipVM>()
                    it.forEach { vmList.add(VipVM.fromEntity(it)) }
                    vmList.toList()
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            viewState.loadVips(it)
                        },
                        {
                            ThrowableManager.process(it)
                        }
                )

//                .subscribe(object: SingleObserver<List<VipDTO>> {
//                    override fun onSubscribe(d: Disposable?) {
//                    }
//
//                    override fun onSuccess(t: List<VipDTO>?) {
//                    }
//
//                    override fun onError(e: Throwable?) {
//                    }
//
//                }

        Log.d("SSS", "subs = $subs")
    }


}