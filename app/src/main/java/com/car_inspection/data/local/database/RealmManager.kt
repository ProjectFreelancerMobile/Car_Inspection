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

    override fun initStepData(step : Int) :List<StepOrinalModel> {
        var listStep = mutableListOf<StepOrinalModel>()
        getDefaultInstance().executeTransaction { realm ->
           realm.where(StepOrinalModel::class.java).findAll().let {
                if(it !=null && it.size >0 ){
                    Logger.e("stepOrinalModels="+it.toString())
                    listStep = realm.copyFromRealm(it)
                }
                else{
                    var size = 5
                    if (step % 2 == 0)
                        size = 4
                    for (i in 1..size) {
                        val stepmodify = StepOrinalModel()
                        stepmodify.step = "$step"
                        stepmodify.subStep = "$step." + i
                        stepmodify.subStepTitle1 = "bên ngoài xe"
                        stepmodify.subStepTitle2 = "bên trái trước"
                        stepmodify.subStepTitle3 = "bên ngoài cửa xe"
                        listStep.add(stepmodify)
                    }
                    Logger.e("stepOrinalModels="+listStep.toString())
                    realm.copyToRealmOrUpdate(listStep)
                    listStep
                }
            }
        }
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
