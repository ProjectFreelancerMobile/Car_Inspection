package com.car_inspection.library

import android.content.Context
import android.content.res.Configuration

/**
 * Created by wanglei on 05/01/2018.
 */

object MiscUtils {
    fun isOrientationLandscape(context: Context): Boolean {
        val isOrientationLandscape: Boolean
        val orientation = context.resources.configuration.orientation
        when (orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> isOrientationLandscape = true
            Configuration.ORIENTATION_PORTRAIT -> isOrientationLandscape = false
            else -> isOrientationLandscape = false
        }
        return isOrientationLandscape
    }
}
