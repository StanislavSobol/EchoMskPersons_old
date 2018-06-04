package com.gmail.echomskfan.persons.interactor

import android.content.Context
import com.gmail.echomskfan.persons.MApplication
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
            val vips = parser.getVips(appContext)
            vips.forEach {
                val dao = PersonsDatabase.getInstance(appContext).getVipFavDao()
                dao.insert(VipDetailsEntity(it.url))
                val vipDetails = dao.getByPk(it.url)
                it.fav = vipDetails.fav
                it.notification = vipDetails.notification
            }
            it.onSuccess(vips)
        }
    }

    override fun switchVipNotificationById(appContext: Context, url: String, oldNotification: Boolean): Completable {
        return Completable.create {
            try {
                val dao = PersonsDatabase.getInstance(appContext).getVipFavDao()
                dao.setNotificationByPk(url, !oldNotification)
                it.onComplete()
            } catch (t: Throwable) {
                ThrowableManager.process(t)
                it.onError(t)
            }
        }
    }

}

