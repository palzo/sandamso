package com.sandamso.sansaninfo.Data

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FBRef {
    companion object {
        //firebase database 만들기
        val database = Firebase.database("https://sansaninfo-7819a-default-rtdb.firebaseio.com/")
        val myRef = database.getReference("POST")
    }
}