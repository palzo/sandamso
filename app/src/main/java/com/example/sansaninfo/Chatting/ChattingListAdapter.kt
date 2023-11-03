package com.example.sansaninfo.Chatting

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sansaninfo.databinding.ChattingListItemBinding

class ChattingListAdapter: RecyclerView.Adapter<ChattingListAdapter.ViewHolder>() {

    private val list = arrayListOf<ChattingModel>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChattingListAdapter.ViewHolder {
        return ViewHolder(
            ChattingListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ChattingListAdapter.ViewHolder, position: Int) {
        return holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(private val binding: ChattingListItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(items: ChattingModel){

        }
    }
}