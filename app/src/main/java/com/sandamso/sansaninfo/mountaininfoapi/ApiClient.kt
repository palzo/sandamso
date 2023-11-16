package com.sandamso.sansaninfo.mountaininfoapi

import com.sandamso.sansaninfo.BuildConfig
import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
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
        .baseUrl(BASE_URL)
        .addConverterFactory(TikXmlConverterFactory.create(TikXml.Builder().exceptionOnUnreadXml(false).build()))
        .client(createOkHttpClient())
        .build()

    val mntNetwork: ApiService by lazy { mntRetrofit.create(ApiService::class.java) }
}