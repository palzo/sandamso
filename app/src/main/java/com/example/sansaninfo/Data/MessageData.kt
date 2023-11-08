package com.example.sansaninfo.Data

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MessageData {
    var id: String = ""
    var msg: String = ""
    var userName: String = ""
    var timestamp: Long = 0

    constructor()

    constructor(msg: String, userName: String) {
        this.msg = msg
        this.userName = userName
        this.timestamp = System.currentTimeMillis()
    }

    fun getFormattedTimestamp(): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        val sdf = SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.KOREA)
        return sdf.format(calendar.time)
    }
}