package com.car_inspection.di.module

import com.car_inspection.di.FragmentScope
import com.car_inspection.ui.main.MainFragment
import com.car_inspection.ui.recorddefault.RecordFragment
import com.car_inspection.ui.recordotg.RecordOTGFragment
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

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeRecordFragment(): RecordFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeRecordOTGFragment(): RecordOTGFragment
}
