package com.sandamso.sansaninfo

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sandamso.sansaninfo.databinding.ActivityUserPopUpBinding

class UserPopUp(context: Context, enterRoom: DatabaseReference) : Dialog(context) {
    private val binding by lazy { ActivityUserPopUpBinding.inflate(layoutInflater) }
    private val roomReference: DatabaseReference = enterRoom.child("users")
    private val list = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding) {
            userPopupBtn.setOnClickListener {
                dismiss()
            }
            roomReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (userId in snapshot.children) {
                        Log.d("popup", "${userId.key}")
                        searchNickname(userId.key)

                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }

    private fun searchNickname(uid: String?) {
        uid?.let {
            FirebaseDatabase.getInstance().reference.child("users").child(uid)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        // 사용자의 닉네임 가져오기
                        val nickname = snapshot.child("nickname").getValue(String::class.java)

                        // 가져온 닉네임이 null이 아니면 리스트에 추가
                        nickname?.let {
                            list.add(it)
                        }
                        addUser()
                    }
                    override fun onCancelled(error: DatabaseError) {
                    }
                })
        }
    }

    @SuppressLint("SetTextI18n")
    private fun addUser(){
        var cnt = 0
        var txt = ""
        list.forEach {
            txt += it + "\n"
            cnt++
            binding.userPopupTvUserCount.text = "($cnt)"
        }
        binding.userPopupUserNickname.text = txt
    }
}