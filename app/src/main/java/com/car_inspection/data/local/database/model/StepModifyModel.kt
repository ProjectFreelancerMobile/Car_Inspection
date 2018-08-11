package com.car_inspection.data.local.database.model

import io.realm.RealmList
import io.realm.RealmObject

open class StepModifyModel: RealmObject(){
    var step:String? = null // 2
    var stepTitle:String? = null
    var subStep:String? = null // 2.1
    var subStepTitle1:String? = null //bên ngoài xe
    var subStepTitle2:String? = null //bên trái trước
    var subStepTitle3:String? = null //phía ngoài cửa xe
    var note:String? = null
    var imagepaths: RealmList<String>? = null
    var rating:String? = null // value = G,P or F
}