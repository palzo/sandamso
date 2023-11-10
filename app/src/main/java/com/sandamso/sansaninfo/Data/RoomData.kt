package com.sandamso.sansaninfo.Data


data class RoomData (
    var id: String = "",
    var title: String = "",
    var users: MutableMap<String, Boolean> = HashMap(),
    var postId: String = ""
)