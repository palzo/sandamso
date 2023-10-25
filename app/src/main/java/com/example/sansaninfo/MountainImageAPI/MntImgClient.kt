package com.example.sansaninfo.MountainImageAPI

import com.example.sansaninfo.BuildConfig
import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.create

object MntImgClient {

    private const val BASE_URL = "https://apis.data.go.kr/1400000/service/cultureInfoService2/"

    private fun createOkHttpClient() : OkHttpClient {
        val interceptor = HttpLoggingInterceptor()

        if(BuildConfig.DEBUG)
            interceptor.level = HttpLoggingInterceptor.Level.BODY
        else
            interceptor.level = HttpLoggingInterceptor.Level.NONE

        return OkHttpClient.Builder()
            .addNetworkInterceptor(interceptor)
            .build()
    }

    private val mntRetrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(TikXmlConverterFactory.create(TikXml.Builder().exceptionOnUnreadXml(false).build()))
        .client(createOkHttpClient())
        .build()

    val mntNetwork: MntImgService by lazy { mntRetrofit.create(MntImgService::class.java) }

}