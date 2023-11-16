package com.sandamso.sansaninfo.api.modeldata

data class Weather(val response: Response)

data class Response(
    val header: Header,
    val body: Body,
)

data class Header(
    val resultCode: String,
    val resultMsg: String,
)

data class Body(
    val dataType: String,
    val items: Items,
    val pageNo: Int,
    val numOfRows: Int,
    val totalCount: Int,
)

data class Items(
    val item: List<Item>,
)

data class Item(
    val baseDate: String,
    val baseTime: String,
    val category: String,
    val fcstTime: String,
    val fcstDate: String,
    val fcstValue: String,
    val nx: Int,
    val ny: Int,
)

