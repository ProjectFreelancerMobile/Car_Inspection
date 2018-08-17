package com.car_inspection.ui.step

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.car_inspection.data.repository.MainRepository
import com.orhanobut.logger.Logger
import com.toan_itc.core.base.BaseViewModel
import javax.inject.Inject

class StepViewModel
@Inject
internal constructor(private val mainRepository: MainRepository) : BaseViewModel(), LifecycleObserver {
    private val TAG = "StepViewModel"

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        Logger.d(TAG + "ON_DESTROY")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        Logger.d(TAG + "ON_START")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        Logger.d(TAG + "ON_STOP")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        Logger.d(TAG + "ON_RESUME")
    }


    fun initStepData(step: Int) = mainRepository.initStepData(step)

    fun clearAll() = mainRepository.clearAll()
}