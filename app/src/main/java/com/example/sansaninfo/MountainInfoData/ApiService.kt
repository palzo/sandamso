package com.example.sansaninfo.MountainInfoData

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("getforeststoryservice")
    fun getMountainInfo(
        @Query("mntnNm") mntName: String = "",
        @Query("mntnHght") mntHgt: String? = "",
        @Query("mntnAdd") mntRegion: String = "",
        @Query("mntnInfoAraCd") mntArea: String? = "",
        @Query("mntnInfoSsnCd") mntBackground: String? = "",
        @Query("mntnInfoThmCd") mntTheme: String? = "",
        @Query("ServiceKey") key: String? = "",
        @Query("pageNo") pageNo: Int? = 1,
        @Query("numOfRows") numOfRows: Int? = 100
    ) : Call<XmlResponse?>?
}