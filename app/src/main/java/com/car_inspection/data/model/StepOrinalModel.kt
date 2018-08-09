package com.car_inspection.data.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class StepOrinalModel: RealmObject() {
    @PrimaryKey
    var step:String? = null // 2
    var subStep:String? = null // 2.1
    var subStepTitle1:String? = null //bên ngoài xe
    var subStepTitle2:String? = null //bên trái trước
    var subStepTitle3:String? = null //phía ngoài cửa xe
}