package com.example.sansaninfo.API.Retrofit

import com.example.sansaninfo.API.Constrants.Constrant
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object WeatherClient {
    private val retrofit = Retrofit.Builder()
        .baseUrl(Constrant.WEATHER_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService  = retrofit.create(WeatherInterface::class.java)
}