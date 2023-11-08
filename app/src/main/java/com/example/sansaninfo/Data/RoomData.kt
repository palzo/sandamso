package com.example.sansaninfo.Data


class RoomData {
    var id: String = ""
    var title: String = ""
    var users: String = ""

    constructor()

    constructor(title: String, creatorName: String) {
        this.title = title
        users = creatorName
    }
}