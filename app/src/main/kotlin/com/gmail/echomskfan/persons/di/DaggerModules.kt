package com.gmail.echomskfan.persons.di

import com.gmail.echomskfan.persons.interactor.IInteractor
import com.gmail.echomskfan.persons.interactor.Interactor
import com.gmail.echomskfan.persons.interactor.parser.IParser
import com.gmail.echomskfan.persons.interactor.parser.Parser
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DaggerModules {

    @Provides
    @Singleton
    fun privideInteractor(): IInteractor = Interactor()

    @Provides
    @Singleton
    fun privideParser(): IParser = Parser()
}