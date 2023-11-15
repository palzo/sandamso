package com.sandamso.sansaninfo.data

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FBRoom {
    companion object {
        //firebase database 만들기
        val database = Firebase.database("https://sansaninfo-7819a-default-rtdb.firebaseio.com/")
        val roomRef = database.getReference("Rooms")
    }
}