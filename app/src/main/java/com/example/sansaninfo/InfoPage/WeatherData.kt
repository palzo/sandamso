package com.example.sansaninfo.InfoPage

data class WeatherData(
    val baseDate: String,
    val baseTime: String,
    val category: String,
    val nx: Long,
    val ny: Long,
    val tmp : String
)
