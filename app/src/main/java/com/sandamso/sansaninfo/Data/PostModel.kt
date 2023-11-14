package com.sandamso.sansaninfo.Data

data class PostModel(
    var id: String = "",
    var date: String = "",
    var nickname: String = "",
    var title: String = "",
    var maintext: String = "",
    var image: String = "",
    var mountain: String = "",
    var deadlinedate: String = "",
    var writer: String? = "",
    var roomId: String = "",
    var userCount: String = ""
)
