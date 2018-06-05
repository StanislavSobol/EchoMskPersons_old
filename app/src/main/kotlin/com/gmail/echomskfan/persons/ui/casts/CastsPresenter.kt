package com.gmail.echomskfan.persons.ui.casts

import android.content.Context
import android.support.annotation.VisibleForTesting
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.gmail.echomskfan.persons.MApplication
import com.gmail.echomskfan.persons.data.CastVM
import com.gmail.echomskfan.persons.interactor.IInteractor
import com.gmail.echomskfan.persons.utils.ThrowableManager
import com.gmail.echomskfan.persons.utils.fromIoToMain
import io.reactivex.Single
import javax.inject.Inject

@InjectViewState
class CastsPresenter : MvpPresenter<ICastsView>() {

    @Inject
    lateinit var interactor: IInteractor

    private lateinit var url: String
    private var pageNum: Int = 1

    fun setUrl(url: String) {
        this.url = url
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        MApplication.getDaggerComponents().inject(this)
        loadCastsFromDb()
    }

    private fun loadCastsFromDb() {
        loadCastsFromJsonToDbMappedToCastsVM(MApplication.getAppContext(), url, pageNum)
                .fromIoToMain()
                .subscribe(
                        {
                            viewState.showCasts(it)
                            loadCastsFromWebMappedToCastsVM(MApplication.getAppContext(), url, pageNum)
                                    .fromIoToMain()
                                    // interactor.loadCastsFromWeb(MApplication.getAppContext(), url, pageNum)
                                    .subscribe(
                                            {
                                                viewState.showCasts(it)
                                            },
                                            {
                                                ThrowableManager.process(it)
                                            })
                        },
                        {
                            ThrowableManager.process(it)
                        })
    }

    @VisibleForTesting
    fun loadCastsFromJsonToDbMappedToCastsVM(appContext: Context, url: String, pageNum: Int): Single<List<CastVM>> {
        return interactor.loadCastsFromDb(appContext, url, pageNum)
                .map {
                    val vMs = mutableListOf<CastVM>()
                    it.forEach { vMs.add(CastVM.fromEntity(it)) }
                    vMs
                }
    }

    @VisibleForTesting
    fun loadCastsFromWebMappedToCastsVM(appContext: Context, url: String, pageNum: Int): Single<List<CastVM>> {
        return interactor.loadCastsFromWeb(MApplication.getAppContext(), url, pageNum)
                .map {
                    val vMs = mutableListOf<CastVM>()
                    it.forEach { vMs.add(CastVM.fromEntity(it)) }
                    vMs
                }
    }
}