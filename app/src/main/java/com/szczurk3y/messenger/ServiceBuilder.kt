package com.szczurk3y.messenger

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ServiceBuilder {
//    val BASE_URL = "http://10.0.2.2:1234/api/user/" // ANDROID STUDIO
    val BASE_URL = "http://192.168.1.27:1235/api/user/" // MY PHONE

    var mInstance: ServiceBuilder? = null
    var retrofit: Retrofit

    init {
        val gson = GsonBuilder()
            .setLenient()
            .create()
          retrofit = Retrofit.Builder()
              .baseUrl(BASE_URL)
              .addConverterFactory(GsonConverterFactory.create(gson))
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