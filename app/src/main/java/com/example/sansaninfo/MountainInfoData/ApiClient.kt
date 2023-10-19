package com.example.sansaninfo.MountainInfoData

import com.example.sansaninfo.BuildConfig
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

object ApiClient {

    private const val BASE_URL = "http://api.forest.go.kr/openapi/service/trailInfoService/"

    private fun createOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()

        if (BuildConfig.DEBUG)
            interceptor.level = HttpLoggingInterceptor.Level.BODY
        else
            interceptor.level = HttpLoggingInterceptor.Level.NONE

        return OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .addNetworkInterceptor(interceptor)
            .build()
    }

    private val mntRetrofit = Retrofit.Builder()
        .baseUrl(BASE_URL).addConverterFactory(SimpleXmlConverterFactory.create()).client(
            createOkHttpClient()
        ).build()

// val mntNetwork: ApiService = mntRetrofit.create(ApiService::class.java)
    val mntNetwork: ApiService by lazy { mntRetrofit.create(ApiService::class.java) }

//    private fun buildOkHttpClient(): OkHttpClient =
//        OkHttpClient.Builder()
//            .addInterceptor(
//                //로깅 인터셉터
//                HttpLoggingInterceptor().apply {
//                    level = if (BuildConfig.DEBUG) {
//                        HttpLoggingInterceptor.Level.BODY
//                    } else {
//                        HttpLoggingInterceptor.Level.NONE
//                    }
//                }
//            )
//            .build()
//
//    private val apiService: ApiService by lazy {
//        Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(SimpleXmlConverterFactory.create())
//            .client(buildOkHttpClient())
//            .build()
//            .create(ApiService::class.java)
//    }
//
//
//
//    suspend fun getMountainInfo(MntName: String) {
//         apiService.getMountainInfo(MntName)
//    }

}