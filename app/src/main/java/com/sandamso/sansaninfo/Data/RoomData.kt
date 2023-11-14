package com.sandamso.sansaninfo.Data


data class RoomData (
    var id: String = "",
    var title: String = "",
    var users: Map<String, String> = mutableMapOf(),
    var postId: String = "",
    var userCount: Long = 1,
    var newMsg: Int = 0
)