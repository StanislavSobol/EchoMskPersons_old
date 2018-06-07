package com.gmail.echomskfan.persons.ui.casts

import android.content.Context
import android.support.annotation.VisibleForTesting
import com.arellomobile.mvp.InjectViewState
import com.gmail.echomskfan.persons.MApplication
import com.gmail.echomskfan.persons.data.CastVM
import com.gmail.echomskfan.persons.interactor.IInteractor
import com.gmail.echomskfan.persons.ui.BasePresenter
import com.gmail.echomskfan.persons.utils.ThrowableManager
import com.gmail.echomskfan.persons.utils.fromIoToMain
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@InjectViewState
class CastsPresenter : BasePresenter<ICastsView>() {

    @Inject
    lateinit var interactor: IInteractor

    private var pageNum: Int = 1

    var url: String = ""

    public override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        MApplication.getDaggerComponents().inject(this)

        interactor.castsUpdatedSubject
                .fromIoToMain()
                .map { CastVM.fromEntitiesList(it) }
                .subscribe(
                        { viewState.addItems(it) },
                        { ThrowableManager.process(it) }
                )
                .unsubscribeOnDestroy()

        loadCastsFromDb()
    }

    private fun loadCastsFromDb() {
        loadCastsFromWebToDbMappedToCastsVM(MApplication.getAppContext(), pageNum)
                .subscribe(
                        {
                            viewState.addItems(it)
                            interactor.loadCastsFromWebToDbIfNeeded(MApplication.getAppContext(), url, pageNum)
                                    .subscribeOn(Schedulers.io())
                                    .subscribe()
                        },
                        {
                            ThrowableManager.process(it)
                        }
                )
                .unsubscribeOnDestroy()
    }


    @VisibleForTesting
    fun loadCastsFromWebToDbMappedToCastsVM(appContext: Context, pageNum: Int): Single<List<CastVM>> {
        return interactor.loadCastsFromDb(appContext, url, pageNum)
                .map { CastVM.fromEntitiesList(it) }
                .fromIoToMain()
    }

    fun itemFavIconClicked(item: CastVM) {
        interactor.switchCastFavById(MApplication.getAppContext(), item.fullTextURL, item.fav)
                .fromIoToMain()
                .subscribe({
                    item.fav = !item.fav
                    viewState.updateItem(item)
                }, {
                    ThrowableManager.process(it)
                })
                .unsubscribeOnDestroy()
    }
}