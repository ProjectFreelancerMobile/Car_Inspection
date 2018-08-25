package com.car_inspection.utils

import com.jiangdg.usbcamera.UVCCameraHelper

class Constanst {
    companion object {
        private val SAVE_PATH: String = UVCCameraHelper.ROOT_PATH +"CarInspection"
        var CAR_CODE = ""

        fun getFolderPicturePath(): String = "$SAVE_PATH/Picture/"
        fun getFolderVideoPath(): String = "$SAVE_PATH/Record/"
    }
}