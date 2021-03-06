package com.holker.smart.di.modules

import com.holker.smart.di.ActivityScope
import com.holker.smart.functionalities.create_advertising.CreateAdvertisingActivity
import com.holker.smart.functionalities.create_device.CreateDeviceActivity
import com.holker.smart.functionalities.login.LoginActivity
import com.holker.smart.functionalities.main.MainActivity
import com.holker.smart.functionalities.sign_up.SignUpActivity
import com.holker.smart.functionalities.start.StartActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun contributeStartActivity(): StartActivity

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun contributesMainActivity(): MainActivity

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun contributesLoginActivity(): LoginActivity

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun contributesSignUpActivity(): SignUpActivity

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun contributesCreateDeviceActivity(): CreateDeviceActivity

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun contributesCreateAdvertisingActivity(): CreateAdvertisingActivity
}