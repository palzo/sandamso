package com.example.sansaninfo.API.Retrofit

import com.example.sansaninfo.API.Constrants.Constrant
import com.example.sansaninfo.API.ModelData.Root
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherInterface {
    @GET("getVilageFcst?serviceKey=${Constrant.WEATHER_API_KEY}")
    suspend fun getWeatherInfo(
        //@Query("serviceKey") serviceKey : String,
        @Query("dataType") dataType : String,
        @Query("numOfRows") numOfRows : String,
        @Query("pageNo") pageNo : String,
        @Query("base_date") baseData : String,
        @Query("base_time") baseTime : String,
        @Query("nx") nx : String,
        @Query("ny") ny : String
    ) : Response<Root>
}