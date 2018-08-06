package com.car_inspection.data.repository

import com.car_inspection.data.local.database.RealmManager
import com.car_inspection.data.remote.service.ApiMainService
import com.toan_itc.core.architecture.AppExecutors
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Created by ToanDev on 05/04/18.
 * Email:Huynhvantoan.itc@gmail.com
 */

@Singleton
class LoginRepository @Inject constructor(
        private val realmManager: RealmManager,
        private val appExecutors: AppExecutors,
        private val apiService: ApiMainService
) {

    fun clearAll(){
        realmManager.clearAll()
    }
}