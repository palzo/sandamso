package com.sandamso.sansaninfo.CommunityPage

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.sandamso.sansaninfo.AddPage.AddPageActivity
import com.sandamso.sansaninfo.Data.PostModel
import com.sandamso.sansaninfo.Data.UserData
import com.sandamso.sansaninfo.DetailPage.DetailPageActivity
import com.sandamso.sansaninfo.R
import com.sandamso.sansaninfo.databinding.FragmentCommunityPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CommunityPageFragment : Fragment() {

    private lateinit var binding: FragmentCommunityPageBinding
    private lateinit var auth: FirebaseAuth
    private val communityList = mutableListOf<PostModel>()
    private val communityPageAdapter by lazy { CommunityPageAdapter() }
    private var firebaseDatabase = FirebaseDatabase.getInstance().reference

    // 마감일 지난 글 옵션 필터 체크 여부
    private var isDeadlineOption = false

    companion object {
        fun newInstance() = CommunityPageFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //FirebaseAuth 연결
        auth = FirebaseAuth.getInstance()

        // Inflate the layout for this fragment
        binding = FragmentCommunityPageBinding.inflate(inflater, container, false)

        binding.communityPageFab.setOnClickListener {
            val intent = Intent(requireContext(), AddPageActivity::class.java)
            intent.putExtra("switch", "add")
            startActivity(intent)
        }

        // 스위치 옵션을 눌렀을 경우
        binding.communitySwitch.setOnCheckedChangeListener { _, isChecked ->
            isDeadlineOption = isChecked
            isOverDeadline()
        }

        binding.communityImageOption.setOnClickListener {
            Log.d("MENU", "버튼 눌렀냐?")
            val menu = PopupMenu(context, it)
            menu.menuInflater.inflate(R.menu.sort_option, menu.menu)
            menu.setOnMenuItemClickListener {
                when(it.itemId) {
                    R.id.menu_option_latest -> {
                        sortPostLatest()
                        true
                    }
                    R.id.menu_option_like -> {
                        sortPostLike()
                        true
                    }
                    R.id.menu_option_deadline -> {
                        sortPostDeadline()
                        true
                    }
                    R.id.menu_option_mine -> {
                        sortPostMine()
                        true
                    }
                    else -> {
                        false
                    }
                }
            }
            menu.show()
        }

        return binding.root
    }
    // 최신순으로 정렬 -> 날짜 최신순대로 내림차순
    @SuppressLint("NotifyDataSetChanged")
    private fun sortPostLatest() {
        Log.d("menu", "최신순으로 정렬")
        communityList.sortBy { it.date }
        communityPageAdapter.addItem(communityList)
        communityPageAdapter.notifyDataSetChanged()
        isOverDeadline()
    }

    // 좋아요가 많은 순대로 정렬
    @SuppressLint("NotifyDataSetChanged")
    private fun sortPostLike() {
        Log.d("menu", "좋아요순으로 정렬")
        /*communityPageAdapter.addItem(communityList)
        communityPageAdapter.notifyDataSetChanged()*/
    }

    // 마감일순으로 정렬
    @SuppressLint("NotifyDataSetChanged")
    private fun sortPostDeadline() {
        Log.d("menu", "마감일 순으로 정렬")

        val currentDate = System.currentTimeMillis()

        // 마감일에 따른 정렬
        communityList.sortWith(compareBy { post ->
            val deadlineDate = post.deadlinedate
            val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault())
            val date = dateFormat.parse(deadlineDate)
            val deadline = date?.time ?: 0
            (currentDate - deadline).toInt()
        })
        communityPageAdapter.addItem(communityList)
        communityPageAdapter.notifyDataSetChanged()
        isOverDeadline()
    }

    // 내 글로 정렬
    @SuppressLint("NotifyDataSetChanged")
    private fun sortPostMine() {
        Log.d("menu", "내 글로만 최신순으로 정렬")
        val currentUser = auth.currentUser
        if(currentUser != null) {
            val userId = currentUser.uid

            firebaseDatabase.child("users").child(userId).child("nickname")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot.exists()) {
                            val currentNickname = snapshot.getValue(String ::class.java)
                            val myPost = communityList.filter { it.nickname == currentNickname }
                            val sortedMyPost = myPost.sortedBy { it.date }
                            Log.d("MyPost", "$sortedMyPost")


                            if(isDeadlineOption) {
                                // 스위치가 켜져 있는 경우 -> 스위치 끄기
                                binding.communitySwitch.isChecked = false
                                isDeadlineOption = false
                                isOverDeadline()
                            }
                            communityPageAdapter.addItem(sortedMyPost.toMutableList())
                            communityPageAdapter.notifyDataSetChanged()
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                    }
                })
        }
        isOverDeadline()
    }

    override fun onResume() {
        super.onResume()
        //데이터베이스에서 데이터 읽어오기
        getItems()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 업로드한 게시물이 위로 가게 설정
        binding.communityPageRecyclerview.layoutManager = LinearLayoutManager(context).apply {
            reverseLayout = true
            stackFromEnd = true
        }
        binding.communityPageRecyclerview.adapter = communityPageAdapter

        communityPageAdapter.addItem(communityList)
        binding.communityPageRecyclerview.apply {
            adapter = communityPageAdapter
            clickItem()
        }
    }

    // 아이템 클릭 처리
    private fun clickItem() {
        communityPageAdapter.setOnClickListener(object : CommunityPageAdapter.ItemClick {
            override fun onClick(view: View, position: Int, model: PostModel) {
                val intent = Intent(activity, DetailPageActivity::class.java)

                with(model) {
                    intent.putExtra("dataFromAddPageimage", image)
                    intent.putExtra("dataFromAddPagedday", deadlinedate)
                    intent.putExtra("dataFromAddPageId", id)
                    intent.putExtra("dataFromAddPageWriter", writer)
                    intent.putExtra("dataFromAddPagedday", deadlinedate)
                }
                startActivity(intent)
            }
        })
    }

    // Realtime Database에서 POST 데이터 값 전부 가져오기
    fun getItems() {

        // 기존 데이터 초기화 하기
        communityList.clear()

        firebaseDatabase.child("POST")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (item in snapshot.children) {
                            Log.d("item", "${item.value}")
                            val userData =
                                item.getValue(PostModel::class.java)
                            if (userData != null) {
                                communityList.add(userData)
                                communityPageAdapter.addItem(communityList)
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun isOverDeadline() {
        Log.d("Press Switch", "스위치 눌렀냐?")
        val currentDate = System.currentTimeMillis()

        Log.d("check", "$isDeadlineOption")
        // 제외하는 옵션 눌렀을 경우
        if(isDeadlineOption) {
            val filterList = communityList.filter { post ->
                val deadlineDate = post.deadlinedate
                val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault())
                val date = dateFormat.parse(deadlineDate)
                val deadline = date?.time ?: 0
                currentDate < deadline
            }
            Log.d("if check", "$isDeadlineOption")
            communityPageAdapter.addItem(filterList.toMutableList())
        }
        else {
            Log.d("else check", "$isDeadlineOption")
            communityPageAdapter.addItem(communityList)
            // 상단으로 올라가기
            binding.communityPageRecyclerview.scrollToPosition(communityList.size - 1)
        }
        communityPageAdapter.notifyDataSetChanged()
    }
}
