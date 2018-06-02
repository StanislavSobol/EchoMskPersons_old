package com.gmail.echomskfan.persons.interactor

import android.content.Context
import com.gmail.echomskfan.persons.data.entity.VipEntity
import io.reactivex.Single


interface IInteractor {

    fun loadVipsFromJsonToDb(appContext: Context): Single<List<VipEntity>>

    fun loadVips(appContext: Context): Single<List<VipEntity>>
}
