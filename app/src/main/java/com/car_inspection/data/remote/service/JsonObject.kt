package com.car_inspection.data.remote.service

import com.google.gson.annotations.Expose

/**
 * Created by ToanDev on 28/2/18.
 * Email:Huynhvantoan.itc@gmail.com
 */

class JsonObject<T> {
    @Expose
    var result: T? = null
}

