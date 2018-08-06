package com.car_inspection.data.remote.service

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

/**
 * Created by ToanDev on 28/2/18.
 * Email:Huynhvantoan.itc@gmail.com
 * REST API access points
 */

interface ApiMainService {
    //////////////////////////////
    //YAHOO SERVICE
    //https://query.yahooapis.com/v1/public/yql?q=%s&format=json

    @GET
    fun getWeather(@Url link: String): Call<ResponseBody>


}
