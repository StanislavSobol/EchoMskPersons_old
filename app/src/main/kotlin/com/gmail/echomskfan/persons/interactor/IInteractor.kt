package com.gmail.echomskfan.persons.interactor

import android.content.Context
import com.gmail.echomskfan.persons.data.VipDTO
import io.reactivex.Single


interface IInteractor {

    fun loadVips(context: Context): Single<List<VipDTO>>
}
