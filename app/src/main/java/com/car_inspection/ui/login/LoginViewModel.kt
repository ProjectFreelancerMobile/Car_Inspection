package com.car_inspection.ui.login

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.car_inspection.data.repository.LoginRepository
import com.orhanobut.logger.Logger
import com.toan_itc.core.base.BaseViewModel
import javax.inject.Inject

class LoginViewModel
@Inject
internal constructor(private val loginRepository: LoginRepository) : BaseViewModel(), LifecycleObserver {
    private val TAG = "LoginViewModel"

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


    fun login(email: String, password: String) = loginRepository.login(email, password)

}