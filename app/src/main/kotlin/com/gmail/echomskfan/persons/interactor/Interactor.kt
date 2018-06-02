package com.gmail.echomskfan.persons.interactor

import android.content.Context
import com.gmail.echomskfan.persons.MApplication
import com.gmail.echomskfan.persons.data.db.PersonsDatabase
import com.gmail.echomskfan.persons.data.entity.VipEntity
import com.gmail.echomskfan.persons.interactor.parser.IParser
import io.reactivex.Single
import javax.inject.Inject

class Interactor : IInteractor {


    @Inject
    lateinit var parser: IParser

//    val vipsUodatedSubject: PublishSubject<Unit> = PublishSubject.create()

    init {
        MApplication.getDaggerComponents().inject(this)
    }

    override fun loadVipsFromJsonToDb(appContext: Context): Single<List<VipEntity>> {
        return Single.create {
            val db = PersonsDatabase.getInstance(appContext)
            val vips = parser.getVips(appContext)
            vips.forEach { db.getVipDao().insert(it) }
            it.onSuccess(vips)
        }
    }

    override fun loadVips(appContext: Context): Single<List<VipEntity>> {
        return Single.fromCallable { parser.getVips(appContext) }
    }
}

