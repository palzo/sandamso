package com.example.sansaninfo

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application(){
    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this, "102f27e4599b2bdb62b23c03df265bf3")
    }
}