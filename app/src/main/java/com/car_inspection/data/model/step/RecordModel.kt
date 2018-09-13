package com.car_inspection.data.model.step

/**
 * Created by wanglei02 on 2016/1/22.
 */
class RecordModel {
    var startTimestamp: Long = 0
    var endTimestamp: Long = 0

    val duration: Long
        get() = endTimestamp - startTimestamp
}
