package com.sandamso.sansaninfo.mountainimageapi

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MntImgService {
    @GET("mntInfoImgOpenAPI2")
    fun getMntImgCode(
        @Query("mntiListNo") mntCodeNum : Int = 0,
        @Query("pageNo") pageNo: String = "",
        @Query("numOfRows") numOfRows : String = "",
        @Query("ServiceKey") key: String = ""
    ): Call<ImgResponse>
}