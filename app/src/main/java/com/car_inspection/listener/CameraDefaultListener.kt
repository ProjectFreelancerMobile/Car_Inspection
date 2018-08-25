package com.car_inspection.listener

import java.io.File

interface CameraDefaultListener {
    fun showCameraDefault()
    fun uploadYoutube(path: String)
    fun uploadFileYoutube(path: File?)
}