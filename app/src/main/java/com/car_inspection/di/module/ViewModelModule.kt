package com.car_inspection.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.car_inspection.di.ViewModelFactory
import com.car_inspection.di.key.ViewModelKey
import com.car_inspection.ui.recorddefault.RecordViewModel
import com.car_inspection.ui.recordotg.RecordOTGViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by ToanDev on 28/2/18.
 * Email:Huynhvantoan.itc@gmail.com
 */
@Suppress("unused")
@Module
internal abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(RecordViewModel::class)
    internal abstract fun bindRecordViewModel(mainViewModel: RecordViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RecordOTGViewModel::class)
    internal abstract fun bindRecordOTGViewModel(mainOTGViewModel: RecordOTGViewModel): ViewModel

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}
