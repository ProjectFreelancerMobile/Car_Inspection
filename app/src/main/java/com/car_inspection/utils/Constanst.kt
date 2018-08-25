package com.car_inspection.utils

import com.jiangdg.usbcamera.UVCCameraHelper

class Constanst {
    companion object {
        val MAX_KEYWORD_LENGTH = 30
        val DEFAULT_KEYWORD = "ytdl"
        val SAVE_PATH: String = UVCCameraHelper.ROOT_PATH + "CarInspection"
        var CAR_CODE = ""

        fun getFolderPicturePath(): String = "$SAVE_PATH/Picture/"
        fun getFolderVideoPath(): String = "$SAVE_PATH/Record/"
    }
}