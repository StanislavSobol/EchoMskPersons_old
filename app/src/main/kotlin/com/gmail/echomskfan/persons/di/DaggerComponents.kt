package com.gmail.echomskfan.persons.di

import com.gmail.echomskfan.persons.ui.vips.VipsPresenter
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(DaggerModules::class)])
interface DaggerComponents {
    fun inject(vipsPresenter: VipsPresenter)
}