package com.sandamso.sansaninfo.ChattingPage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sandamso.sansaninfo.Data.MessageData
import com.sandamso.sansaninfo.databinding.ListTalkItemMineBinding


class MsgListAdapter(val msgList: MutableList<MessageData>) :
    RecyclerView.Adapter<MsgListAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            ListTalkItemMineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val msg = msgList[position]
        holder.setMsg(msg)
    }

    override fun getItemCount(): Int {
        return msgList.size
    }

    inner class Holder(val binding: ListTalkItemMineBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setMsg(msg: MessageData) {
            with(binding) {
                txtName.text = msg.userName
                txtMessage.text = msg.msg
                txtDate.text = msg.getFormattedTimestamp()
            }
        }
    }
}
