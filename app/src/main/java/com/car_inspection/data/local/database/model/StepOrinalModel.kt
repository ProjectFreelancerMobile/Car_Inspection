package com.car_inspection.data.local.database.model

import io.realm.RealmObject

open class StepOrinalModel: RealmObject() {
    var step:String? = null // 2
    var stepTitle:String? = null
    var subStep:String? = null // 2.1
    var subStepTitle1:String? = null //bên ngoài xe
    var subStepTitle2:String? = null //bên trái trước
    var subStepTitle3:String? = null //phía ngoài cửa xe
}