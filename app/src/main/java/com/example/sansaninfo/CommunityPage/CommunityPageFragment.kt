package com.example.sansaninfo.CommunityPage

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
import com.example.sansaninfo.AddPage.AddPageActivity
import com.example.sansaninfo.Data.PostModel
import com.example.sansaninfo.DetailPage.DetailPageActivity
import com.example.sansaninfo.R
import com.example.sansaninfo.databinding.FragmentCommunityPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CommunityPageFragment : Fragment() {

    private lateinit var binding: FragmentCommunityPageBinding
    private lateinit var auth: FirebaseAuth
    private val communityList = mutableListOf<PostModel>()
    private val communityPageAdapter by lazy { CommunityPageAdapter() }
    private var firebaseDatabase = FirebaseDatabase.getInstance().reference

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

        binding.communityImageOption.setOnClickListener {
            Log.d("Options", "버튼 눌렀냐?")
            val menu = PopupMenu(context, it)
            menu.menuInflater.inflate(R.menu.sort_option, menu.menu)
            menu.setOnMenuItemClickListener {
                when(it.itemId) {
                    R.id.menu_option_latest -> {
                        true
                    }
                    R.id.menu_option_deadline -> {
                        true
                    }
                    R.id.menu_option_mine -> {
                        true
                    }
                    else -> {
                        false
                    }
                }
            }
        }

        return binding.root
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
}
