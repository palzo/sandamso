package com.example.sansaninfo.CommunityPage

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.sansaninfo.Data.PostModel
import com.example.sansaninfo.databinding.CommunityPageItemBinding
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CommunityPageAdapter :
    RecyclerView.Adapter<CommunityPageAdapter.ViewHolder>() {

    private val items = arrayListOf<PostModel>()

    interface ItemClick {
        fun onClick(view: View, position: Int, model: PostModel)
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

    fun addItem(newitem: MutableList<PostModel>) {
        items.clear()
        items.addAll(newitem)
        notifyDataSetChanged()
    }

    // 화면에 표시 될 뷰를 저장하는 역할의 메서드
    inner class ViewHolder(private val binding: CommunityPageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItems(items: PostModel) = with(binding) {
            communityItemTvTitle.text = items.title
            communityItemTvNickname.text = items.nickname
            communityItemTvDate.text = items.date
            communityItemTvDday.text = calculateDday(items)

            // 이미지 URI를 사용하여 이미지 표시하기
            val storage = FirebaseStorage.getInstance()
            val pathReference = storage.reference.child("images/${items.image}")

            pathReference.downloadUrl.addOnSuccessListener {
                Log.d("Image Tag222", "$it")
                communityItemIvImage.load(it)
            }.addOnFailureListener {
                Log.d("Image Tag333", "$it")
            }
        }
    }

    // 디데이 계산하기
    fun calculateDday(items: PostModel): String {
        val dateData = items.deadlinedate

        if (dateData.isEmpty()) {
            return "날짜 없음"
        }

        val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA)
        val currentDate = Date()
        val targetDate = dateFormat.parse(dateData)

        if (targetDate != null) {
            val timeDiff = targetDate.time - currentDate.time
            val dday = timeDiff / (1000 * 60 * 60 * 24)

            if (dday.toInt() == 0) {
                return "D-Day"
            } else if (dday > 0) {
                return "D-${dday}"
            } else {
                val outday = -dday
                return "D+${outday}"
            }
        } else {
            return "유효하지 않은 날짜"
        }
    }
    fun setCalculateDday(items: PostModel): Int {
        val dateData = items.deadlinedate

        if (dateData.isEmpty()) {
            return Int.MAX_VALUE // D-day가 없으면 아주 큰 값 반환
        }

        val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA)
        val currentDate = Date()
        val targetDate = dateFormat.parse(dateData)

        if (targetDate != null) {
            val timeDiff = targetDate.time - currentDate.time
            val dday = timeDiff / (1000 * 60 * 60 * 24)

            return dday.toInt()
        } else {
            return Int.MAX_VALUE // 유효하지 않은 날짜이면 아주 큰 값 반환
        }
    }


    fun setItems(item : List<PostModel>) {
        items.clear()
        items.addAll(item)
        notifyDataSetChanged()
    }
}