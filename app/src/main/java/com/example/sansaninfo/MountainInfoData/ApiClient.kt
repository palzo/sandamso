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
import java.util.concurrent.TimeUnit

object ApiClient {

    private const val BASE_URL = "http://api.forest.go.kr/openapi/service/trailInfoService/"

//    private val retrofit: Retrofit by lazy {
//
//        val loggingInterceptor = HttpLoggingInterceptor().apply {
//            level = HttpLoggingInterceptor.Level.BODY
//        }
//
//        val client = OkHttpClient.Builder()
//            .addInterceptor(loggingInterceptor)
//            .addInterceptor{ chain ->
//                val request = chain.request()
//                val response = chain.proceed(request)
//                response
//            }
//            .build()
//        var gson= GsonBuilder().setLenient().create()
//        Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(SimpleXmlConverterFactory.create())
//            .client(client)
//            .build()
//
//    }
//        val apiService: ApiService by lazy {
//        retrofit.create(ApiService::class.java)
//    }

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

 val mntNetWork: ApiService = mntRetrofit.create(ApiService::class.java)

}