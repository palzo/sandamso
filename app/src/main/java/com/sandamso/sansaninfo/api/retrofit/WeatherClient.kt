package com.sandamso.sansaninfo.api.retrofit

import com.sandamso.sansaninfo.api.constants.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object WeatherClient {

    private val weatherRetrofit = Retrofit.Builder()
        .baseUrl(Constants.WEATHER_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val weatherNetwork: WeatherInterface by lazy {
        weatherRetrofit.create(WeatherInterface::class.java)
    }
}