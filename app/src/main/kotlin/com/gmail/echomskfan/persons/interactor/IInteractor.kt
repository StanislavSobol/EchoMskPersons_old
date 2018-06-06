package com.gmail.echomskfan.persons.interactor

import android.content.Context
import com.gmail.echomskfan.persons.data.ItemDTO
import com.gmail.echomskfan.persons.data.entity.VipEntity
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject


interface IInteractor {

    val castsUpdatedSubject: PublishSubject<Unit>

    fun copyVipsFromJsonToDb(appContext: Context): Single<List<VipEntity>>
    fun switchVipNotificationById(appContext: Context, url: String, oldNotification: Boolean): Completable
    fun switchVipFavById(appContext: Context, url: String, oldFav: Boolean): Completable

    fun loadCastsFromDb(appContext: Context, vipUrl: String, pageNum: Int): Single<List<ItemDTO>>
    @Deprecated("loadCastsFromWebToDbIfNeeded")
    fun loadCastsFromWeb(appContext: Context, url: String, pageNum: Int): Single<List<ItemDTO>>

    fun loadCastsFromWebToDbIfNeeded(appContext: Context, castsUrl: String, pageNum: Int): Completable

    // fun loadCasts(appContext: Context, url: String, pageNum: Int): Single<List<ItemDTO>>
}
