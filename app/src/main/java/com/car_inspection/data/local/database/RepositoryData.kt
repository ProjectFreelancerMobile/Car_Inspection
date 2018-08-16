package com.car_inspection.data.local.database

import com.car_inspection.data.model.StepOrinalModel

/**
 * Created by ToanDev on 28/2/18.
 * Email:Huynhvantoan.itc@gmail.com
 */

interface RepositoryData {

    fun initStepData(step: Int) : List<StepOrinalModel>

    fun clearAll()

    fun closeRealm()
}