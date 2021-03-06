package com.holker.smart.di.components

import android.app.Application
import com.holker.smart.SmartAd
import com.holker.smart.di.modules.ActivityModule
import com.holker.smart.di.modules.AppModule
import com.holker.smart.di.modules.FragmentModule
import com.holker.smart.di.modules.RepositoryModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AndroidSupportInjectionModule::class, AppModule::class, ActivityModule::class,
        RepositoryModule::class, FragmentModule::class]
)
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(app: SmartAd)
}