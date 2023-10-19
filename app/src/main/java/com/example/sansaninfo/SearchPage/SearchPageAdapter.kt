package com.example.sansaninfo.SearchPage

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.sansaninfo.databinding.SearchpageItemBinding

class SearchPageAdapter : RecyclerView.Adapter<SearchPageAdapter.SearchViewHolder>() {

    interface ItemClick {
        fun onClick(view: View, position: Int, model: MntModel)
    }

    var itemClick: ItemClick? = null

    fun setOnClickListener(listener: ItemClick) {
        itemClick = listener
    }

    private val list = arrayListOf<MntModel>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchPageAdapter.SearchViewHolder {
        return SearchViewHolder(
            SearchpageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: SearchPageAdapter.SearchViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            itemClick?.onClick(it, position, list[position])
            println(position)
        }
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun addItems(newItems: List<MntModel>) {
        list.clear()
        list.addAll(newItems)
        notifyDataSetChanged()
    }

    inner class SearchViewHolder(private val binding: SearchpageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: MntModel) = with(binding) {
                searchPageTvMnt.text = model.mntName
        }
    }
    fun itemsClear(){
        list.clear()
        notifyDataSetChanged()
    }
}