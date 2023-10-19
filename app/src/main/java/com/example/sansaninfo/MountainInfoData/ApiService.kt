package com.example.sansaninfo.MountainInfoData

import com.example.sansaninfo.BuildConfig
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("getforeststoryservice")
    suspend fun getMountainInfo(
        @Query("mntnNm") mntName: String = "",
        @Query("mntnHght") mntHgt: String? = "",
        @Query("mntnAdd") mntRegion: String = "",
        @Query("mntnInfoAraCd") mntArea: String? = "",
        @Query("mntnInfoSsnCd") mntBackground: String? = "",
        @Query("mntnInfoThmCd") mntTheme: String? = "",
        @Query("ServiceKey") key: String? = "",
        @Query("pageNo") pageNo: Int? = 1,
        @Query("numOfRows") numOfRows: Int? = 30
    ) : Response
}