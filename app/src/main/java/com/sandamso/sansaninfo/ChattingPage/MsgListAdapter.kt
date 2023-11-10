package com.sandamso.sansaninfo.ChattingPage

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sandamso.sansaninfo.Data.MessageData
import com.sandamso.sansaninfo.databinding.ListTalkItemMineBinding
import com.sandamso.sansaninfo.databinding.ListTalkItemOthersBinding

class MsgListAdapter(val msgList: MutableList<MessageData>, var currentUser : String) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // 채팅할 유저1, 유저2 viewType 상수 정의
    private val TYPE_USER1 = 0
    private val TYPE_USER2 = 1
    var currentNickName = ""
    private var firebaseDatabase = FirebaseDatabase.getInstance().reference

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_USER1 -> {
                val binding = ListTalkItemMineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                User1Holder(binding)
            }
            else -> {
                val binding = ListTalkItemOthersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                User2Holder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val room = msgList[position]
        val userId = currentUser
        currentUserNick(userId)
        Log.d("room id", "${room.msg}")
        Log.d("room id", "${room.userName}")
        Log.d("*******", "${currentNickName}")

        // 내가 채팅을 보낸 경우
        if(room.userName == currentNickName) {
            (holder as MsgListAdapter.User1Holder).setMsg(room)
        }
        // 다른 사용자가 채팅을 보낸 경우
        else {
            (holder as MsgListAdapter.User2Holder).setMsg(room)
        }
    }

    private fun currentUserNick(uid : String) {
        firebaseDatabase.child("users").child(uid).child("nickname")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()) {
                        val nickName = snapshot.getValue(String ::class.java)
                        if(nickName != null) {
                            currentNickName = nickName
                            Log.d("currentNickName", "$nickName")
                            notifyDataSetChanged()
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    override fun getItemCount(): Int {
        return msgList.size
    }
    override fun getItemViewType(position: Int): Int {
        val message = msgList[position]
        return if (message.userName == currentNickName) {
            TYPE_USER1
        } else {
            TYPE_USER2
        }
    }

    // 사용자 1의 viewHolder 클래스 정의
    inner class User1Holder(val binding: ListTalkItemMineBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setMsg(msg: MessageData) {
            with(binding) {
                txtMessage.text = msg.msg
                txtDate.text = msg.getFormattedTimestamp()
            }
        }
    }

    // 사용자 2의 viewHolder 클래스 정의
    inner class User2Holder(val binding: ListTalkItemOthersBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setMsg(msg: MessageData) {
            with(binding) {
                txtName.text = msg.userName
                txtMessage.text = msg.msg
                txtDate.text = msg.getFormattedTimestamp()
            }
        }
    }
}
