package com.example.sansaninfo.API.Retrofit

import com.example.sansaninfo.API.Constants.Constants
import com.example.sansaninfo.BuildConfig
import com.example.sansaninfo.MountainInfoAPI.ApiClient
import com.example.sansaninfo.MountainInfoAPI.ApiService
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
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