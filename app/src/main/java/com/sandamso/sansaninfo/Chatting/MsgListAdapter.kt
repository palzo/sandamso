package com.sandamso.sansaninfo.Chatting

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.sandamso.sansaninfo.Data.MessageData
import com.sandamso.sansaninfo.databinding.ListTalkItemMineBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase


class MsgListAdapter(val msgList: MutableList<MessageData>) :
    RecyclerView.Adapter<MsgListAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            ListTalkItemMineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val msg = msgList.get(position)
        holder.setMsg(msg)
    }

    override fun getItemCount(): Int {
        return msgList.size
    }

    inner class Holder(val binding: ListTalkItemMineBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setMsg(msg: MessageData) {
            with(binding) {
                setNickname { nickname ->
                    txtName.text = nickname
                }
                txtMessage.text = msg.msg
                txtDate.text = msg.getFormattedTimestamp()
            }
        }
    }

    // Firebase에서 닉네임 가져오기
    private fun setNickname(onNickNameFetched: (String) -> Unit) {
        val userReference = FirebaseDatabase.getInstance().getReference("users")
        val uid = Firebase.auth.currentUser?.uid ?: ""
        if(uid.isNotEmpty()){
            userReference.child(uid).addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val nickname = snapshot.child("nickname").getValue(String::class.java)
                    if(!nickname.isNullOrEmpty()){
                        Log.d("test1", "$nickname")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }else{

        }
    }
}
