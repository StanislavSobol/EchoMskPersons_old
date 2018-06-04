package com.gmail.echomskfan.persons.interactor

import android.content.Context
import com.gmail.echomskfan.persons.data.entity.VipEntity
import io.reactivex.Completable
import io.reactivex.Single


interface IInteractor {

    fun copyVipsFromJsonToDb(appContext: Context): Single<List<VipEntity>>
    fun switchVipNotificationById(appContext: Context, url: String, oldNotification: Boolean): Completable
    fun switchVipFavById(appContext: Context, url: String, oldFav: Boolean): Completable
}
