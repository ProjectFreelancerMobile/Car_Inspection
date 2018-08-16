package com.car_inspection.data.repository

import com.car_inspection.data.local.database.RealmManager
import com.car_inspection.data.remote.service.ApiMainService
import com.toan_itc.core.architecture.AppExecutors
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by ToanDev on 07/08/18.
 * Email:Huynhvantoan.itc@gmail.com
 */

@Singleton
class MainRepository @Inject constructor(
        private val realmManager: RealmManager,
        private val appExecutors: AppExecutors,
        private val apiService: ApiMainService
) {

    fun initStepData(step: Int) = realmManager.initStepData(step)

    fun clearAll() = realmManager.clearAll()
}