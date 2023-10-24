package com.example.sansaninfo.CommunityPage

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sansaninfo.AddPage.AddPage
import com.example.sansaninfo.Data.UserModel
import com.example.sansaninfo.databinding.FragmentCommunityPageBinding
import com.google.firebase.auth.FirebaseAuth

class CommunityPageFragment : Fragment() {

    private lateinit var binding: FragmentCommunityPageBinding
    private lateinit var auth: FirebaseAuth
    private val communityList = mutableListOf<UserModel>()
    private val communityPageAdapter by lazy { CommunityPageAdapter() }

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
            val intent = Intent(requireContext(), AddPage::class.java)
            startActivity(intent)
        }

        return binding.root
    }
    override fun onResume() {
        super.onResume()
        //데이터베이스에서 데이터 읽어오기
//        getCommunityData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.communityPageRecyclerview.layoutManager = LinearLayoutManager(context)
        binding.communityPageRecyclerview.adapter = communityPageAdapter

        for (i in 0..5) {
            communityList.add(UserModel(title = "용석님", time = "2023.10.20"))
        }

        communityPageAdapter.addItem(communityList)
        binding.communityPageRecyclerview.apply {
            adapter = communityPageAdapter
            clickItem()
        }
    }

//    private fun getCommunityData() {
//        val postListener = object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                communityList.clear()
//
//                for (data in snapshot.children) {
//                    val item = data.getValue(UserModel::class.java)
//                    Log.d("CommunityList", "item: ${item}")
//                    if (item != null) {
//                        communityList.add(item)
//                    }
//                }
//
//                communityList.reverse()
//              // notifyDataSetChanged()를 호출하여 adapter에게 값이 변경 되었음을 알려준다.
//                communityPageAdapter.notifyDataSetChanged()
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Log.e("Firebase Error", error.message)
//            }
//        }
//
//        // addValueEventListener() 메서드로 DatabaseReference에 ValueEventListener를 추가한다.
//        FBRef.myRef.addValueEventListener(postListener)
//    }

    private fun clickItem() {
        communityPageAdapter.setOnClickListener(object : CommunityPageAdapter.ItemClick{
            override fun onClick(view: View, position: Int, model: UserModel) {
                Toast.makeText(context,"$position", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
