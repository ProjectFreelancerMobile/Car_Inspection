package com.car_inspection.listener

interface CameraRecordListener {
    fun recordEvent(isTake: Boolean = false, step: Int = 2, subStep: String = "")
    fun capture()
}