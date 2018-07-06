package com.gmail.echomskfan.persons.interactor

import android.content.Context
import com.gmail.echomskfan.persons.MApplication
import com.gmail.echomskfan.persons.data.IData
import com.gmail.echomskfan.persons.data.ItemDTO
import com.gmail.echomskfan.persons.data.db.PersonsDatabase
import com.gmail.echomskfan.persons.data.entity.VipDetailsEntity
import com.gmail.echomskfan.persons.data.entity.VipEntity
import com.gmail.echomskfan.persons.interactor.parser.IParser
import com.gmail.echomskfan.persons.utils.ThrowableManager
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class Interactor : IInteractor {

    @Inject
    lateinit var parser: IParser

    override val castsOverSubject: PublishSubject<Boolean> = PublishSubject.create()
    override val castsUpdatedSubject: PublishSubject<List<ItemDTO>> = PublishSubject.create()

    init {
        MApplication.getDaggerComponents().inject(this)
    }

    override fun copyVipsFromJsonToDb(appContext: Context): Single<List<VipEntity>> {
        return Single.create {
            try {
                val vips = parser.getVips(appContext)
                vips.forEach {
                    val dao = PersonsDatabase.getInstance(appContext).getVipFavDao()
                    dao.insert(VipDetailsEntity(it.url))
                    val vipDetails = dao.getById(it.url)
                    vipDetails?.run {
                        it.fav = vipDetails.fav
                        it.notification = vipDetails.notification
                    }
                }
                it.onSuccess(vips)
            } catch (t: Throwable) {
                ThrowableManager.process(t)
                it.onError(t)
            }
        }
    }

    override fun switchVipNotificationById(appContext: Context, url: String, oldNotification: Boolean): Completable {
        return Completable.create {
            try {
                PersonsDatabase.getInstance(appContext).getVipFavDao().setNotificationById(url, !oldNotification)
                it.onComplete()
            } catch (t: Throwable) {
                ThrowableManager.process(t)
                it.onError(t)
            }
        }
    }

    override fun switchVipFavById(appContext: Context, url: String, oldFav: Boolean): Completable {
        return Completable.create {
            try {
                PersonsDatabase.getInstance(appContext).getVipFavDao().setFavById(url, !oldFav)
                it.onComplete()
            } catch (t: Throwable) {
                ThrowableManager.process(t)
                it.onError(t)
            }
        }
    }

    override fun loadCastsFromDb(appContext: Context, vipUrl: String, pageNum: Int): Single<List<ItemDTO>> {
        return Single.create {
            try {
                val casts = PersonsDatabase.getInstance(appContext).getCastDao().getForVipAndPage(vipUrl, pageNum)
                it.onSuccess(casts)
            } catch (t: Throwable) {
                ThrowableManager.process(t)
                it.onError(t)
            }
        }
    }

    override fun loadCastsFromWebToDbIfNeeded(appContext: Context, castsUrl: String, pageNum: Int): Completable {
        return Completable.create {
            try {
                val dao = PersonsDatabase.getInstance(appContext).getCastDao()
                val dbCasts = dao.getForVipAndPage(castsUrl, pageNum)
                val vip = parser.getVips(appContext).find { it.url == castsUrl }
                val webCasts = parser.getCasts(IData.getCastFullUrl(castsUrl, pageNum), vip!!, pageNum)

                if (webCasts.isEmpty()) {
                    castsOverSubject.onNext(true)
                } else if (castsListsAreDifferent(dbCasts, webCasts)) {
                    dao.clearForVipAndPage(castsUrl, pageNum)
                    webCasts.forEach { dao.insert(it) }
                    castsUpdatedSubject.onNext(webCasts)
                }

                it.onComplete()
            } catch (t: Throwable) {
                ThrowableManager.process(t)
                it.onError(t)
            }

        }
    }

    private fun castsListsAreDifferent(dbCasts: List<ItemDTO>, webCasts: List<ItemDTO>): Boolean {
        if (dbCasts.size != webCasts.size) {
            return true
        }

        dbCasts.forEach { it.fav = false }
        for (i in 0 until dbCasts.size) {
            if (dbCasts[i] != webCasts[i]) {
                return true
            }
        }

        return false
    }

    override fun switchCastFavById(appContext: Context, fullTextUrl: String, oldFav: Boolean): Completable {
        return Completable.create {
            try {
                PersonsDatabase.getInstance(appContext).getCastDao().setFavById(fullTextUrl, !oldFav)
                it.onComplete()
            } catch (t: Throwable) {
                ThrowableManager.process(t)
                it.onError(t)
            }
        }
    }
}

