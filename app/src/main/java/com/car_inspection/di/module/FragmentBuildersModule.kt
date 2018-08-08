package com.car_inspection.di.module

import com.car_inspection.di.FragmentScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by ToanDev on 28/2/18.
 * Email:Huynhvantoan.itc@gmail.com
 */

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeMainFragment(): MainFragment
}
