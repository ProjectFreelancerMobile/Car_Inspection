package com.car_inspection.listener

interface CameraRecordListener {
    fun recordEvent(isTake: Boolean = false, step: String = "")
}