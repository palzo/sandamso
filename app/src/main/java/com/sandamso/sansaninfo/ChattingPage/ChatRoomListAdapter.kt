package com.sandamso.sansaninfo.ChattingPage

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sandamso.sansaninfo.Data.RoomData

class ChatRoomListAdapter(val roomList: MutableList<RoomData>) :
    RecyclerView.Adapter<ChatRoomListAdapter.ViewHolder>() {

    // 채팅방 아이템 삭제하기
    interface OnItemLongClickListener {
        fun onItemLongClick(position: Int)
    }

    private var onItemLongClickListener: OnItemLongClickListener? = null

    fun setOnItemLongClickListener(listener: OnItemLongClickListener) {
        this.onItemLongClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val room = roomList[position]
        holder.setRoom(room)

        // 아이템을 길게 눌렀을 때 처리하기
        holder.itemView.setOnLongClickListener {
            onItemLongClickListener?.onItemLongClick(holder.adapterPosition)
            return@setOnLongClickListener true
        }
    }

    override fun getItemCount(): Int {
        return roomList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //        lateinit var mRoom: RoomData
        init {
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, ChatRoomActivity::class.java)
                intent.putExtra("roomId", roomList[adapterPosition].id)
                intent.putExtra("dataFromdetailPageTitle", roomList[adapterPosition].title)
                itemView.context.startActivity(intent)
            }
            itemView.setOnLongClickListener {
                onItemLongClickListener?.onItemLongClick(adapterPosition)
                return@setOnLongClickListener true
            }
        }

        fun setRoom(room: RoomData) {
            itemView.findViewById<TextView>(android.R.id.text1).text = room.title
        }
    }
}