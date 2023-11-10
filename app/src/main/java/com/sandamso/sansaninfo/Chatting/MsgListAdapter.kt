package com.sandamso.sansaninfo.Chatting

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
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
import com.sandamso.sansaninfo.R
import com.sandamso.sansaninfo.databinding.ListTalkItemOthersBinding


class MsgListAdapter(val msgList: MutableList<MessageData>, val currentUser : String) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // 채팅할 유저1, 유저2 viewType 상수 정의
    private val TYPE_USER1 = 0
    private val TYPE_USER2 = 1

/*    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            ListTalkItemMineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        /*val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1,parent,false)
        return ViewHolder(view)*/
        return when (viewType) {
            TYPE_USER1 -> {
                val binding = ListTalkItemOthersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                User1Holder(binding)
            }
            else -> {
                val binding = ListTalkItemMineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                User2Holder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val room = msgList[position]
        // 내가 채팅을 보낸 경우
        if(room.id == currentUser) {
            (holder as MsgListAdapter.User1Holder).setMsg(room)
        }
        // 다른 사용자가 채팅을 보낸 경우
        else {
            (holder as MsgListAdapter.User2Holder).setMsg(room)
        }
    }

/*    override fun onBindViewHolder(holder: Holder, position: Int) {
        val msg = msgList.get(position)
        holder.setMsg(msg)
    }*/

    override fun getItemCount(): Int {
        return msgList.size
    }

    override fun getItemViewType(position: Int): Int {
        val message = msgList[position]
        return if (message.id == currentUser) {
            TYPE_USER1
        } else {
            TYPE_USER2
        }
    }

    /*inner class Holder(val binding: ListTalkItemMineBinding) :
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
    }*/

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

    // 사용자 1의 viewHolder 클래스 정의
    inner class User1Holder(val binding: ListTalkItemOthersBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setMsg(msg: MessageData) {
            with(binding) {
                txtMessage.text = msg.msg
                txtDate.text = msg.getFormattedTimestamp()
            }
        }
    }

    // 사용자 2의 viewHolder 클래스 정의
    inner class User2Holder(val binding: ListTalkItemMineBinding) : RecyclerView.ViewHolder(binding.root) {
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
}
