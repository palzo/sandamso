package com.example.sansaninfo.MountainInfoData

import com.google.gson.annotations.SerializedName

data class Header(
    @SerializedName("resultcode")
    val resultCode: Int,
    @SerializedName("resultmsg")
    val resultMsg: String
)