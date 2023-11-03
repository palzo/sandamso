package com.example.sansaninfo.Chatting

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChattingModel(
    val id: String?,
    val title: String?,
    val description: String?
): Parcelable