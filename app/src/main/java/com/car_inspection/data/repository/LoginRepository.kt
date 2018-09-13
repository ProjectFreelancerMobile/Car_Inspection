package com.car_inspection.data.repository

import androidx.lifecycle.LiveData
import com.car_inspection.data.local.database.RealmManager
import com.car_inspection.data.model.login.Login
import com.car_inspection.data.remote.fetchdata.FetchLoginTask
import com.car_inspection.data.remote.service.ApiMainService
import com.toan_itc.core.architecture.AppExecutors
import com.toan_itc.core.architecture.Resource
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by ToanDev on 07/08/18.
 * Email:Huynhvantoan.itc@gmail.com
 */

@Singleton
class LoginRepository @Inject constructor(
        private val realmManager: RealmManager,
        private val appExecutors: AppExecutors,
        private val apiService: ApiMainService
) {

    fun login(userName: String, password: String): LiveData<Resource<Boolean>> {
        val fetchLoginTask = FetchLoginTask(apiService, userName, password)
        appExecutors.diskIO().execute(fetchLoginTask)
        return fetchLoginTask.getLiveData()
    }

    fun clearAll(){
        realmManager.clearAll()
    }
}