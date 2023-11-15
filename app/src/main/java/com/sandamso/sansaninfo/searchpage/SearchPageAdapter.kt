package com.sandamso.sansaninfo.searchpage

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.sandamso.sansaninfo.R
import com.sandamso.sansaninfo.databinding.SearchpageItemBinding
import kotlin.random.Random

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
            Log.d(
                "test",
                "mntName: ${model.mntName}, mntImgCode : ${model.mntImgCode}, mntImgURL : ${model.mntImgURL}"
            )
            if (model.mntImgURL == "") {
                val randomImg = randomImage()
                searchPageIvMnt.setImageResource(randomImg)
                model.mntImgCode = randomImg
            } else {
                val img = "https://www.forest.go.kr/images/data/down/mountain/" + model.mntImgURL
                searchPageIvMnt.load(img){
                    size(160, 130)
                }
            }
        }
    }
    fun itemsClear() {
        list.clear()
        notifyDataSetChanged()
    }
    private fun randomImage(): Int {
        return when (Random.nextInt(26)) {
            1 -> R.drawable.ic_mnt1
            2 -> R.drawable.ic_mnt2
            3 -> R.drawable.ic_mnt3
            4 -> R.drawable.ic_mnt4
            5 -> R.drawable.ic_mnt5
            6 -> R.drawable.ic_mnt6
            7 -> R.drawable.ic_mnt7
            8 -> R.drawable.ic_mnt8
            9 -> R.drawable.ic_mnt9
            10 -> R.drawable.ic_mnt10
            11 -> R.drawable.ic_mnt11
            12 -> R.drawable.ic_mnt12
            13 -> R.drawable.ic_mnt13
            14 -> R.drawable.ic_mnt14
            15 -> R.drawable.ic_mnt15
            16 -> R.drawable.ic_mnt16
            17 -> R.drawable.ic_mnt17
            18 -> R.drawable.ic_mnt18
            19 -> R.drawable.ic_mnt19
            20 -> R.drawable.ic_mnt20
            21 -> R.drawable.ic_mnt21
            22 -> R.drawable.ic_mnt22
            23 -> R.drawable.ic_mnt23
            24 -> R.drawable.ic_mnt24
            25 -> R.drawable.ic_mnt25
            else -> R.drawable.ic_mnt26
        }
    }
}