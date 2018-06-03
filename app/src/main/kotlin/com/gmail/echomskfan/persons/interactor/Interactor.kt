package com.gmail.echomskfan.persons.interactor

import android.content.Context
import com.gmail.echomskfan.persons.MApplication
import com.gmail.echomskfan.persons.data.db.PersonsDatabase
import com.gmail.echomskfan.persons.data.entity.VipEntity
import com.gmail.echomskfan.persons.data.entity.VipFavEntity
import com.gmail.echomskfan.persons.interactor.parser.IParser
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
            val vips = parser.getVips(appContext).toMutableList()
            vips.forEach {
                val dao = PersonsDatabase.getInstance(appContext).getVipFavDao()
                dao.insert(VipFavEntity(it.url))
                val vipFav = dao.getByPk(it.url)
                it.fav = vipFav.fav
            }
            it.onSuccess(vips)
        }
    }
}

