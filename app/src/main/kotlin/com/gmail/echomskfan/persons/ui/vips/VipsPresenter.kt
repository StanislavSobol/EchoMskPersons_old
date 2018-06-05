package com.gmail.echomskfan.persons.ui.vips

import android.support.annotation.VisibleForTesting
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.gmail.echomskfan.persons.MApplication
import com.gmail.echomskfan.persons.data.VipVM
import com.gmail.echomskfan.persons.interactor.IInteractor
import com.gmail.echomskfan.persons.utils.ThrowableManager
import com.gmail.echomskfan.persons.utils.fromIoToMain
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@InjectViewState
class VipsPresenter : MvpPresenter<IVipsView>() {

    @Inject
    lateinit var interactor: IInteractor

    // TODO refactor - to the base class
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    public override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        MApplication.getDaggerComponents().inject(this)
        loadVipsFromJsonToDb()
    }

    private fun loadVipsFromJsonToDb() {
        compositeDisposable.clear()
        compositeDisposable.add(
                loadVipsFromJsonToDbMappedToVipsVM()
                .subscribe(
                        { viewState.showVips(it) },
                        { ThrowableManager.process(it) }
                )
        )
    }

    @VisibleForTesting
    fun loadVipsFromJsonToDbMappedToVipsVM(): Single<List<VipVM>> {
        return interactor.copyVipsFromJsonToDb(MApplication.getAppContext())
                .map {
                    val vmList = mutableListOf<VipVM>()
                    it.forEach { vmList.add(VipVM.fromEntity(it)) }
                    vmList.toList()
                }
                .fromIoToMain()
    }

    fun itemNotificationIconClicked(item: VipVM): Completable {
        return interactor.switchVipNotificationById(MApplication.getAppContext(), item.url, item.notification)
    }

    fun itemFavIconClicked(item: VipVM): Completable {
        return interactor.switchVipFavById(MApplication.getAppContext(), item.url, item.fav)
    }

    fun itemClicked(item: VipVM) {
        viewState.showVip(item.url)
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }
}