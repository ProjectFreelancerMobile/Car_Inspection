package com.car_inspection.data.local.database

import com.car_inspection.app.Constants
import com.car_inspection.data.local.prefs.PreferenceManager
import com.car_inspection.data.model.StepOrinalModel
import com.orhanobut.logger.Logger
import io.realm.Realm
import io.realm.Realm.getDefaultInstance
import io.realm.RealmConfiguration

@Suppress("ReplaceCallWithBinaryOperator")
/**
 * Created by ToanDev on 07/06/18.
 * Email:Huynhvantoan.itc@gmail.com
 */

class RealmManager : RepositoryData {
    private val mRealmConfig: RealmConfiguration = RealmConfiguration.Builder()
            .name(Constants.DB_Realm)
            .deleteRealmIfMigrationNeeded()
            .migration(Migration())
            .schemaVersion(Constants.RealmVersion)
            .build()

    init {
        Realm.setDefaultConfiguration(mRealmConfig)
    }

    override fun initStepData(step: Int): List<StepOrinalModel> {
        var listStep = mutableListOf<StepOrinalModel>()
        /*getDefaultInstance().executeTransaction { realm ->
            realm.where(StepOrinalModel::class.java).equalTo("step", step.toString()).findAll().let {
                if (it != null && it.size > 0) {
                    Logger.e("stepOrinalModels=" + it.toString())
                    listStep = realm.copyFromRealm(it)
                } else {
                    var size = 5
//                    if (step == 2)
//                        size = 6
//                    else if (step % 2 == 0)
//                        size = 4
//                    else size = 5
                    for (i in 1..size) {
                        val stepOrinal = StepOrinalModel()
                        stepOrinal.step = "$step"
                        when (step) {
                            2 -> stepOrinal.stepTitle = "Kiểm tra bên ngoài xe"
                            3 -> stepOrinal.stepTitle = "Kiểm tra dưới gầm xe"
                            4 -> stepOrinal.stepTitle = "Kiểm tra khoang động cơ"
                            5 -> stepOrinal.stepTitle = "Kiểm tra động cơ và hộp số"
                            6 -> stepOrinal.stepTitle = "Kiểm tra bên trong xe"
                            7 -> stepOrinal.stepTitle = "Kiểm tra lái thử xe"
                        }
                        stepOrinal.subStep = "$step." + i
                        stepOrinal.subStepTitle1 = "bên ngoài xe"
                        stepOrinal.subStepTitle2 = "bên trái trước"
                        when (i){
                            1-> stepOrinal.subStepTitle3 = "phía ngoài cửa xe"
                            2-> stepOrinal.subStepTitle3 = "Kính xe"
                            3-> stepOrinal.subStepTitle3 = "Bánh xe"
                            4-> stepOrinal.subStepTitle3 = "Kính chiếu hậu"
                            5-> stepOrinal.subStepTitle3 = "Độ kín khít"
                        }

                        listStep.add(stepOrinal)
                    }
                    Logger.e("stepOrinalModels=" + listStep.toString())
                    realm.copyToRealmOrUpdate(listStep)
                }
            }
        }*/
        var size = 5
        if (step % 2 == 0)
            size = 4
        for (i in 1..size) {
            val stepOrinal = StepOrinalModel()
            stepOrinal.step = "$step"
            when (step) {
                2 -> stepOrinal.stepTitle = "Kiểm tra bên ngoài xe"
                3 -> stepOrinal.stepTitle = "Kiểm tra dưới gầm xe"
                4 -> stepOrinal.stepTitle = "Kiểm tra khoang động cơ"
                5 -> stepOrinal.stepTitle = "Kiểm tra động cơ và hộp số"
                6 -> stepOrinal.stepTitle = "Kiểm tra bên trong xe"
                7 -> stepOrinal.stepTitle = "Kiểm tra lái thử xe"
            }
            stepOrinal.subStep = "$step." + i
            stepOrinal.subStepTitle1 = "bên ngoài xe"
            stepOrinal.subStepTitle2 = "bên trái trước"
            stepOrinal.subStepTitle3 = "bên ngoài cửa xe"
            listStep.add(stepOrinal)
        }
        Logger.e("stepOrinalModels=" + listStep.toString())
        return listStep
    }

    override fun clearAll() {
        PreferenceManager.clear()
    }


    override fun closeRealm() {
        getDefaultInstance().apply {
            if (!isClosed) {
                Logger.e("closeRealm")
                close()
            }
        }
    }

}
