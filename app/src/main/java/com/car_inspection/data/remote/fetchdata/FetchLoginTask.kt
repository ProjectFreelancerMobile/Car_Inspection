package com.car_inspection.data.remote.fetchdata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.car_inspection.data.local.prefs.pref
import com.car_inspection.data.model.query.LoginQuery
import com.car_inspection.data.remote.service.ApiMainService
import com.car_inspection.define.PrefDef
import com.car_inspection.utils.returnBody
import com.google.gson.Gson
import com.toan_itc.core.architecture.*
import retrofit2.http.Field

/**
 * Created by ToanDev on 04/06/18.
 * Email:Huynhvantoan.itc@gmail.com
 */

class FetchLoginTask
internal constructor(private val apiService: ApiMainService, private var email: String="", private var password: String="") : Runnable {
    private val liveData = MutableLiveData<Resource<Boolean>>()
    private val messageError = "Đăng nhập lỗi vui lòng kiểm tra!"
    override fun run() {
        liveData.postValue(Resource.loading(null))
        val newValue = try {
            val query = Gson().toJson(LoginQuery(email, password))
            val response = apiService.login(returnBody(query)).execute()
            val apiResponse = ApiResponse.create(response)
            when (apiResponse) {
                is ApiSuccessResponse -> {
                    val token = response.body()?.token?:""
                    if (token.isNotEmpty()) {
                        pref {
                            put(PrefDef.PRE_TOKEN, token)
                        }
                        Resource.success(true)
                    } else
                        Resource.error(messageError, false)
                }
                is ApiEmptyResponse -> Resource.error(messageError,false)
                is ApiErrorResponse -> Resource.error(messageError, false)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.error(messageError, false)
        }
        liveData.postValue(newValue)
    }

    internal fun getLiveData(): LiveData<Resource<Boolean>> {
        return liveData
    }

}
