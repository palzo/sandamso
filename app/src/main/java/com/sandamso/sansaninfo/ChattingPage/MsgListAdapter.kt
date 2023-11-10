package com.sandamso.sansaninfo.ChattingPage

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sandamso.sansaninfo.Data.MessageData
import com.sandamso.sansaninfo.databinding.ListTalkItemMineBinding
import com.sandamso.sansaninfo.databinding.ListTalkItemOthersBinding


class MsgListAdapter(val msgList: MutableList<MessageData>, val currentUser : String) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // 채팅할 유저1, 유저2 viewType 상수 정의
    private val TYPE_USER1 = 0
    private val TYPE_USER2 = 1

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
        Log.d("room id", "${room.id}")
        Log.d("room id", "${room.userName}")
        // 내가 채팅을 보낸 경우
        if(room.id == currentUser) {
            (holder as MsgListAdapter.User1Holder).setMsg(room)
        }
        // 다른 사용자가 채팅을 보낸 경우
        else {
            (holder as MsgListAdapter.User2Holder).setMsg(room)
        }
    }

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
