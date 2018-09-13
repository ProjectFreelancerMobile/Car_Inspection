package com.car_inspection.data.model.step

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class StepOrinalModel: RealmObject() {
    @PrimaryKey
    var step:String? = null // 2
    var stepTitle:String? = null
    var subStep:String? = null // 2.1
    var subStepTitle1:String? = null //bên ngoài xe
    var subStepTitle2:String? = null //bên trái trước
    var subStepTitle3:String? = null //phía ngoài cửa xe
    var canIgnore = false
}