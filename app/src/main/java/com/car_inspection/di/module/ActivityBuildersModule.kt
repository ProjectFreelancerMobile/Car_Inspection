package com.car_inspection.di.module

import com.car_inspection.di.ActivityScope
import com.car_inspection.ui.activity.MainActivity
import com.car_inspection.ui.activity.StepActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by ToanDev on 28/2/18.
 * Email:Huynhvantoan.itc@gmail.com
 */
@Suppress("unused")
@Module
abstract class ActivityBuildersModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [(FragmentBuildersModule::class)])
    internal abstract fun contributeMainActivity(): MainActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [(FragmentBuildersModule::class)])
    internal abstract fun contributeStepActivity(): StepActivity
}
