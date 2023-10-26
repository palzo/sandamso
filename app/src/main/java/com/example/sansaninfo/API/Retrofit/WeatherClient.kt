package com.example.sansaninfo.API.Retrofit

import com.example.sansaninfo.API.Constrants.Constrant
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object WeatherClient {
    val apiService : WeatherInterface
        get() = retrofit.create(WeatherInterface::class.java)

    private val gson = GsonBuilder().setLenient().create()

    private val retrofit = Retrofit.Builder()
        .baseUrl(Constrant.WEATHER_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
}