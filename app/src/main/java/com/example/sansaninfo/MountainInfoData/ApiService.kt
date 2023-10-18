package com.example.sansaninfo.MountainInfoData

import com.example.sansaninfo.BuildConfig
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface ApiService {
    @GET("getforeststoryservice")
    suspend fun getMountainInfo(
        @Query("mntnNm") mntName: String = "",
        @Query("mntnHght") mntHgt: String? = null,
        @Query("mntnAdd") mntRegion: String = "",
        @Query("mntnInfoAraCd") mntArea: String? = null,
        @Query("mntnInfoSsnCd") mntBackground: String? = null,
        @Query("mntnInfoThmCd") mntTheme: String? = null,
        @Query("ServiceKey") key: String? = "",
        @Query("pageNo") pageNo: String? = null,
        @Query("numOfRows") numOfRows: String? = null,
    ) : ApiResponse
}