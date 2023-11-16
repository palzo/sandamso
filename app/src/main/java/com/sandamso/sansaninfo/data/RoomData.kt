package com.sandamso.sansaninfo.data


data class RoomData (
    var id: String = "",
    var title: String = "",
    var users: MutableMap<String, String> = mutableMapOf(),
    var postId: String = "",
    var userCount: Long = 1,
    var newMsg: Int = 0,
    var deadlinedate: String = "",
    var lastMessage: String = ""
)