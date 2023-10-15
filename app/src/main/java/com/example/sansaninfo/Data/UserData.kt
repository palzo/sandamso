package com.example.sansaninfo.Data

//사용자 정보에 간단하게 이름, 아이디, 성별, 닉네임 DB 생성
data class UserData(
    val name : String,
    val email : String,
    val gender : String,
    val nickname : String
)
