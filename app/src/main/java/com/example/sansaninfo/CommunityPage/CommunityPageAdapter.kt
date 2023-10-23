package com.example.sansaninfo.CommunityPage

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sansaninfo.Data.UserModel
import com.example.sansaninfo.R
import com.example.sansaninfo.databinding.CommunityPageItemBinding

class CommunityPageAdapter :
    RecyclerView.Adapter<CommunityPageAdapter.ViewHolder>() {

    private val items = arrayListOf<UserModel>()

    interface ItemClick {
        fun onClick(view: View, position: Int, model: UserModel)
    }

    var itemClick: ItemClick? = null

    fun setOnClickListener(listener: ItemClick) {
        itemClick = listener
    }

    // View Holder를 생성하고 View를 붙여주는 메서드
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CommunityPageAdapter.ViewHolder {
        return ViewHolder(
            CommunityPageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    }

    // 생성된 View Holder에 데이터를 바인딩 해주는 메서드
    override fun onBindViewHolder(holder: CommunityPageAdapter.ViewHolder, position: Int) {

        holder.itemView.setOnClickListener {
            itemClick?.onClick(it, position, items[position])
        }

        holder.bindItems(items[position])

    }

    // 데이터의 개수를 반환하는 메서드
    override fun getItemCount(): Int {
        return items.size
    }

    fun addItem(newitem: MutableList<UserModel>) {
        items.clear()
        items.addAll(newitem)
        notifyDataSetChanged()
    }

    // 화면에 표시 될 뷰를 저장하는 역할의 메서드
    inner class ViewHolder(private val binding: CommunityPageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItems(items: UserModel) = with(binding) {
            val title = itemView.findViewById<TextView>(R.id.community_item_tv_title)
            val nickname = itemView.findViewById<TextView>(R.id.community_item_tv_nickname)
            val date = itemView.findViewById<TextView>(R.id.community_item_tv_date)

            title.text = items.title
            nickname.text = items.nickname
            date.text = items.time

        }
    }
}