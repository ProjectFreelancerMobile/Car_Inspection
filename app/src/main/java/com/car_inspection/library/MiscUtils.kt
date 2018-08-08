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
        isOrientationLandscape = when (orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> true
            Configuration.ORIENTATION_PORTRAIT -> false
            else -> false
        }
        return isOrientationLandscape
    }
}
