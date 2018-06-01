package com.gmail.echomskfan.persons.interactor

import android.content.Context
import com.gmail.echomskfan.persons.MApplication
import com.gmail.echomskfan.persons.data.VipDTO
import com.gmail.echomskfan.persons.interactor.parser.IParser
import io.reactivex.Single
import javax.inject.Inject

class Interactor : IInteractor {

    @Inject
    lateinit var parser: IParser

    init {
        MApplication.getDaggerComponents().inject(this)
    }

    override fun loadVips(context: Context): Single<List<VipDTO>> {
        return Single.fromCallable { parser.getVips(context) }
    }
}

