package com.example.sansaninfo.CommunityPage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sansaninfo.AddPage.AddPage
import com.example.sansaninfo.Data.FBRef
import com.example.sansaninfo.Data.UserModel
import com.example.sansaninfo.databinding.FragmentCommunityPageBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class CommunityPageFragment : Fragment() {

    private lateinit var binding: FragmentCommunityPageBinding
    lateinit var communityPageAdapter: CommunityPageAdapter
    private val communityList = mutableListOf<UserModel>()

    companion object {
        fun newInstance() = CommunityPageFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCommunityPageBinding.inflate(inflater, container, false)

        binding.communityPageFab.setOnClickListener {
            val intent = Intent(requireContext(), AddPage::class.java)
            startActivity(intent)
        }

        communityPageAdapter = CommunityPageAdapter(communityList)
        binding.communityPageRecyclerview.adapter = communityPageAdapter

//        //데이터베이스에서 데이터 읽어오기
//        getCommunityData()

        return binding.root
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
////                // notifyDataSetChanged()를 호출하여 adapter에게 값이 변경 되었음을 알려준다.
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
}