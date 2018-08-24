package com.car_inspection.di.module

import com.car_inspection.BuildConfig
import com.car_inspection.data.remote.service.ApiMainService
import com.car_inspection.di.MainUrl
import com.car_inspection.utils.isDebug
import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import com.toan_itc.core.architecture.LiveDataCallAdapterFactory
import dagger.Module
import dagger.Provides
import io.realm.RealmObject
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by ToanDev on 28/2/18.
 * Email:Huynhvantoan.itc@gmail.com
 */

@Suppress("unused")
@Module
internal class NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val client = OkHttpClient.Builder()
        client.connectTimeout(30,TimeUnit.SECONDS)
        client.readTimeout(30,TimeUnit.SECONDS)
        client.addInterceptor(LoggingInterceptor.Builder()
                .loggable(isDebug())
                .setLevel(Level.BASIC)
                .log(Platform.INFO)
                .tag("LoggingI")
                .request("request")
                .response("response")
                .build())
        return client.build()
    }

    @Singleton
    @Provides
    fun providesGson(): Gson {
        val builder = GsonBuilder()
        builder.setExclusionStrategies(object : ExclusionStrategy {
            override fun shouldSkipField(f: FieldAttributes): Boolean {
                return f.declaringClass == RealmObject::class.java
            }

            override fun shouldSkipClass(clazz: Class<*>): Boolean {
                return false
            }
        })
        return builder.create()
    }

    @Singleton
    @Provides
    fun sRestMainClient(@MainUrl retrofit: Retrofit): ApiMainService = retrofit.create(ApiMainService::class.java)

    @Singleton
    @Provides
    @MainUrl
    fun provideMainRetrofit(client: OkHttpClient, gson: Gson): Retrofit = createRetrofit(client, gson, BuildConfig.BASE_PATH)

    @Singleton
    @Provides
    @Named("Retrofit")
    fun createRetrofit(okHttpClient: OkHttpClient, gson: Gson, baseUrl: String): Retrofit {
        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addCallAdapterFactory(LiveDataCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
    }
}
