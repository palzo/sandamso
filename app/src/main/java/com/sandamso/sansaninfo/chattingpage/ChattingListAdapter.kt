package com.sandamso.sansaninfo.chattingpage

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sandamso.sansaninfo.data.RoomData
import com.sandamso.sansaninfo.databinding.ChattingListItemBinding


class ChattingListAdapter(val roomList: MutableList<RoomData>) :
    RecyclerView.Adapter<ChattingListAdapter.ViewHolder>() {

    // 채팅방 아이템 삭제하기
    interface OnItemLongClickListener {
        fun onItemLongClick(position: Int)
    }

    private var onItemLongClickListener: OnItemLongClickListener? = null

    fun setOnItemLongClickListener(listener: OnItemLongClickListener) {
        this.onItemLongClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ChattingListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(roomList[position])

        // 아이템을 길게 눌렀을 때 처리하기
        holder.itemView.setOnLongClickListener {
            onItemLongClickListener?.onItemLongClick(holder.adapterPosition)
            return@setOnLongClickListener true
        }
    }

    override fun getItemCount(): Int {
        return roomList.size
    }

    fun remove(position: Int) {
        roomList.removeAt(position)
        notifyItemRemoved(position)
    }


    inner class ViewHolder(private val binding: ChattingListItemBinding) : RecyclerView.ViewHolder(binding.root) {
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
        fun bind(room: RoomData) {
            with(binding){
//                val lastMessage = LastMessageManager.lastMessage
                txtMessageDate.text = room.deadlinedate
                txtName.text = room.title
                txtUserCount.text = room.userCount.toString()
                // 마지막 채팅
//                txtMessage.text = lastMessage.lastMsg
//                for(i in room.users){
//                    // 자신의 value가 1일 때
//                    if(i.key == currentUser && i.value == "1"){
//                        txtChatCount.visibility = View.VISIBLE
//                    }else{
//                        txtChatCount.visibility = View.INVISIBLE
//                    }
//                    break
//                }
            }
        }
    }
}