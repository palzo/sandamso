package com.example.sansaninfo.CommunityPage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sansaninfo.Data.UserModel
import com.example.sansaninfo.R

class CommunityPageAdapter(val items: MutableList<UserModel>) :
    RecyclerView.Adapter<CommunityPageAdapter.ViewHolder>() {

    // View Holder를 생성하고 View를 붙여주는 메서드
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CommunityPageAdapter.ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.community_page_item, parent, false)
        return ViewHolder(v)
    }

    // 생성된 View Holder에 데이터를 바인딩 해주는 메서드
    override fun onBindViewHolder(holder: CommunityPageAdapter.ViewHolder, position: Int) {
        holder.bindItems(items[position])
    }

    // 데이터의 개수를 반환하는 메서드
    override fun getItemCount(): Int {
        return items.count()
    }

    // 화면에 표시 될 뷰를 저장하는 역할의 메서드
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(items: UserModel) {
            val title = itemView.findViewById<TextView>(R.id.community_item_tv_title)
            val nickname = itemView.findViewById<TextView>(R.id.community_item_tv_nickname)
            val date = itemView.findViewById<TextView>(R.id.community_item_tv_date)

            title.text = items.title
            nickname.text = items.nickname
            date.text = items.time
        }
    }
}