package com.szczurk3y.messenger

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ServiceBuilder {
    val BASE_URL = "http://10.0.2.2:1234/api/user/"
    var mInstance: ServiceBuilder? = null
    var retrofit: Retrofit

    init {
          retrofit = Retrofit.Builder()
              .baseUrl(BASE_URL)
              .addConverterFactory(GsonConverterFactory.create())
              .build()
    }

    @Synchronized fun getInstance(): ServiceBuilder {
        if (mInstance == null) mInstance = ServiceBuilder()
        return mInstance as ServiceBuilder
    }

    fun getService(): MessengerService {
        return retrofit.create(MessengerService::class.java)
    }


}