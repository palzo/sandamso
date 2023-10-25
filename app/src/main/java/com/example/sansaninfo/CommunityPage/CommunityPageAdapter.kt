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

class CommunityPageAdapter :
    RecyclerView.Adapter<CommunityPageAdapter.ViewHolder>() {

    private val items = arrayListOf<PostModel>()

    // 화면 터치를 제어할 변수
    private var isBlockingTouch = false

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
            communityItemTvNickname.text =items.nickname
            communityItemTvDate.text = items.date

            // 로딩 중 화면 터치 막기
            if (isBlockingTouch) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }

            // 이미지 URI를 사용하여 이미지 표시하기
            val storage = FirebaseStorage.getInstance()
            val pathReference = storage.reference.child("images/${items.image}")

//            // 로딩 중 화면 터치 막기
//            blockLayoutTouch()

            pathReference.downloadUrl.addOnSuccessListener {
                // 프로그래스 바 숨기기
                if (!isBlockingTouch) {
                    // 프로그래스 바 숨기기
                    progressBar.visibility = View.GONE
                }
//                showProgress(false)
                Log.d("Image Tag222", "$it")
                communityItemIvImage.load(it)
//                // 화면 터치 풀기
//                clearBlockLayoutTouch()
            }.addOnFailureListener {
                // 실패할 경우에도 프로그래스 바 숨기기
                showProgress(false)
                Log.d("Image Tag333", "$it")
                clearBlockLayoutTouch()
            }
        }
    }

    fun showProgress(isShow: Boolean) {
        if (isShow) {
            isBlockingTouch = true
            notifyDataSetChanged()
        } else {
            isBlockingTouch = false
            notifyDataSetChanged()
        }
    }

    // 화면 터치 막기
    private fun blockLayoutTouch() {
        isBlockingTouch = true
        notifyDataSetChanged()
    }

    // 화면 터치 풀기
    private fun clearBlockLayoutTouch() {
        isBlockingTouch = false
        notifyDataSetChanged()
    }
}