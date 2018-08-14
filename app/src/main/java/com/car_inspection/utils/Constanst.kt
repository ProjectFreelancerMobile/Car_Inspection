package com.car_inspection.utils

import android.os.Environment

class Constanst {
    companion object {
        val SAVE_PATH: String = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath
        var CAR_CODE = ""

        fun getFolderPicturePath(): String = SAVE_PATH + "/" + CAR_CODE
    }
}