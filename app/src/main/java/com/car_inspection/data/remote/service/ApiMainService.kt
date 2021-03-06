package com.car_inspection.data.remote.service

import com.car_inspection.data.model.inspection.ListInspection
import com.car_inspection.data.model.inspectiondetails.InspectionDetails
import com.car_inspection.data.model.login.Login
import com.car_inspection.data.model.senddata.SendInSpection
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by ToanDev on 28/2/18.
 * Email:Huynhvantoan.itc@gmail.com
 * REST API access points
 */

interface ApiMainService {
    //////////////////////////////
    //YAHOO SERVICE
    //https://query.yahooapis.com/v1/public/yql?q=%s&format=json

    @POST("login")
    fun login(@Body body: RequestBody): Call<Login>

    @GET("inspection")
    fun getListInspection(): Call<ListInspection>

    @GET("inspection/{id}")
    fun getInspectionDetails(@Path("id") id: String): Call<InspectionDetails>

    @PUT("inspection/{id}")
    fun sendDetails(@Path("id") id: String): Call<SendInSpection>
}
