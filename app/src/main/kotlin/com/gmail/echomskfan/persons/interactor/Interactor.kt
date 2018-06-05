package com.gmail.echomskfan.persons.interactor

import android.content.Context
import com.gmail.echomskfan.persons.MApplication
import com.gmail.echomskfan.persons.data.ItemDTO
import com.gmail.echomskfan.persons.data.db.PersonsDatabase
import com.gmail.echomskfan.persons.data.entity.VipDetailsEntity
import com.gmail.echomskfan.persons.data.entity.VipEntity
import com.gmail.echomskfan.persons.interactor.parser.IParser
import com.gmail.echomskfan.persons.utils.ThrowableManager
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class Interactor : IInteractor {

    @Inject
    lateinit var parser: IParser

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
                    val vipDetails = dao.getByPk(it.url)
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
                PersonsDatabase.getInstance(appContext).getVipFavDao().setNotificationByPk(url, !oldNotification)
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
                PersonsDatabase.getInstance(appContext).getVipFavDao().setFavByPk(url, !oldFav)
                it.onComplete()
            } catch (t: Throwable) {
                ThrowableManager.process(t)
                it.onError(t)
            }
        }
    }

    override fun loadCastsFromDb(appContext: Context, url: String, pageNum: Int): Single<List<ItemDTO>> {
        return Single.create {
            try {
                val casts = PersonsDatabase.getInstance(appContext).getCastDao().getAll()
                it.onSuccess(casts)
            } catch (t: Throwable) {
                ThrowableManager.process(t)
                it.onError(t)
            }
        }
    }

    override fun loadCastsFromWeb(appContext: Context, url: String, pageNum: Int): Single<List<ItemDTO>> {
        return Single.create {
            try {
                val vip = parser.getVips(appContext).find { it.url == url }
                val casts = parser.getCasts(url, vip!!)
                it.onSuccess(casts)
            } catch (t: Throwable) {
                ThrowableManager.process(t)
                it.onError(t)
            }
        }
    }

}

