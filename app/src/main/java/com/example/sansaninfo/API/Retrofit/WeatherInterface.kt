package com.example.sansaninfo.API.Retrofit

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherInterface {
    @GET("getVilageFcst")
    suspend fun getWeatherInfo(
        @Query("serviceKey") serviceKey : String,
        @Query("pageNo") pageNo : String,
        @Query("dataType") dataType : String,
        @Query("base_date") base_data : String,
        @Query("base_time") base_time : String,
        @Query("nx") nx : String,
        @Query("ny") ny : String
    )
}