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
            realm.where(StepOrinalModel::class.java).equalTo("step", "$step").findAll().let {
                if (it != null && it.size > 0) {
                    Logger.e("stepOrinalModels=" + it.toString())
                    listStep = realm.copyFromRealm(it)
                } else {
                    val size = 5
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

        when (step) {
            2 -> listStep = dataStep2()
            3 -> listStep = dataStep3()
            4 -> listStep = dataStep4()
            5 -> listStep = dataStep5()
            6 -> listStep = dataStep6()
            7->listStep = dataStep7()
        }

        return listStep
    }

    fun dataTest(step: Int): MutableList<StepOrinalModel> {
        var listStep = mutableListOf<StepOrinalModel>()
        val size = 5
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
            when (i) {
                1 -> stepOrinal.subStepTitle3 = "phía ngoài cửa xe"
                2 -> stepOrinal.subStepTitle3 = "Kính xe"
                3 -> stepOrinal.subStepTitle3 = "Bánh xe"
                4 -> stepOrinal.subStepTitle3 = "Kính chiếu hậu"
                5 -> stepOrinal.subStepTitle3 = "Độ kín khít"
            }

            listStep.add(stepOrinal)
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

    fun dataStep2(): MutableList<StepOrinalModel> {
        var listSteps = mutableListOf<StepOrinalModel>()
        // step 2
        var stepOrinalStep2_1 = StepOrinalModel()
        stepOrinalStep2_1.step = "2"
        stepOrinalStep2_1.stepTitle = "Kiểm tra bên ngoài xe"
        stepOrinalStep2_1.subStep = "2.1"
        stepOrinalStep2_1.subStepTitle1 = "Bên ngoài xe"
        stepOrinalStep2_1.subStepTitle2 = "Bên trái trước"
        stepOrinalStep2_1.subStepTitle3 = "Phía ngoài cửa"
        listSteps.add(stepOrinalStep2_1)

        var stepOrinalStep2_2 = StepOrinalModel()
        stepOrinalStep2_2.step = "2"
        stepOrinalStep2_2.stepTitle = "Kiểm tra bên ngoài xe"
        stepOrinalStep2_2.subStep = "2.1"
        stepOrinalStep2_2.subStepTitle1 = "Bên ngoài xe"
        stepOrinalStep2_2.subStepTitle2 = "Bên trái trước"
        stepOrinalStep2_2.subStepTitle3 = "Kính xe"
        listSteps.add(stepOrinalStep2_2)

        var stepOrinalStep2_3 = StepOrinalModel()
        stepOrinalStep2_3.step = "2"
        stepOrinalStep2_3.stepTitle = "Kiểm tra bên ngoài xe"
        stepOrinalStep2_3.subStep = "2.1"
        stepOrinalStep2_3.subStepTitle1 = "Bên ngoài xe"
        stepOrinalStep2_3.subStepTitle2 = "Bên trái trước"
        stepOrinalStep2_3.subStepTitle3 = "Vè xe"
        listSteps.add(stepOrinalStep2_3)

        var stepOrinalStep2_4 = StepOrinalModel()
        stepOrinalStep2_4.step = "2"
        stepOrinalStep2_4.stepTitle = "Kiểm tra bên ngoài xe"
        stepOrinalStep2_4.subStep = "2.1"
        stepOrinalStep2_4.subStepTitle1 = "Bên ngoài xe"
        stepOrinalStep2_4.subStepTitle2 = "Bên trái trước"
        stepOrinalStep2_4.subStepTitle3 = "Bánh xe"
        listSteps.add(stepOrinalStep2_4)

        var stepOrinalStep2_5 = StepOrinalModel()
        stepOrinalStep2_5.step = "2"
        stepOrinalStep2_5.stepTitle = "Kiểm tra bên ngoài xe"
        stepOrinalStep2_5.subStep = "2.1"
        stepOrinalStep2_5.subStepTitle1 = "Bên ngoài xe"
        stepOrinalStep2_5.subStepTitle2 = "Bên trái trước"
        stepOrinalStep2_5.subStepTitle3 = "Kính chiếu hậu"
        listSteps.add(stepOrinalStep2_5)

        var stepOrinalStep2_6 = StepOrinalModel()
        stepOrinalStep2_6.step = "2"
        stepOrinalStep2_6.stepTitle = "Kiểm tra bên ngoài xe"
        stepOrinalStep2_6.subStep = "2.1"
        stepOrinalStep2_6.subStepTitle1 = "Bên ngoài xe"
        stepOrinalStep2_6.subStepTitle2 = "Bên trái trước"
        stepOrinalStep2_6.subStepTitle3 = "Độ kín khít"
        listSteps.add(stepOrinalStep2_6)

        var stepOrinalStep2_7 = StepOrinalModel()
        stepOrinalStep2_7.step = "2"
        stepOrinalStep2_7.stepTitle = "Kiểm tra bên ngoài xe"
        stepOrinalStep2_7.subStep = "2.2"
        stepOrinalStep2_7.subStepTitle1 = "bên ngoài xe"
        stepOrinalStep2_7.subStepTitle2 = "Phía trước"
        stepOrinalStep2_7.subStepTitle3 = "Nắp capo"
        listSteps.add(stepOrinalStep2_7)

        var stepOrinalStep2_8 = StepOrinalModel()
        stepOrinalStep2_8.step = "2"
        stepOrinalStep2_8.stepTitle = "Kiểm tra bên ngoài xe"
        stepOrinalStep2_8.subStep = "2.2"
        stepOrinalStep2_8.subStepTitle1 = "bên ngoài xe"
        stepOrinalStep2_8.subStepTitle2 = "Phía trước"
        stepOrinalStep2_8.subStepTitle3 = "Kính xe"
        listSteps.add(stepOrinalStep2_8)

        var stepOrinalStep2_9 = StepOrinalModel()
        stepOrinalStep2_9.step = "2"
        stepOrinalStep2_9.stepTitle = "Kiểm tra bên ngoài xe"
        stepOrinalStep2_9.subStep = "2.2"
        stepOrinalStep2_9.subStepTitle1 = "bên ngoài xe"
        stepOrinalStep2_9.subStepTitle2 = "Phía trước"
        stepOrinalStep2_9.subStepTitle3 = "Ba đờ sốc"
        listSteps.add(stepOrinalStep2_9)

        var stepOrinalStep2_10 = StepOrinalModel()
        stepOrinalStep2_10.step = "2"
        stepOrinalStep2_10.stepTitle = "Kiểm tra bên ngoài xe"
        stepOrinalStep2_10.subStep = "2.2"
        stepOrinalStep2_10.subStepTitle1 = "bên ngoài xe"
        stepOrinalStep2_10.subStepTitle2 = "Phía trước"
        stepOrinalStep2_10.subStepTitle3 = "Lưới tán nhiệt"
        listSteps.add(stepOrinalStep2_10)

        var stepOrinalStep2_11 = StepOrinalModel()
        stepOrinalStep2_11.step = "2"
        stepOrinalStep2_11.stepTitle = "Kiểm tra bên ngoài xe"
        stepOrinalStep2_11.subStep = "2.2"
        stepOrinalStep2_11.subStepTitle1 = "bên ngoài xe"
        stepOrinalStep2_11.subStepTitle2 = "Phía trước"
        stepOrinalStep2_11.subStepTitle3 = "Các đèn xe phía trước"
        listSteps.add(stepOrinalStep2_11)

        var stepOrinalStep2_12 = StepOrinalModel()
        stepOrinalStep2_12.step = "2"
        stepOrinalStep2_12.stepTitle = "Kiểm tra bên ngoài xe"
        stepOrinalStep2_12.subStep = "2.2"
        stepOrinalStep2_12.subStepTitle1 = "bên ngoài xe"
        stepOrinalStep2_12.subStepTitle2 = "Phía trước"
        stepOrinalStep2_12.subStepTitle3 = "Độ kín khít"
        listSteps.add(stepOrinalStep2_12)

        var stepOrinalStep2_13 = StepOrinalModel()
        stepOrinalStep2_13.step = "2"
        stepOrinalStep2_13.stepTitle = "Kiểm tra bên ngoài xe"
        stepOrinalStep2_13.subStep = "2.3"
        stepOrinalStep2_13.subStepTitle1 = "bên ngoài xe"
        stepOrinalStep2_13.subStepTitle2 = "Bên phải trước"
        stepOrinalStep2_13.subStepTitle3 = "Phía ngoài cửa xe"
        listSteps.add(stepOrinalStep2_13)

        var stepOrinalStep2_14 = StepOrinalModel()
        stepOrinalStep2_14.step = "2"
        stepOrinalStep2_14.stepTitle = "Kiểm tra bên ngoài xe"
        stepOrinalStep2_14.subStep = "2.3"
        stepOrinalStep2_14.subStepTitle1 = "bên ngoài xe"
        stepOrinalStep2_14.subStepTitle2 = "Bên phải trước"
        stepOrinalStep2_14.subStepTitle3 = "Kính xe"
        listSteps.add(stepOrinalStep2_14)

        var stepOrinalStep2_15 = StepOrinalModel()
        stepOrinalStep2_15.step = "2"
        stepOrinalStep2_15.stepTitle = "Kiểm tra bên ngoài xe"
        stepOrinalStep2_15.subStep = "2.3"
        stepOrinalStep2_15.subStepTitle1 = "bên ngoài xe"
        stepOrinalStep2_15.subStepTitle2 = "Bên phải trước"
        stepOrinalStep2_15.subStepTitle3 = "Vè xe"
        listSteps.add(stepOrinalStep2_15)

        var stepOrinalStep2_16 = StepOrinalModel()
        stepOrinalStep2_16.step = "2"
        stepOrinalStep2_16.stepTitle = "Kiểm tra bên ngoài xe"
        stepOrinalStep2_16.subStep = "2.3"
        stepOrinalStep2_16.subStepTitle1 = "bên ngoài xe"
        stepOrinalStep2_16.subStepTitle2 = "Bên phải trước"
        stepOrinalStep2_16.subStepTitle3 = "Bánh xe (thông số lốp, độ mòn gái, mâm)"
        listSteps.add(stepOrinalStep2_16)

        var stepOrinalStep2_17 = StepOrinalModel()
        stepOrinalStep2_17.step = "2"
        stepOrinalStep2_17.stepTitle = "Kiểm tra bên ngoài xe"
        stepOrinalStep2_17.subStep = "2.3"
        stepOrinalStep2_17.subStepTitle1 = "bên ngoài xe"
        stepOrinalStep2_17.subStepTitle2 = "Bên phải trước"
        stepOrinalStep2_17.subStepTitle3 = "Kính chiếu hậu"
        listSteps.add(stepOrinalStep2_17)

        var stepOrinalStep2_18 = StepOrinalModel()
        stepOrinalStep2_18.step = "2"
        stepOrinalStep2_18.stepTitle = "Kiểm tra bên ngoài xe"
        stepOrinalStep2_18.subStep = "2.3"
        stepOrinalStep2_18.subStepTitle1 = "bên ngoài xe"
        stepOrinalStep2_18.subStepTitle2 = "Bên phải trước"
        stepOrinalStep2_18.subStepTitle3 = "Độ kín khít"
        listSteps.add(stepOrinalStep2_18)

        var stepOrinalStep2_19 = StepOrinalModel()
        stepOrinalStep2_19.step = "2"
        stepOrinalStep2_19.stepTitle = "Kiểm tra bên ngoài xe"
        stepOrinalStep2_19.subStep = "2.4"
        stepOrinalStep2_19.subStepTitle1 = "bên ngoài xe"
        stepOrinalStep2_19.subStepTitle2 = "Bên phải sau"
        stepOrinalStep2_19.subStepTitle3 = "Phía ngoài cửa xe"
        listSteps.add(stepOrinalStep2_19)

        var stepOrinalStep2_20 = StepOrinalModel()
        stepOrinalStep2_20.step = "2"
        stepOrinalStep2_20.stepTitle = "Kiểm tra bên ngoài xe"
        stepOrinalStep2_20.subStep = "2.4"
        stepOrinalStep2_20.subStepTitle1 = "bên ngoài xe"
        stepOrinalStep2_20.subStepTitle2 = "Bên phải sau"
        stepOrinalStep2_20.subStepTitle3 = "Kính xe"
        listSteps.add(stepOrinalStep2_20)

        var stepOrinalStep2_21 = StepOrinalModel()
        stepOrinalStep2_21.step = "2"
        stepOrinalStep2_21.stepTitle = "Kiểm tra bên ngoài xe"
        stepOrinalStep2_21.subStep = "2.4"
        stepOrinalStep2_21.subStepTitle1 = "bên ngoài xe"
        stepOrinalStep2_21.subStepTitle2 = "Bên phải sau"
        stepOrinalStep2_21.subStepTitle3 = "Vè xe"
        listSteps.add(stepOrinalStep2_21)

        var stepOrinalStep2_22 = StepOrinalModel()
        stepOrinalStep2_22.step = "2"
        stepOrinalStep2_22.stepTitle = "Kiểm tra bên ngoài xe"
        stepOrinalStep2_22.subStep = "2.4"
        stepOrinalStep2_22.subStepTitle1 = "bên ngoài xe"
        stepOrinalStep2_22.subStepTitle2 = "Bên phải sau"
        stepOrinalStep2_22.subStepTitle3 = "Bánh xe"
        listSteps.add(stepOrinalStep2_22)

        var stepOrinalStep2_23 = StepOrinalModel()
        stepOrinalStep2_23.step = "2"
        stepOrinalStep2_23.stepTitle = "Kiểm tra bên ngoài xe"
        stepOrinalStep2_23.subStep = "2.4"
        stepOrinalStep2_23.subStepTitle1 = "bên ngoài xe"
        stepOrinalStep2_23.subStepTitle2 = "Bên phải sau"
        stepOrinalStep2_23.subStepTitle3 = "Độ kín khít"
        listSteps.add(stepOrinalStep2_23)

        var stepOrinalStep2_24 = StepOrinalModel()
        stepOrinalStep2_24.step = "2"
        stepOrinalStep2_24.stepTitle = "Kiểm tra bên ngoài xe"
        stepOrinalStep2_24.subStep = "2.5"
        stepOrinalStep2_24.subStepTitle1 = "bên ngoài xe"
        stepOrinalStep2_24.subStepTitle2 = "Phía sau"
        stepOrinalStep2_24.subStepTitle3 = "Nắp cốp"
        listSteps.add(stepOrinalStep2_24)

        var stepOrinalStep2_25 = StepOrinalModel()
        stepOrinalStep2_25.step = "2"
        stepOrinalStep2_25.stepTitle = "Kiểm tra bên ngoài xe"
        stepOrinalStep2_25.subStep = "2.5"
        stepOrinalStep2_25.subStepTitle1 = "bên ngoài xe"
        stepOrinalStep2_25.subStepTitle2 = "Phía sau"
        stepOrinalStep2_25.subStepTitle3 = "Kính xe"
        listSteps.add(stepOrinalStep2_25)

        var stepOrinalStep2_26 = StepOrinalModel()
        stepOrinalStep2_26.step = "2"
        stepOrinalStep2_26.stepTitle = "Kiểm tra bên ngoài xe"
        stepOrinalStep2_26.subStep = "2.5"
        stepOrinalStep2_26.subStepTitle1 = "bên ngoài xe"
        stepOrinalStep2_26.subStepTitle2 = "Phía sau"
        stepOrinalStep2_26.subStepTitle3 = "Ba đờ sốc"
        listSteps.add(stepOrinalStep2_26)

        var stepOrinalStep2_27 = StepOrinalModel()
        stepOrinalStep2_27.step = "2"
        stepOrinalStep2_27.stepTitle = "Kiểm tra bên ngoài xe"
        stepOrinalStep2_27.subStep = "2.5"
        stepOrinalStep2_27.subStepTitle1 = "bên ngoài xe"
        stepOrinalStep2_27.subStepTitle2 = "Phía sau"
        stepOrinalStep2_27.subStepTitle3 = "Các đèn xe phía sau"
        listSteps.add(stepOrinalStep2_27)

        var stepOrinalStep2_28 = StepOrinalModel()
        stepOrinalStep2_28.step = "2"
        stepOrinalStep2_28.stepTitle = "Kiểm tra bên ngoài xe"
        stepOrinalStep2_28.subStep = "2.5"
        stepOrinalStep2_28.subStepTitle1 = "bên ngoài xe"
        stepOrinalStep2_28.subStepTitle2 = "Phía sau"
        stepOrinalStep2_28.subStepTitle3 = "Độ kín khít"
        listSteps.add(stepOrinalStep2_28)

        var stepOrinalStep2_29 = StepOrinalModel()
        stepOrinalStep2_29.step = "2"
        stepOrinalStep2_29.stepTitle = "Kiểm tra bên ngoài xe"
        stepOrinalStep2_29.subStep = "2.6"
        stepOrinalStep2_29.subStepTitle1 = "bên ngoài xe"
        stepOrinalStep2_29.subStepTitle2 = "Bên trái sau"
        stepOrinalStep2_29.subStepTitle3 = "Phía ngoài cửa xe"
        listSteps.add(stepOrinalStep2_29)

        var stepOrinalStep2_30 = StepOrinalModel()
        stepOrinalStep2_30.step = "2"
        stepOrinalStep2_30.stepTitle = "Kiểm tra bên ngoài xe"
        stepOrinalStep2_30.subStep = "2.6"
        stepOrinalStep2_30.subStepTitle1 = "bên ngoài xe"
        stepOrinalStep2_30.subStepTitle2 = "Bên trái sau"
        stepOrinalStep2_30.subStepTitle3 = "Kính xe"
        listSteps.add(stepOrinalStep2_30)

        var stepOrinalStep2_31 = StepOrinalModel()
        stepOrinalStep2_31.step = "2"
        stepOrinalStep2_31.stepTitle = "Kiểm tra bên ngoài xe"
        stepOrinalStep2_31.subStep = "2.6"
        stepOrinalStep2_31.subStepTitle1 = "bên ngoài xe"
        stepOrinalStep2_31.subStepTitle2 = "Bên trái sau"
        stepOrinalStep2_31.subStepTitle3 = "Vè xe"
        listSteps.add(stepOrinalStep2_31)

        var stepOrinalStep2_32 = StepOrinalModel()
        stepOrinalStep2_32.step = "2"
        stepOrinalStep2_32.stepTitle = "Kiểm tra bên ngoài xe"
        stepOrinalStep2_32.subStep = "2.6"
        stepOrinalStep2_32.subStepTitle1 = "bên ngoài xe"
        stepOrinalStep2_32.subStepTitle2 = "Bên trái sau"
        stepOrinalStep2_32.subStepTitle3 = "Bánh xe"
        listSteps.add(stepOrinalStep2_32)

        var stepOrinalStep2_33 = StepOrinalModel()
        stepOrinalStep2_33.step = "2"
        stepOrinalStep2_33.stepTitle = "Kiểm tra bên ngoài xe"
        stepOrinalStep2_33.subStep = "2.6"
        stepOrinalStep2_33.subStepTitle1 = "bên ngoài xe"
        stepOrinalStep2_33.subStepTitle2 = "Bên trái sau"
        stepOrinalStep2_33.subStepTitle3 = "Độ kín khít"
        listSteps.add(stepOrinalStep2_33)

        var stepOrinalStep2_34 = StepOrinalModel()
        stepOrinalStep2_33.step = "2"
        stepOrinalStep2_33.stepTitle = "Kiểm tra bên ngoài xe"
        stepOrinalStep2_33.subStep = "2.7"
        stepOrinalStep2_33.subStepTitle1 = "bên ngoài xe"
        stepOrinalStep2_33.subStepTitle2 = "Phía trên"
        stepOrinalStep2_33.subStepTitle3 = "Bên ngoài trần xe"
        listSteps.add(stepOrinalStep2_33)
        return listSteps
    }

    fun dataStep3(): MutableList<StepOrinalModel> {
        var listSteps = mutableListOf<StepOrinalModel>()

        var stepOrinalStep3_1 = StepOrinalModel()
        stepOrinalStep3_1.step = "3"
        stepOrinalStep3_1.stepTitle = "Kiểm tra dưới gầm xe"
        stepOrinalStep3_1.subStep = "3.1"
        stepOrinalStep3_1.subStepTitle1 = "Dưới gầm xe"
        stepOrinalStep3_1.subStepTitle2 = "Khung xe"
        stepOrinalStep3_1.subStepTitle3 = "Tình trạng khung xe"
        listSteps.add(stepOrinalStep3_1)

        var stepOrinalStep3_2 = StepOrinalModel()
        stepOrinalStep3_2.step = "3"
        stepOrinalStep3_2.stepTitle = "Kiểm tra dưới gầm xe"
        stepOrinalStep3_2.subStep = "3.2"
        stepOrinalStep3_2.subStepTitle1 = "Dưới gầm xe"
        stepOrinalStep3_2.subStepTitle2 = "Động cơ và hộp số"
        stepOrinalStep3_2.subStepTitle3 = "Bề mặt ốc vít, gioăng, keo làm kín"
        listSteps.add(stepOrinalStep3_2)

        var stepOrinalStep3_3 = StepOrinalModel()
        stepOrinalStep3_3.step = "3"
        stepOrinalStep3_3.stepTitle = "Kiểm tra dưới gầm xe"
        stepOrinalStep3_3.subStep = "3.2"
        stepOrinalStep3_3.subStepTitle1 = "Dưới gầm xe"
        stepOrinalStep3_3.subStepTitle2 = "Động cơ và hộp số"
        stepOrinalStep3_3.subStepTitle3 = "Tình trạng rò rỉ"
        listSteps.add(stepOrinalStep3_3)

        var stepOrinalStep3_4 = StepOrinalModel()
        stepOrinalStep3_4.step = "3"
        stepOrinalStep3_4.stepTitle = "Kiểm tra dưới gầm xe"
        stepOrinalStep3_4.subStep = "3.2"
        stepOrinalStep3_4.subStepTitle1 = "Dưới gầm xe"
        stepOrinalStep3_4.subStepTitle2 = "Động cơ và hộp số"
        stepOrinalStep3_4.subStepTitle3 = "Dấu hiệu va đập"
        listSteps.add(stepOrinalStep3_4)

        var stepOrinalStep3_5 = StepOrinalModel()
        stepOrinalStep3_5.step = "3"
        stepOrinalStep3_5.stepTitle = "Kiểm tra dưới gầm xe"
        stepOrinalStep3_5.subStep = "3.2"
        stepOrinalStep3_5.subStepTitle1 = "Dưới gầm xe"
        stepOrinalStep3_5.subStepTitle2 = "Động cơ và hộp số"
        stepOrinalStep3_5.subStepTitle3 = "Cao su chân hộp số"
        listSteps.add(stepOrinalStep3_5)

        var stepOrinalStep3_6 = StepOrinalModel()
        stepOrinalStep3_6.step = "3"
        stepOrinalStep3_6.stepTitle = "Kiểm tra dưới gầm xe"
        stepOrinalStep3_6.subStep = "3.3"
        stepOrinalStep3_6.subStepTitle1 = "Dưới gầm xe"
        stepOrinalStep3_6.subStepTitle2 = "Hệ thống giảm chấn"
        stepOrinalStep3_6.subStepTitle3 = "Tình trạng rò rỉ"
        listSteps.add(stepOrinalStep3_6)

        var stepOrinalStep3_7 = StepOrinalModel()
        stepOrinalStep3_7.step = "3"
        stepOrinalStep3_7.stepTitle = "Kiểm tra dưới gầm xe"
        stepOrinalStep3_7.subStep = "3.3"
        stepOrinalStep3_7.subStepTitle1 = "Dưới gầm xe"
        stepOrinalStep3_7.subStepTitle2 = "Hệ thống giảm chấn"
        stepOrinalStep3_7.subStepTitle3 = "Các khớp nối cao su"
        listSteps.add(stepOrinalStep3_7)

        var stepOrinalStep3_8 = StepOrinalModel()
        stepOrinalStep3_8.step = "3"
        stepOrinalStep3_8.stepTitle = "Kiểm tra dưới gầm xe"
        stepOrinalStep3_8.subStep = "3.4"
        stepOrinalStep3_8.subStepTitle1 = "Dưới gầm xe"
        stepOrinalStep3_8.subStepTitle2 = "Hệ thống lái"
        stepOrinalStep3_8.subStepTitle3 = "Tính trạng rò rỉ"
        listSteps.add(stepOrinalStep3_8)

        var stepOrinalStep3_9 = StepOrinalModel()
        stepOrinalStep3_9.step = "3"
        stepOrinalStep3_9.stepTitle = "Kiểm tra dưới gầm xe"
        stepOrinalStep3_9.subStep = "3.4"
        stepOrinalStep3_9.subStepTitle1 = "Dưới gầm xe"
        stepOrinalStep3_9.subStepTitle2 = "Hệ thống lái"
        stepOrinalStep3_9.subStepTitle3 = "Các khóp nối cao su"
        listSteps.add(stepOrinalStep3_9)

        var stepOrinalStep3_10 = StepOrinalModel()
        stepOrinalStep3_10.step = "3"
        stepOrinalStep3_10.stepTitle = "Kiểm tra dưới gầm xe"
        stepOrinalStep3_10.subStep = "3.5"
        stepOrinalStep3_10.subStepTitle1 = "Dưới gầm xe"
        stepOrinalStep3_10.subStepTitle2 = "Hệ thống truyền lực"
        stepOrinalStep3_10.subStepTitle3 = "Tình trạng rò rỉ"
        listSteps.add(stepOrinalStep3_10)

        var stepOrinalStep3_11 = StepOrinalModel()
        stepOrinalStep3_11.step = "3"
        stepOrinalStep3_11.stepTitle = "Kiểm tra dưới gầm xe"
        stepOrinalStep3_11.subStep = "3.5"
        stepOrinalStep3_11.subStepTitle1 = "Dưới gầm xe"
        stepOrinalStep3_11.subStepTitle2 = "Hệ thống truyền lực"
        stepOrinalStep3_11.subStepTitle3 = "Các khóp nối cao su"
        listSteps.add(stepOrinalStep3_11)

        var stepOrinalStep3_12 = StepOrinalModel()
        stepOrinalStep3_12.step = "3"
        stepOrinalStep3_12.stepTitle = "Kiểm tra dưới gầm xe"
        stepOrinalStep3_12.subStep = "3.6"
        stepOrinalStep3_12.subStepTitle1 = "Dưới gầm xe"
        stepOrinalStep3_12.subStepTitle2 = "Sàn xe"
        stepOrinalStep3_12.subStepTitle3 = "Dấu hiệu va đập"
        listSteps.add(stepOrinalStep3_12)

        var stepOrinalStep3_13 = StepOrinalModel()
        stepOrinalStep3_13.step = "3"
        stepOrinalStep3_13.stepTitle = "Kiểm tra dưới gầm xe"
        stepOrinalStep3_13.subStep = "3.7"
        stepOrinalStep3_13.subStepTitle1 = "Dưới gầm xe"
        stepOrinalStep3_13.subStepTitle2 = "Lốp dự phòng"
        stepOrinalStep3_13.subStepTitle3 = "Dấu hiệu va đập"
        listSteps.add(stepOrinalStep3_13)

        var stepOrinalStep3_14 = StepOrinalModel()
        stepOrinalStep3_14.step = "3"
        stepOrinalStep3_14.stepTitle = "Kiểm tra dưới gầm xe"
        stepOrinalStep3_14.subStep = "3.8"
        stepOrinalStep3_14.subStepTitle1 = "Dưới gầm xe"
        stepOrinalStep3_14.subStepTitle2 = "Tấm yếm phủ lườn xe"
        stepOrinalStep3_14.subStepTitle3 = "Dấu hiệu va đập"
        listSteps.add(stepOrinalStep3_14)

        var stepOrinalStep3_15 = StepOrinalModel()
        stepOrinalStep3_15.step = "3"
        stepOrinalStep3_15.stepTitle = "Kiểm tra dưới gầm xe"
        stepOrinalStep3_15.subStep = "3.9"
        stepOrinalStep3_15.subStepTitle1 = "Dưới gầm xe"
        stepOrinalStep3_15.subStepTitle2 = "Lốp xe"
        stepOrinalStep3_15.subStepTitle3 = "Tình trạng chung"
        listSteps.add(stepOrinalStep3_15)

        var stepOrinalStep3_16 = StepOrinalModel()
        stepOrinalStep3_16.step = "3"
        stepOrinalStep3_16.stepTitle = "Kiểm tra dưới gầm xe"
        stepOrinalStep3_16.subStep = "3.9"
        stepOrinalStep3_16.subStepTitle1 = "Dưới gầm xe"
        stepOrinalStep3_16.subStepTitle2 = "Lốp xe"
        stepOrinalStep3_16.subStepTitle3 = "Độ mòn"
        listSteps.add(stepOrinalStep3_16)

        var stepOrinalStep3_17 = StepOrinalModel()
        stepOrinalStep3_17.step = "3"
        stepOrinalStep3_17.stepTitle = "Kiểm tra dưới gầm xe"
        stepOrinalStep3_17.subStep = "3.10"
        stepOrinalStep3_17.subStepTitle1 = "Dưới gầm xe"
        stepOrinalStep3_17.subStepTitle2 = "Cụm chi tiết phanh bánh xe"
        stepOrinalStep3_17.subStepTitle3 = "Kiểm tranh các đường ống phanh lắp đúng vị trí"
        listSteps.add(stepOrinalStep3_17)

        var stepOrinalStep3_18 = StepOrinalModel()
        stepOrinalStep3_18.step = "3"
        stepOrinalStep3_18.stepTitle = "Kiểm tra dưới gầm xe"
        stepOrinalStep3_18.subStep = "3.10"
        stepOrinalStep3_18.subStepTitle1 = "Dưới gầm xe"
        stepOrinalStep3_18.subStepTitle2 = "Cụm chi tiết phanh bánh xe"
        stepOrinalStep3_18.subStepTitle3 = "Má phanh"
        listSteps.add(stepOrinalStep3_18)

        var stepOrinalStep3_19 = StepOrinalModel()
        stepOrinalStep3_19.step = "3"
        stepOrinalStep3_19.stepTitle = "Kiểm tra dưới gầm xe"
        stepOrinalStep3_19.subStep = "3.10"
        stepOrinalStep3_19.subStepTitle1 = "Dưới gầm xe"
        stepOrinalStep3_19.subStepTitle2 = "Cụm chi tiết phanh bánh xe"
        stepOrinalStep3_19.subStepTitle3 = "Đĩa phanh"
        listSteps.add(stepOrinalStep3_19)

        return listSteps
    }

    fun dataStep4(): MutableList<StepOrinalModel> {
        var listSteps = mutableListOf<StepOrinalModel>()

        var stepOrinalStep4_1 = StepOrinalModel()
        stepOrinalStep4_1.step = "4"
        stepOrinalStep4_1.stepTitle = "Kiểm tra khoang động cơ"
        stepOrinalStep4_1.subStep = ""
        stepOrinalStep4_1.subStepTitle1 = "Khoang động cơ"
        stepOrinalStep4_1.subStepTitle2 = ""
        stepOrinalStep4_1.subStepTitle3 = "Phía trong nắp capo"
        listSteps.add(stepOrinalStep4_1)

        var stepOrinalStep4_2 = StepOrinalModel()
        stepOrinalStep4_2.step = "4"
        stepOrinalStep4_2.stepTitle = "Kiểm tra khoang động cơ"
        stepOrinalStep4_2.subStep = ""
        stepOrinalStep4_2.subStepTitle1 = "Khoang động cơ"
        stepOrinalStep4_2.subStepTitle2 = ""
        stepOrinalStep4_2.subStepTitle3 = "Chân đèn pha"
        listSteps.add(stepOrinalStep4_2)

        var stepOrinalStep4_3 = StepOrinalModel()
        stepOrinalStep4_3.step = "4"
        stepOrinalStep4_3.stepTitle = "Kiểm tra khoang động cơ"
        stepOrinalStep4_3.subStep = ""
        stepOrinalStep4_3.subStepTitle1 = "Khoang động cơ"
        stepOrinalStep4_3.subStepTitle2 = ""
        stepOrinalStep4_3.subStepTitle3 = "Cụm đầu sắt xi"
        listSteps.add(stepOrinalStep4_3)

        var stepOrinalStep4_4 = StepOrinalModel()
        stepOrinalStep4_4.step = "4"
        stepOrinalStep4_4.stepTitle = "Kiểm tra khoang động cơ"
        stepOrinalStep4_4.subStep = ""
        stepOrinalStep4_4.subStepTitle1 = "Khoang động cơ"
        stepOrinalStep4_4.subStepTitle2 = ""
        stepOrinalStep4_4.subStepTitle3 = "Bề mặt ốc vít, gioăng, keo làm kín"
        listSteps.add(stepOrinalStep4_4)

        return listSteps
    }

    fun dataStep5(): MutableList<StepOrinalModel> {
        var listSteps = mutableListOf<StepOrinalModel>()

        var stepOrinalStep5_1 = StepOrinalModel()
        stepOrinalStep5_1.step = "5"
        stepOrinalStep5_1.stepTitle = "Kiểm tra động cơ và hộp số"
        stepOrinalStep5_1.subStep = "5.1"
        stepOrinalStep5_1.subStepTitle1 = "Động cơ và hộp số"
        stepOrinalStep5_1.subStepTitle2 = "Các chất lỏng"
        stepOrinalStep5_1.subStepTitle3 = "Dầu nhớt động cơ"
        listSteps.add(stepOrinalStep5_1)

        var stepOrinalStep5_2 = StepOrinalModel()
        stepOrinalStep5_2.step = "5"
        stepOrinalStep5_2.stepTitle = "Kiểm tra động cơ và hộp số"
        stepOrinalStep5_2.subStep = "5.1"
        stepOrinalStep5_2.subStepTitle1 = "Động cơ và hộp số"
        stepOrinalStep5_2.subStepTitle2 = "Các chất lỏng"
        stepOrinalStep5_2.subStepTitle3 = "Nước làm mát"
        listSteps.add(stepOrinalStep5_2)

        var stepOrinalStep5_3 = StepOrinalModel()
        stepOrinalStep5_3.step = "5"
        stepOrinalStep5_3.stepTitle = "Kiểm tra động cơ và hộp số"
        stepOrinalStep5_3.subStep = "5.1"
        stepOrinalStep5_3.subStepTitle1 = "Động cơ và hộp số"
        stepOrinalStep5_3.subStepTitle2 = "Các chất lỏng"
        stepOrinalStep5_3.subStepTitle3 = "Dầu phanh"
        listSteps.add(stepOrinalStep5_3)

        var stepOrinalStep5_4 = StepOrinalModel()
        stepOrinalStep5_4.step = "5"
        stepOrinalStep5_4.stepTitle = "Kiểm tra động cơ và hộp số"
        stepOrinalStep5_4.subStep = "5.1"
        stepOrinalStep5_4.subStepTitle1 = "Động cơ và hộp số"
        stepOrinalStep5_4.subStepTitle2 = "Các chất lỏng"
        stepOrinalStep5_4.subStepTitle3 = "Dầu hộp số tự động/ hộp số sàn"
        listSteps.add(stepOrinalStep5_4)

        var stepOrinalStep5_5 = StepOrinalModel()
        stepOrinalStep5_5.step = "5"
        stepOrinalStep5_5.stepTitle = "Kiểm tra động cơ và hộp số"
        stepOrinalStep5_5.subStep = "5.1"
        stepOrinalStep5_5.subStepTitle1 = "Động cơ và hộp số"
        stepOrinalStep5_5.subStepTitle2 = "Các chất lỏng"
        stepOrinalStep5_5.subStepTitle3 = "Dầu hộp số phụ"
        listSteps.add(stepOrinalStep5_5)

        var stepOrinalStep5_6 = StepOrinalModel()
        stepOrinalStep5_6.step = "5"
        stepOrinalStep5_6.stepTitle = "Kiểm tra động cơ và hộp số"
        stepOrinalStep5_6.subStep = "5.1"
        stepOrinalStep5_6.subStepTitle1 = "Động cơ và hộp số"
        stepOrinalStep5_6.subStepTitle2 = "Các chất lỏng"
        stepOrinalStep5_6.subStepTitle3 = "Dầu (nhớt) cầu"
        listSteps.add(stepOrinalStep5_6)

        var stepOrinalStep5_7 = StepOrinalModel()
        stepOrinalStep5_7.step = "5"
        stepOrinalStep5_7.stepTitle = "Kiểm tra động cơ và hộp số"
        stepOrinalStep5_7.subStep = "5.1"
        stepOrinalStep5_7.subStepTitle1 = "Động cơ và hộp số"
        stepOrinalStep5_7.subStepTitle2 = "Các chất lỏng"
        stepOrinalStep5_7.subStepTitle3 = "Dầu trợ lực lái"
        listSteps.add(stepOrinalStep5_7)

        var stepOrinalStep5_8 = StepOrinalModel()
        stepOrinalStep5_8.step = "5"
        stepOrinalStep5_8.stepTitle = "Kiểm tra động cơ và hộp số"
        stepOrinalStep5_8.subStep = "5.1"
        stepOrinalStep5_8.subStepTitle1 = "Động cơ và hộp số"
        stepOrinalStep5_8.subStepTitle2 = "Các chất lỏng"
        stepOrinalStep5_8.subStepTitle3 = "Dầu ly hợp cho hộp số sàn"
        listSteps.add(stepOrinalStep5_8)

        var stepOrinalStep5_9 = StepOrinalModel()
        stepOrinalStep5_9.step = "5"
        stepOrinalStep5_9.stepTitle = "Kiểm tra động cơ và hộp số"
        stepOrinalStep5_9.subStep = "5.1"
        stepOrinalStep5_9.subStepTitle1 = "Động cơ và hộp số"
        stepOrinalStep5_9.subStepTitle2 = "Các chất lỏng"
        stepOrinalStep5_9.subStepTitle3 = "Dung dịch rửa kính"
        listSteps.add(stepOrinalStep5_9)

        var stepOrinalStep5_10 = StepOrinalModel()
        stepOrinalStep5_10.step = "5"
        stepOrinalStep5_10.stepTitle = "Kiểm tra động cơ và hộp số"
        stepOrinalStep5_10.subStep = "5.1"
        stepOrinalStep5_10.subStepTitle1 = "Động cơ và hộp số"
        stepOrinalStep5_10.subStepTitle2 = "Các chất lỏng"
        stepOrinalStep5_10.subStepTitle3 = "Gas điều hòa"
        listSteps.add(stepOrinalStep5_10)

        var stepOrinalStep5_11 = StepOrinalModel()
        stepOrinalStep5_11.step = "5"
        stepOrinalStep5_11.stepTitle = "Kiểm tra động cơ và hộp số"
        stepOrinalStep5_11.subStep = "5.2"
        stepOrinalStep5_11.subStepTitle1 = "Động cơ và hộp số"
        stepOrinalStep5_11.subStepTitle2 = "Động cơ"
        stepOrinalStep5_11.subStepTitle3 = "Tình trạng rò rỉ dầu"
        listSteps.add(stepOrinalStep5_11)

        var stepOrinalStep5_12 = StepOrinalModel()
        stepOrinalStep5_12.step = "5"
        stepOrinalStep5_12.stepTitle = "Kiểm tra động cơ và hộp số"
        stepOrinalStep5_12.subStep = "5.2"
        stepOrinalStep5_12.subStepTitle1 = "Động cơ và hộp số"
        stepOrinalStep5_12.subStepTitle2 = "Động cơ"
        stepOrinalStep5_12.subStepTitle3 = "Các đường ống"
        listSteps.add(stepOrinalStep5_12)

        var stepOrinalStep5_13 = StepOrinalModel()
        stepOrinalStep5_13.step = "5"
        stepOrinalStep5_13.stepTitle = "Kiểm tra động cơ và hộp số"
        stepOrinalStep5_13.subStep = "5.2"
        stepOrinalStep5_13.subStepTitle1 = "Động cơ và hộp số"
        stepOrinalStep5_13.subStepTitle2 = "Động cơ"
        stepOrinalStep5_13.subStepTitle3 = "Đai dẫn động"
        listSteps.add(stepOrinalStep5_13)

        var stepOrinalStep5_14 = StepOrinalModel()
        stepOrinalStep5_14.step = "5"
        stepOrinalStep5_14.stepTitle = "Kiểm tra động cơ và hộp số"
        stepOrinalStep5_14.subStep = "5.2"
        stepOrinalStep5_14.subStepTitle1 = "Động cơ và hộp số"
        stepOrinalStep5_14.subStepTitle2 = "Động cơ"
        stepOrinalStep5_14.subStepTitle3 = "Các bó dây điện"
        listSteps.add(stepOrinalStep5_14)

        var stepOrinalStep5_15 = StepOrinalModel()
        stepOrinalStep5_15.step = "5"
        stepOrinalStep5_15.stepTitle = "Kiểm tra động cơ và hộp số"
        stepOrinalStep5_15.subStep = "5.2"
        stepOrinalStep5_15.subStepTitle1 = "Động cơ và hộp số"
        stepOrinalStep5_15.subStepTitle2 = "Động cơ"
        stepOrinalStep5_15.subStepTitle3 = "Vỏ hộp lọc gió"
        listSteps.add(stepOrinalStep5_15)

        var stepOrinalStep5_16 = StepOrinalModel()
        stepOrinalStep5_16.step = "5"
        stepOrinalStep5_16.stepTitle = "Kiểm tra động cơ và hộp số"
        stepOrinalStep5_16.subStep = "5.2"
        stepOrinalStep5_16.subStepTitle1 = "Động cơ và hộp số"
        stepOrinalStep5_16.subStepTitle2 = "Động cơ"
        stepOrinalStep5_16.subStepTitle3 = "Các cao su chân máy"
        listSteps.add(stepOrinalStep5_16)

        var stepOrinalStep5_17 = StepOrinalModel()
        stepOrinalStep5_17.step = "5"
        stepOrinalStep5_17.stepTitle = "Kiểm tra động cơ và hộp số"
        stepOrinalStep5_17.subStep = "5.2"
        stepOrinalStep5_17.subStepTitle1 = "Động cơ và hộp số"
        stepOrinalStep5_17.subStepTitle2 = "Động cơ"
        stepOrinalStep5_17.subStepTitle3 = "Két nước, giải nhiệt turbo, giải nhiệt hệ thống lạnh"
        listSteps.add(stepOrinalStep5_17)

        var stepOrinalStep5_18 = StepOrinalModel()
        stepOrinalStep5_18.step = "5"
        stepOrinalStep5_18.stepTitle = "Kiểm tra động cơ và hộp số"
        stepOrinalStep5_18.subStep = "5.3"
        stepOrinalStep5_18.subStepTitle1 = "Động cơ và hộp số"
        stepOrinalStep5_18.subStepTitle2 = "Hệ thống làm mát"
        stepOrinalStep5_18.subStepTitle3 = "Ly hợp chết"
        listSteps.add(stepOrinalStep5_18)

        var stepOrinalStep5_19 = StepOrinalModel()
        stepOrinalStep5_19.step = "5"
        stepOrinalStep5_19.stepTitle = "Kiểm tra động cơ và hộp số"
        stepOrinalStep5_19.subStep = "5.3"
        stepOrinalStep5_19.subStepTitle1 = "Động cơ và hộp số"
        stepOrinalStep5_19.subStepTitle2 = "Hệ thống làm mát"
        stepOrinalStep5_19.subStepTitle3 = "Ly hợp theo nhiệt độ động cơ"
        listSteps.add(stepOrinalStep5_19)

        var stepOrinalStep5_20 = StepOrinalModel()
        stepOrinalStep5_20.step = "5"
        stepOrinalStep5_20.stepTitle = "Kiểm tra động cơ và hộp số"
        stepOrinalStep5_20.subStep = "5.3"
        stepOrinalStep5_20.subStepTitle1 = "Động cơ và hộp số"
        stepOrinalStep5_20.subStepTitle2 = "Hệ thống làm mát"
        stepOrinalStep5_20.subStepTitle3 = "Bơm nước"
        listSteps.add(stepOrinalStep5_20)

        var stepOrinalStep5_21 = StepOrinalModel()
        stepOrinalStep5_21.step = "5"
        stepOrinalStep5_21.stepTitle = "Kiểm tra động cơ và hộp số"
        stepOrinalStep5_21.subStep = "5.3"
        stepOrinalStep5_21.subStepTitle1 = "Động cơ và hộp số"
        stepOrinalStep5_21.subStepTitle2 = "Hệ thống làm mát"
        stepOrinalStep5_21.subStepTitle3 = "Bình nước làm mát phụ"
        listSteps.add(stepOrinalStep5_21)

        var stepOrinalStep5_22 = StepOrinalModel()
        stepOrinalStep5_22.step = "5"
        stepOrinalStep5_22.stepTitle = "Kiểm tra động cơ và hộp số"
        stepOrinalStep5_22.subStep = "5.4"
        stepOrinalStep5_22.subStepTitle1 = "Động cơ và hộp số"
        stepOrinalStep5_22.subStepTitle2 = "Hệ thống nhiên liệu"
        stepOrinalStep5_22.subStepTitle3 = "Tiếng ồn bơm nhiên liệu"
        listSteps.add(stepOrinalStep5_22)

        var stepOrinalStep5_23 = StepOrinalModel()
        stepOrinalStep5_23.step = "5"
        stepOrinalStep5_23.stepTitle = "Kiểm tra động cơ và hộp số"
        stepOrinalStep5_23.subStep = "5.4"
        stepOrinalStep5_23.subStepTitle1 = "Động cơ và hộp số"
        stepOrinalStep5_23.subStepTitle2 = "Hệ thống nhiên liệu"
        stepOrinalStep5_23.subStepTitle3 = "Lọc nhiên liệu"
        listSteps.add(stepOrinalStep5_23)

        var stepOrinalStep5_24 = StepOrinalModel()
        stepOrinalStep5_24.step = "5"
        stepOrinalStep5_24.stepTitle = "Kiểm tra động cơ và hộp số"
        stepOrinalStep5_24.subStep = "5.4"
        stepOrinalStep5_24.subStepTitle1 = "Động cơ và hộp số"
        stepOrinalStep5_24.subStepTitle2 = "Hệ thống nhiên liệu"
        stepOrinalStep5_24.subStepTitle3 = "Lọc gió động cơ"
        listSteps.add(stepOrinalStep5_24)

        var stepOrinalStep5_25 = StepOrinalModel()
        stepOrinalStep5_25.step = "5"
        stepOrinalStep5_25.stepTitle = "Kiểm tra động cơ và hộp số"
        stepOrinalStep5_25.subStep = "5.5"
        stepOrinalStep5_25.subStepTitle1 = "Động cơ và hộp số"
        stepOrinalStep5_25.subStepTitle2 = "Hệ thống điện"
        stepOrinalStep5_25.subStepTitle3 = "Ắc quy"
        listSteps.add(stepOrinalStep5_25)

        return listSteps
    }

    fun dataStep6(): MutableList<StepOrinalModel> {
        var listSteps = mutableListOf<StepOrinalModel>()

        var stepOrinalStep6_1 = StepOrinalModel()
        stepOrinalStep6_1.step = "6"
        stepOrinalStep6_1.stepTitle = "Kiểm tra bên trong xe"
        stepOrinalStep6_1.subStep = "6.1"
        stepOrinalStep6_1.subStepTitle1 = "Bên trong xe"
        stepOrinalStep6_1.subStepTitle2 = "Chức năng"
        stepOrinalStep6_1.subStepTitle3 = "Mùi nội thất"
        listSteps.add(stepOrinalStep6_1)

        var stepOrinalStep6_2 = StepOrinalModel()
        stepOrinalStep6_2.step = "6"
        stepOrinalStep6_2.stepTitle = "Kiểm tra bên trong xe"
        stepOrinalStep6_2.subStep = "6.1"
        stepOrinalStep6_2.subStepTitle1 = "Bên trong xe"
        stepOrinalStep6_2.subStepTitle2 = "Chức năng"
        stepOrinalStep6_2.subStepTitle3 = "Cửa xe"
        listSteps.add(stepOrinalStep6_2)

        var stepOrinalStep6_3 = StepOrinalModel()
        stepOrinalStep6_3.step = "6"
        stepOrinalStep6_3.stepTitle = "Kiểm tra bên trong xe"
        stepOrinalStep6_3.subStep = "6.1"
        stepOrinalStep6_3.subStepTitle1 = "Bên trong xe"
        stepOrinalStep6_3.subStepTitle2 = "Chức năng"
        stepOrinalStep6_3.subStepTitle3 = "Tình trạng ghế"
        listSteps.add(stepOrinalStep6_3)

        var stepOrinalStep6_4 = StepOrinalModel()
        stepOrinalStep6_4.step = "6"
        stepOrinalStep6_4.stepTitle = "Kiểm tra bên trong xe"
        stepOrinalStep6_4.subStep = "6.1"
        stepOrinalStep6_4.subStepTitle1 = "Bên trong xe"
        stepOrinalStep6_4.subStepTitle2 = "Chức năng"
        stepOrinalStep6_4.subStepTitle3 = "Tình trạng dây an toàn"
        listSteps.add(stepOrinalStep6_4)

        var stepOrinalStep6_5 = StepOrinalModel()
        stepOrinalStep6_5.step = "6"
        stepOrinalStep6_5.stepTitle = "Kiểm tra bên trong xe"
        stepOrinalStep6_5.subStep = "6.1"
        stepOrinalStep6_5.subStepTitle1 = "Bên trong xe"
        stepOrinalStep6_5.subStepTitle2 = "Chức năng"
        stepOrinalStep6_5.subStepTitle3 = "Các chức năng và thông số trên taplo"
        listSteps.add(stepOrinalStep6_5)

        var stepOrinalStep6_6 = StepOrinalModel()
        stepOrinalStep6_6.step = "6"
        stepOrinalStep6_6.stepTitle = "Kiểm tra bên trong xe"
        stepOrinalStep6_6.subStep = "6.1"
        stepOrinalStep6_6.subStepTitle1 = "Bên trong xe"
        stepOrinalStep6_6.subStepTitle2 = "Chức năng"
        stepOrinalStep6_6.subStepTitle3 = "Hệ thống điều hòa không khí"
        listSteps.add(stepOrinalStep6_6)

        var stepOrinalStep6_7 = StepOrinalModel()
        stepOrinalStep6_7.step = "6"
        stepOrinalStep6_7.stepTitle = "Kiểm tra bên trong xe"
        stepOrinalStep6_7.subStep = "6.1"
        stepOrinalStep6_7.subStepTitle1 = "Bên trong xe"
        stepOrinalStep6_7.subStepTitle2 = "Chức năng"
        stepOrinalStep6_7.subStepTitle3 = "Hoạt động của đèn xe"
        listSteps.add(stepOrinalStep6_7)

        var stepOrinalStep6_8 = StepOrinalModel()
        stepOrinalStep6_8.step = "6"
        stepOrinalStep6_8.stepTitle = "Kiểm tra bên trong xe"
        stepOrinalStep6_8.subStep = "6.1"
        stepOrinalStep6_8.subStepTitle1 = "Bên trong xe"
        stepOrinalStep6_8.subStepTitle2 = "Chức năng"
        stepOrinalStep6_8.subStepTitle3 = "Hoạt động còi xe"
        listSteps.add(stepOrinalStep6_8)

        var stepOrinalStep6_9 = StepOrinalModel()
        stepOrinalStep6_9.step = "6"
        stepOrinalStep6_9.stepTitle = "Kiểm tra bên trong xe"
        stepOrinalStep6_9.subStep = "6.1"
        stepOrinalStep6_9.subStepTitle1 = "Bên trong xe"
        stepOrinalStep6_9.subStepTitle2 = "Chức năng"
        stepOrinalStep6_9.subStepTitle3 = "Sàn xe"
        listSteps.add(stepOrinalStep6_9)

        var stepOrinalStep6_10 = StepOrinalModel()
        stepOrinalStep6_10.step = "6"
        stepOrinalStep6_10.stepTitle = "Kiểm tra bên trong xe"
        stepOrinalStep6_10.subStep = "6.1"
        stepOrinalStep6_10.subStepTitle1 = "Bên trong xe"
        stepOrinalStep6_10.subStepTitle2 = "Chức năng"
        stepOrinalStep6_10.subStepTitle3 = "Tấm trần"
        listSteps.add(stepOrinalStep6_10)

        var stepOrinalStep6_11 = StepOrinalModel()
        stepOrinalStep6_11.step = "6"
        stepOrinalStep6_11.stepTitle = "Kiểm tra bên trong xe"
        stepOrinalStep6_11.subStep = "6.1"
        stepOrinalStep6_11.subStepTitle1 = "Bên trong xe"
        stepOrinalStep6_11.subStepTitle2 = "Chức năng"
        stepOrinalStep6_11.subStepTitle3 = "Mặt trong nắp cốp"
        listSteps.add(stepOrinalStep6_11)

        var stepOrinalStep6_12 = StepOrinalModel()
        stepOrinalStep6_12.step = "6"
        stepOrinalStep6_12.stepTitle = "Kiểm tra bên trong xe"
        stepOrinalStep6_12.subStep = "6.2"
        stepOrinalStep6_12.subStepTitle1 = "Bên trong xe"
        stepOrinalStep6_12.subStepTitle2 = "Tiện ích khác"
        stepOrinalStep6_12.subStepTitle3 = "Túi khí"
        listSteps.add(stepOrinalStep6_12)

        var stepOrinalStep6_13 = StepOrinalModel()
        stepOrinalStep6_13.step = "6"
        stepOrinalStep6_13.stepTitle = "Kiểm tra bên trong xe"
        stepOrinalStep6_13.subStep = "6.2"
        stepOrinalStep6_13.subStepTitle1 = "Bên trong xe"
        stepOrinalStep6_13.subStepTitle2 = "Tiện ích khác"
        stepOrinalStep6_13.subStepTitle3 = "Đài radio, Máy nghe băng, Máy nghe CD, Loa và Ăng ten"
        listSteps.add(stepOrinalStep6_13)

        var stepOrinalStep6_14 = StepOrinalModel()
        stepOrinalStep6_14.step = "6"
        stepOrinalStep6_14.stepTitle = "Kiểm tra bên trong xe"
        stepOrinalStep6_14.subStep = "6.2"
        stepOrinalStep6_14.subStepTitle1 = "Bên trong xe"
        stepOrinalStep6_14.subStepTitle2 = "Tiện ích khác"
        stepOrinalStep6_14.subStepTitle3 = "Hệ thống chống trộm, báo động"
        listSteps.add(stepOrinalStep6_14)

        var stepOrinalStep6_15 = StepOrinalModel()
        stepOrinalStep6_15.step = "6"
        stepOrinalStep6_15.stepTitle = "Kiểm tra bên trong xe"
        stepOrinalStep6_15.subStep = "6.2"
        stepOrinalStep6_15.subStepTitle1 = "Bên trong xe"
        stepOrinalStep6_15.subStepTitle2 = "Tiện ích khác"
        stepOrinalStep6_15.subStepTitle3 = "Hệ thống định vị (dẫn đường)"
        listSteps.add(stepOrinalStep6_15)

        var stepOrinalStep6_16 = StepOrinalModel()
        stepOrinalStep6_16.step = "6"
        stepOrinalStep6_16.stepTitle = "Kiểm tra bên trong xe"
        stepOrinalStep6_16.subStep = "6.2"
        stepOrinalStep6_16.subStepTitle1 = "Bên trong xe"
        stepOrinalStep6_16.subStepTitle2 = "Tiện ích khác"
        stepOrinalStep6_16.subStepTitle3 = "Hệ thống làm tan sương/ xông kính"
        listSteps.add(stepOrinalStep6_16)

        var stepOrinalStep6_17 = StepOrinalModel()
        stepOrinalStep6_17.step = "6"
        stepOrinalStep6_17.stepTitle = "Kiểm tra bên trong xe"
        stepOrinalStep6_17.subStep = "6.2"
        stepOrinalStep6_17.subStepTitle1 = "Bên trong xe"
        stepOrinalStep6_17.subStepTitle2 = "Tiện ích khác"
        stepOrinalStep6_17.subStepTitle3 = "Điều chỉnh độ nghiên/ khoảng cách tay lái"
        listSteps.add(stepOrinalStep6_17)

        var stepOrinalStep6_18 = StepOrinalModel()
        stepOrinalStep6_18.step = "6"
        stepOrinalStep6_18.stepTitle = "Kiểm tra bên trong xe"
        stepOrinalStep6_18.subStep = "6.2"
        stepOrinalStep6_18.subStepTitle1 = "Bên trong xe"
        stepOrinalStep6_18.subStepTitle2 = "Tiện ích khác"
        stepOrinalStep6_18.subStepTitle3 = "Khóa tay lái"
        listSteps.add(stepOrinalStep6_18)

        var stepOrinalStep6_19 = StepOrinalModel()
        stepOrinalStep6_19.step = "6"
        stepOrinalStep6_19.stepTitle = "Kiểm tra bên trong xe"
        stepOrinalStep6_19.subStep = "6.2"
        stepOrinalStep6_19.subStepTitle1 = "Bên trong xe"
        stepOrinalStep6_19.subStepTitle2 = "Tiện ích khác"
        stepOrinalStep6_19.subStepTitle3 = "Chức năng Gạt mưa"
        listSteps.add(stepOrinalStep6_19)

        var stepOrinalStep6_20 = StepOrinalModel()
        stepOrinalStep6_20.step = "6"
        stepOrinalStep6_20.stepTitle = "Kiểm tra bên trong xe"
        stepOrinalStep6_20.subStep = "6.2"
        stepOrinalStep6_20.subStepTitle1 = "Bên trong xe"
        stepOrinalStep6_20.subStepTitle2 = "Tiện ích khác"
        stepOrinalStep6_20.subStepTitle3 = "Chức năng Rửa kính"
        listSteps.add(stepOrinalStep6_20)

        var stepOrinalStep6_21 = StepOrinalModel()
        stepOrinalStep6_21.step = "6"
        stepOrinalStep6_21.stepTitle = "Kiểm tra bên trong xe"
        stepOrinalStep6_21.subStep = "6.2"
        stepOrinalStep6_21.subStepTitle1 = "Bên trong xe"
        stepOrinalStep6_21.subStepTitle2 = "Tiện ích khác"
        stepOrinalStep6_21.subStepTitle3 = "Các đèn chiếu sáng nội thất"
        listSteps.add(stepOrinalStep6_21)

        var stepOrinalStep6_22 = StepOrinalModel()
        stepOrinalStep6_22.step = "6"
        stepOrinalStep6_22.stepTitle = "Kiểm tra bên trong xe"
        stepOrinalStep6_22.subStep = "6.2"
        stepOrinalStep6_22.subStepTitle1 = "Bên trong xe"
        stepOrinalStep6_22.subStepTitle2 = "Tiện ích khác"
        stepOrinalStep6_22.subStepTitle3 = "Kính chiếu hậu ngoài chỉnh bằng tay/ bằng điện và kính chiếu hậu chống chói"
        listSteps.add(stepOrinalStep6_22)

        var stepOrinalStep6_23 = StepOrinalModel()
        stepOrinalStep6_23.step = "6"
        stepOrinalStep6_23.stepTitle = "Kiểm tra bên trong xe"
        stepOrinalStep6_23.subStep = "6.2"
        stepOrinalStep6_23.subStepTitle1 = "Bên trong xe"
        stepOrinalStep6_23.subStepTitle2 = "Tiện ích khác"
        stepOrinalStep6_23.subStepTitle3 = "Hệ thống cảnh báo điểm mù (BLS)"
        listSteps.add(stepOrinalStep6_23)

        var stepOrinalStep6_24 = StepOrinalModel()
        stepOrinalStep6_24.step = "6"
        stepOrinalStep6_24.stepTitle = "Kiểm tra bên trong xe"
        stepOrinalStep6_24.subStep = "6.2"
        stepOrinalStep6_24.subStepTitle1 = "Bên trong xe"
        stepOrinalStep6_24.subStepTitle2 = "Tiện ích khác"
        stepOrinalStep6_24.subStepTitle3 = "Camera phía sau"
        listSteps.add(stepOrinalStep6_24)

        var stepOrinalStep6_25 = StepOrinalModel()
        stepOrinalStep6_25.step = "6"
        stepOrinalStep6_25.stepTitle = "Kiểm tra bên trong xe"
        stepOrinalStep6_25.subStep = "6.2"
        stepOrinalStep6_25.subStepTitle1 = "Bên trong xe"
        stepOrinalStep6_25.subStepTitle2 = "Tiện ích khác"
        stepOrinalStep6_25.subStepTitle3 = "Hệ thống SYNC"
        listSteps.add(stepOrinalStep6_25)

        var stepOrinalStep6_26 = StepOrinalModel()
        stepOrinalStep6_26.step = "6"
        stepOrinalStep6_26.stepTitle = "Kiểm tra bên trong xe"
        stepOrinalStep6_26.subStep = "6.2"
        stepOrinalStep6_26.subStepTitle1 = "Bên trong xe"
        stepOrinalStep6_26.subStepTitle2 = "Tiện ích khác"
        stepOrinalStep6_26.subStepTitle3 = "Hệ thống hổ trợ đổ xe"
        listSteps.add(stepOrinalStep6_26)

        var stepOrinalStep6_27 = StepOrinalModel()
        stepOrinalStep6_27.step = "6"
        stepOrinalStep6_27.stepTitle = "Kiểm tra bên trong xe"
        stepOrinalStep6_27.subStep = "6.2"
        stepOrinalStep6_27.subStepTitle1 = "Bên trong xe"
        stepOrinalStep6_27.subStepTitle2 = "Tiện ích khác"
        stepOrinalStep6_27.subStepTitle3 = "Hệ thống giải trí phía sau"
        listSteps.add(stepOrinalStep6_27)

        var stepOrinalStep6_28 = StepOrinalModel()
        stepOrinalStep6_28.step = "6"
        stepOrinalStep6_28.stepTitle = "Kiểm tra bên trong xe"
        stepOrinalStep6_28.subStep = "6.2"
        stepOrinalStep6_28.subStepTitle1 = "Bên trong xe"
        stepOrinalStep6_28.subStepTitle2 = "Tiện ích khác"
        stepOrinalStep6_28.subStepTitle3 = "Điểm cấp nguồn và mồi thuốc"
        listSteps.add(stepOrinalStep6_28)

        var stepOrinalStep6_29 = StepOrinalModel()
        stepOrinalStep6_29.step = "6"
        stepOrinalStep6_29.stepTitle = "Kiểm tra bên trong xe"
        stepOrinalStep6_29.subStep = "6.2"
        stepOrinalStep6_29.subStepTitle1 = "Bên trong xe"
        stepOrinalStep6_29.subStepTitle2 = "Tiện ích khác"
        stepOrinalStep6_29.subStepTitle3 = "Gạt tàn"
        listSteps.add(stepOrinalStep6_29)

        var stepOrinalStep6_30 = StepOrinalModel()
        stepOrinalStep6_30.step = "6"
        stepOrinalStep6_30.stepTitle = "Kiểm tra bên trong xe"
        stepOrinalStep6_30.subStep = "6.2"
        stepOrinalStep6_30.subStepTitle1 = "Bên trong xe"
        stepOrinalStep6_30.subStepTitle2 = "Tiện ích khác"
        stepOrinalStep6_30.subStepTitle3 = "Hộc găng tay, tự tay và Cốp giữa"
        listSteps.add(stepOrinalStep6_30)

        var stepOrinalStep6_31 = StepOrinalModel()
        stepOrinalStep6_31.step = "6"
        stepOrinalStep6_31.stepTitle = "Kiểm tra bên trong xe"
        stepOrinalStep6_31.subStep = "6.2"
        stepOrinalStep6_31.subStepTitle1 = "Bên trong xe"
        stepOrinalStep6_31.subStepTitle2 = "Tiện ích khác"
        stepOrinalStep6_31.subStepTitle3 = "Che nắng, gương trang điểm và đèn"
        listSteps.add(stepOrinalStep6_31)

        var stepOrinalStep6_32 = StepOrinalModel()
        stepOrinalStep6_32.step = "6"
        stepOrinalStep6_32.stepTitle = "Kiểm tra bên trong xe"
        stepOrinalStep6_32.subStep = "6.2"
        stepOrinalStep6_32.subStepTitle1 = "Bên trong xe"
        stepOrinalStep6_32.subStepTitle2 = "Tiện ích khác"
        stepOrinalStep6_32.subStepTitle3 = "Cơ cấu điều chỉnh bàn đạp nếu có"
        listSteps.add(stepOrinalStep6_32)

        var stepOrinalStep6_33 = StepOrinalModel()
        stepOrinalStep6_33.step = "6"
        stepOrinalStep6_33.stepTitle = "Kiểm tra bên trong xe"
        stepOrinalStep6_33.subStep = "6.2"
        stepOrinalStep6_33.subStepTitle1 = "Bên trong xe"
        stepOrinalStep6_33.subStepTitle2 = "Tiện ích khác"
        stepOrinalStep6_33.subStepTitle3 = "Điều chỉnh ghế và tựa đầu"
        listSteps.add(stepOrinalStep6_33)

        var stepOrinalStep6_34 = StepOrinalModel()
        stepOrinalStep6_34.step = "6"
        stepOrinalStep6_34.stepTitle = "Kiểm tra bên trong xe"
        stepOrinalStep6_34.subStep = "6.2"
        stepOrinalStep6_34.subStepTitle1 = "Bên trong xe"
        stepOrinalStep6_34.subStepTitle2 = "Tiện ích khác"
        stepOrinalStep6_34.subStepTitle3 = "Gập ghế"
        listSteps.add(stepOrinalStep6_34)

        var stepOrinalStep6_35 = StepOrinalModel()
        stepOrinalStep6_35.step = "6"
        stepOrinalStep6_35.stepTitle = "Kiểm tra bên trong xe"
        stepOrinalStep6_35.subStep = "6.2"
        stepOrinalStep6_35.subStepTitle1 = "Bên trong xe"
        stepOrinalStep6_35.subStepTitle2 = "Tiện ích khác"
        stepOrinalStep6_35.subStepTitle3 = "Sưởi ghế"
        listSteps.add(stepOrinalStep6_35)

        var stepOrinalStep6_36 = StepOrinalModel()
        stepOrinalStep6_36.step = "6"
        stepOrinalStep6_36.stepTitle = "Kiểm tra bên trong xe"
        stepOrinalStep6_36.subStep = "6.2"
        stepOrinalStep6_36.subStepTitle1 = "Bên trong xe"
        stepOrinalStep6_36.subStepTitle2 = "Tiện ích khác"
        stepOrinalStep6_36.subStepTitle3 = "Ghế an toàn cho trẻ em tích hợp"
        listSteps.add(stepOrinalStep6_36)

        var stepOrinalStep6_37 = StepOrinalModel()
        stepOrinalStep6_37.step = "6"
        stepOrinalStep6_37.stepTitle = "Kiểm tra bên trong xe"
        stepOrinalStep6_37.subStep = "6.2"
        stepOrinalStep6_37.subStepTitle1 = "Bên trong xe"
        stepOrinalStep6_37.subStepTitle2 = "Tiện ích khác"
        stepOrinalStep6_37.subStepTitle3 = "Hệ thống điều khiển từ xa và Hệ thống khởi động bằng nút nhấn"
        listSteps.add(stepOrinalStep6_37)

        var stepOrinalStep6_38 = StepOrinalModel()
        stepOrinalStep6_38.step = "6"
        stepOrinalStep6_38.stepTitle = "Kiểm tra bên trong xe"
        stepOrinalStep6_38.subStep = "6.2"
        stepOrinalStep6_38.subStepTitle1 = "Bên trong xe"
        stepOrinalStep6_38.subStepTitle2 = "Tiện ích khác"
        stepOrinalStep6_38.subStepTitle3 = "Khóa trẻ em"
        listSteps.add(stepOrinalStep6_38)

        var stepOrinalStep6_39 = StepOrinalModel()
        stepOrinalStep6_39.step = "6"
        stepOrinalStep6_39.stepTitle = "Kiểm tra bên trong xe"
        stepOrinalStep6_39.subStep = "6.2"
        stepOrinalStep6_39.subStepTitle1 = "Bên trong xe"
        stepOrinalStep6_39.subStepTitle2 = "Tiện ích khác"
        stepOrinalStep6_39.subStepTitle3 = "Cơ cấu mở nặp nhiên liệu và Cốp sau điều khiển từ xa"
        listSteps.add(stepOrinalStep6_39)

        var stepOrinalStep6_40 = StepOrinalModel()
        stepOrinalStep6_40.step = "6"
        stepOrinalStep6_40.stepTitle = "Kiểm tra bên trong xe"
        stepOrinalStep6_40.subStep = "6.2"
        stepOrinalStep6_40.subStepTitle1 = "Bên trong xe"
        stepOrinalStep6_40.subStepTitle2 = "Tiện ích khác"
        stepOrinalStep6_40.subStepTitle3 = "Đèn khoang hành lý/ khoang để đồ"
        listSteps.add(stepOrinalStep6_40)

        var stepOrinalStep6_41 = StepOrinalModel()
        stepOrinalStep6_41.step = "6"
        stepOrinalStep6_41.stepTitle = "Kiểm tra bên trong xe"
        stepOrinalStep6_41.subStep = "6.2"
        stepOrinalStep6_41.subStepTitle1 = "Bên trong xe"
        stepOrinalStep6_41.subStepTitle2 = "Tiện ích khác"
        stepOrinalStep6_41.subStepTitle3 = "Bộ dụng cụ và kích nâng"
        listSteps.add(stepOrinalStep6_41)

        var stepOrinalStep6_42 = StepOrinalModel()
        stepOrinalStep6_42.step = "6"
        stepOrinalStep6_42.stepTitle = "Kiểm tra bên trong xe"
        stepOrinalStep6_42.subStep = "6.2"
        stepOrinalStep6_42.subStepTitle1 = "Bên trong xe"
        stepOrinalStep6_42.subStepTitle2 = "Tiện ích khác"
        stepOrinalStep6_42.subStepTitle3 = "Kích thước/ Loại và tình trạng hông lốp bánh xe dự phòng"
        listSteps.add(stepOrinalStep6_42)

        var stepOrinalStep6_43 = StepOrinalModel()
        stepOrinalStep6_43.step = "6"
        stepOrinalStep6_43.stepTitle = "Kiểm tra bên trong xe"
        stepOrinalStep6_43.subStep = "6.2"
        stepOrinalStep6_43.subStepTitle1 = "Bên trong xe"
        stepOrinalStep6_43.subStepTitle2 = "Tiện ích khác"
        stepOrinalStep6_43.subStepTitle3 = "Kiểm tra áp suất lốp/ độ sâu ta-lông lớp xe dự phòng"
        listSteps.add(stepOrinalStep6_43)

        var stepOrinalStep6_44 = StepOrinalModel()
        stepOrinalStep6_44.step = "6"
        stepOrinalStep6_44.stepTitle = "Kiểm tra bên trong xe"
        stepOrinalStep6_44.subStep = "6.2"
        stepOrinalStep6_44.subStepTitle1 = "Bên trong xe"
        stepOrinalStep6_44.subStepTitle2 = "Tiện ích khác"
        stepOrinalStep6_44.subStepTitle3 = "Bộ dụng cụ sữa chữa lốp xe"
        listSteps.add(stepOrinalStep6_44)

        var stepOrinalStep6_45 = StepOrinalModel()
        stepOrinalStep6_45.step = "6"
        stepOrinalStep6_45.stepTitle = "Kiểm tra bên trong xe"
        stepOrinalStep6_45.subStep = "6.2"
        stepOrinalStep6_45.subStepTitle1 = "Bên trong xe"
        stepOrinalStep6_45.subStepTitle2 = "Tiện ích khác"
        stepOrinalStep6_45.subStepTitle3 = "Cơ cấu mở khoang hành lý khẩn cấp"
        listSteps.add(stepOrinalStep6_45)

        return listSteps
    }

    fun dataStep7(): MutableList<StepOrinalModel> {
        var listSteps = mutableListOf<StepOrinalModel>()

        var stepOrinalStep7_1 = StepOrinalModel()
        stepOrinalStep7_1.step = "7"
        stepOrinalStep7_1.stepTitle = "Kiểm tra lái thử xe"
        stepOrinalStep7_1.subStep = "7.1"
        stepOrinalStep7_1.subStepTitle1 = "Lái thử"
        stepOrinalStep7_1.subStepTitle2 = "Khởi động"
        stepOrinalStep7_1.subStepTitle3 = "Hoạt động của ắc quy"
        listSteps.add(stepOrinalStep7_1)

        var stepOrinalStep7_2 = StepOrinalModel()
        stepOrinalStep7_2.step = "7"
        stepOrinalStep7_2.stepTitle = "Kiểm tra lái thử xe"
        stepOrinalStep7_2.subStep = "7.1"
        stepOrinalStep7_2.subStepTitle1 = "Lái thử"
        stepOrinalStep7_2.subStepTitle2 = "Khởi động"
        stepOrinalStep7_2.subStepTitle3 = "Hoạt động của bộ đề"
        listSteps.add(stepOrinalStep7_2)

        var stepOrinalStep7_3 = StepOrinalModel()
        stepOrinalStep7_3.step = "7"
        stepOrinalStep7_3.stepTitle = "Kiểm tra lái thử xe"
        stepOrinalStep7_3.subStep = "7.1"
        stepOrinalStep7_3.subStepTitle1 = "Lái thử"
        stepOrinalStep7_3.subStepTitle2 = "Khởi động"
        stepOrinalStep7_3.subStepTitle3 = "Hoạt động của đánh lửa"
        listSteps.add(stepOrinalStep7_3)

        var stepOrinalStep7_4 = StepOrinalModel()
        stepOrinalStep7_4.step = "7"
        stepOrinalStep7_4.stepTitle = "Kiểm tra lái thử xe"
        stepOrinalStep7_4.subStep = "7.1"
        stepOrinalStep7_4.subStepTitle1 = "Lái thử"
        stepOrinalStep7_4.subStepTitle2 = "Khởi động"
        stepOrinalStep7_4.subStepTitle3 = "Hoạt động phun nhiên liệu"
        listSteps.add(stepOrinalStep7_4)

        var stepOrinalStep7_5 = StepOrinalModel()
        stepOrinalStep7_5.step = "7"
        stepOrinalStep7_5.stepTitle = "Kiểm tra lái thử xe"
        stepOrinalStep7_5.subStep = "7.1"
        stepOrinalStep7_5.subStepTitle1 = "Lái thử"
        stepOrinalStep7_5.subStepTitle2 = "Khởi động"
        stepOrinalStep7_5.subStepTitle3 = "Hoạt động của hệ thống làm mát"
        listSteps.add(stepOrinalStep7_5)

        var stepOrinalStep7_6 = StepOrinalModel()
        stepOrinalStep7_6.step = "7"
        stepOrinalStep7_6.stepTitle = "Kiểm tra lái thử xe"
        stepOrinalStep7_6.subStep = "7.1"
        stepOrinalStep7_6.subStepTitle1 = "Lái thử"
        stepOrinalStep7_6.subStepTitle2 = "Khởi động"
        stepOrinalStep7_6.subStepTitle3 = "Hoạt động của hệ thống điều hòa"
        listSteps.add(stepOrinalStep7_6)

        var stepOrinalStep7_7 = StepOrinalModel()
        stepOrinalStep7_7.step = "7"
        stepOrinalStep7_7.stepTitle = "Kiểm tra lái thử xe"
        stepOrinalStep7_7.subStep = "7.1"
        stepOrinalStep7_7.subStepTitle1 = "Lái thử"
        stepOrinalStep7_7.subStepTitle2 = "Khởi động"
        stepOrinalStep7_7.subStepTitle3 = "Tiếng ồn khoang động cơ"
        listSteps.add(stepOrinalStep7_7)

        var stepOrinalStep7_8 = StepOrinalModel()
        stepOrinalStep7_8.step = "7"
        stepOrinalStep7_8.stepTitle = "Kiểm tra lái thử xe"
        stepOrinalStep7_8.subStep = "7.2"
        stepOrinalStep7_8.subStepTitle1 = "Lái thử"
        stepOrinalStep7_8.subStepTitle2 = "Chạy trên làn đường"
        stepOrinalStep7_8.subStepTitle3 = "Hệ thống lái"
        listSteps.add(stepOrinalStep7_8)

        var stepOrinalStep7_9 = StepOrinalModel()
        stepOrinalStep7_9.step = "7"
        stepOrinalStep7_9.stepTitle = "Kiểm tra lái thử xe"
        stepOrinalStep7_9.subStep = "7.2"
        stepOrinalStep7_9.subStepTitle1 = "Lái thử"
        stepOrinalStep7_9.subStepTitle2 = "Chạy trên làn đường"
        stepOrinalStep7_9.subStepTitle3 = "Hệ thống phanh"
        listSteps.add(stepOrinalStep7_9)

        var stepOrinalStep7_10 = StepOrinalModel()
        stepOrinalStep7_10.step = "7"
        stepOrinalStep7_10.stepTitle = "Kiểm tra lái thử xe"
        stepOrinalStep7_10.subStep = "7.2"
        stepOrinalStep7_10.subStepTitle1 = "Lái thử"
        stepOrinalStep7_10.subStepTitle2 = "Chạy trên làn đường"
        stepOrinalStep7_10.subStepTitle3 = "Hệ thống ly hợp"
        listSteps.add(stepOrinalStep7_10)

        var stepOrinalStep7_11 = StepOrinalModel()
        stepOrinalStep7_11.step = "7"
        stepOrinalStep7_11.stepTitle = "Kiểm tra lái thử xe"
        stepOrinalStep7_11.subStep = "7.2"
        stepOrinalStep7_11.subStepTitle1 = "Lái thử"
        stepOrinalStep7_11.subStepTitle2 = "Chạy trên làn đường"
        stepOrinalStep7_11.subStepTitle3 = "Tiếng ồn bất thường"
        listSteps.add(stepOrinalStep7_11)

        var stepOrinalStep7_12 = StepOrinalModel()
        stepOrinalStep7_12.step = "7"
        stepOrinalStep7_12.stepTitle = "Kiểm tra lái thử xe"
        stepOrinalStep7_12.subStep = "7.2"
        stepOrinalStep7_12.subStepTitle1 = "Lái thử"
        stepOrinalStep7_12.subStepTitle2 = "Chạy trên làn đường"
        stepOrinalStep7_12.subStepTitle3 = "Sang số"
        listSteps.add(stepOrinalStep7_12)

        var stepOrinalStep7_13 = StepOrinalModel()
        stepOrinalStep7_13.step = "7"
        stepOrinalStep7_13.stepTitle = "Kiểm tra lái thử xe"
        stepOrinalStep7_13.subStep = "7.2"
        stepOrinalStep7_13.subStepTitle1 = "Lái thử"
        stepOrinalStep7_13.subStepTitle2 = "Chạy trên làn đường"
        stepOrinalStep7_13.subStepTitle3 = "Tăng tốc"
        listSteps.add(stepOrinalStep7_13)

        var stepOrinalStep7_14 = StepOrinalModel()
        stepOrinalStep7_14.step = "7"
        stepOrinalStep7_14.stepTitle = "Kiểm tra lái thử xe"
        stepOrinalStep7_14.subStep = "7.3"
        stepOrinalStep7_14.subStepTitle1 = "Lái thử"
        stepOrinalStep7_14.subStepTitle2 = "Tiện ích khác"
        stepOrinalStep7_14.subStepTitle3 = "Điều khiển ga tự động"
        listSteps.add(stepOrinalStep7_14)

        var stepOrinalStep7_15 = StepOrinalModel()
        stepOrinalStep7_15.step = "7"
        stepOrinalStep7_15.stepTitle = "Kiểm tra lái thử xe"
        stepOrinalStep7_15.subStep = "7.3"
        stepOrinalStep7_15.subStepTitle1 = "Lái thử"
        stepOrinalStep7_15.subStepTitle2 = "Tiện ích khác"
        stepOrinalStep7_15.subStepTitle3 = "Lựa chọn vị trí ghế lái/Bộ nhớ"
        listSteps.add(stepOrinalStep7_15)

        return listSteps
    }

}
