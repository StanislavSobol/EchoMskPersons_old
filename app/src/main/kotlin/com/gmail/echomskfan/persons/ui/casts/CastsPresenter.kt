package com.gmail.echomskfan.persons.ui.casts

import android.content.Context
import android.support.annotation.VisibleForTesting
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.gmail.echomskfan.persons.MApplication
import com.gmail.echomskfan.persons.data.CastVM
import com.gmail.echomskfan.persons.data.ItemDTO
import com.gmail.echomskfan.persons.data.db.PersonsDatabase
import com.gmail.echomskfan.persons.interactor.IInteractor
import com.gmail.echomskfan.persons.utils.ThrowableManager
import com.gmail.echomskfan.persons.utils.fromIoToMain
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
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

        interactor.castsUpdatedSubject.subscribeOn(io.reactivex.schedulers.Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        interactor.castsUpdatedSubject.fromIoToMain().subscribe(
                {
                    loadCastsFromDb(webRequest = false)
                },
                {
                    ThrowableManager.process(it)
                }
        )

        loadCastsFromDb(webRequest = true)
    }

    private fun loadCastsFromDb(webRequest: Boolean) {
        loadCastsFromJsonToDbMappedToCastsVM(MApplication.getAppContext(), url, pageNum).fromIoToMain().subscribe(
                {
                    viewState.showCasts(it)
                    if (webRequest) {
                        interactor.loadCastsFromWebToDbIfNeeded(MApplication.getAppContext(), url, pageNum)
                                .subscribeOn(Schedulers.io())
                                .subscribe()
                    }
                },
                {
                    ThrowableManager.process(it)
                }
        )
    }

    private fun loadCastsFromDb_old() {
        var dbItems = listOf<ItemDTO>()
        interactor.loadCastsFromDb(MApplication.getAppContext(), url, pageNum)
                .map {
                    dbItems = it
                    val vMs = mutableListOf<CastVM>()
                    it.forEach { vMs.add(CastVM.fromEntity(it)) }
                    vMs
                }
                .fromIoToMain()
                .subscribe(
                        {
                            viewState.showCasts(it)
                            interactor.loadCastsFromWeb(MApplication.getAppContext(), url, pageNum)
                                    .subscribe(
                                            {
                                                if (listAreDifferent(dbItems, it)) {
                                                    val vMs = mutableListOf<CastVM>()
                                                    it.forEach { vMs.add(CastVM.fromEntity(it)) }
                                                    viewState.showCasts(vMs)

                                                    PersonsDatabase.getInstance(MApplication.getAppContext()).getCastDao()


                                                }
                                            },
                                            {
                                                ThrowableManager.process(it)
                                            }
                                    )
                        },
                        {
                            ThrowableManager.process(it)
                        })

        /*
        loadCastsFromJsonToDbMappedToCastsVM(MApplication.getAppContext(), url, pageNum)
                .fromIoToMain()
                .subscribe(
                        {
                            viewState.showCasts(it)

                         //   val webItems = interactor.loadCastsFromWeb(MApplication.getAppContext(), url, pageNum)


//                            loadCastsFromWebMappedToCastsVM(MApplication.getAppContext(), url, pageNum)
//                                    .fromIoToMain()
//                                    // interactor.loadCastsFromWeb(MApplication.getAppContext(), url, pageNum)
//                                    .subscribe(
//                                            {
//                                                viewState.showCasts(it)
//                                            },
//                                            {
//                                                ThrowableManager.process(it)
//                                            })
                        },
                        {
                            ThrowableManager.process(it)
                        })
        */
    }

    private fun listAreDifferent(dbItems: List<ItemDTO>, webItems: List<ItemDTO>?): Boolean {
        return true
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