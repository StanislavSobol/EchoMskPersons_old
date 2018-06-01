package com.gmail.echomskfan.persons.di

import com.gmail.echomskfan.persons.interactor.IInteractor
import com.gmail.echomskfan.persons.interactor.Interactor
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DaggerModules {
    @Provides
    @Singleton
    fun privideListInteractor(): IInteractor = Interactor()
}