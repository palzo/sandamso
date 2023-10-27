package com.example.sansaninfo.API.Retrofit

import com.example.sansaninfo.API.ModelData.Weather
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherInterface {
    @GET("getVilageFcst")
    fun getWeatherInfo(
        @Query("serviceKey") serviceKey : String,
        @Query("dataType") dataType : String = "JSON",
        // 한 페이지 결과 수
        @Query("numOfRows") numOfRows : Int,
        @Query("pageNo") pageNo : Int = 1,
        // 발표 일자 ex) 20231026
        @Query("base_date") baseDate : Int,
        // 발표 시간 ex) 0500, 05시발표
        @Query("base_time") baseTime : String,
        @Query("nx") nx : Int,
        @Query("ny") ny : Int
    ) : Call<Weather>
}