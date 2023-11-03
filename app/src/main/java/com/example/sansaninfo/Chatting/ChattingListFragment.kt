package com.example.sansaninfo.Chatting

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sansaninfo.Data.FBRoom
import com.example.sansaninfo.Data.RoomData
import com.example.sansaninfo.databinding.FragmentChattingListBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.util.zip.Inflater

class ChattingListFragment : Fragment() {

    val roomList = mutableListOf<RoomData>()

    private val chattingListAdapter by lazy {
        ChattingListAdapter()
    }

    private var _binding: FragmentChattingListBinding? = null
    private val binding get() = _binding!!

    companion object {

        var userId: String = ""
        var userName: String = ""
        fun newInstance() = ChattingListFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentChattingListBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.recyclerViewChattingRoom.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewChattingRoom.adapter = chattingListAdapter

        return view
    }

    override fun onResume() {
        super.onResume()
        //데이터베이스에서 데이터 읽어오기
        loadRooms()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    // 채팅방 목록 만들기
    private fun loadRooms() {
        FBRoom.roomRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("load Rooms", "Data received")
                roomList.clear()
                for (item in snapshot.children) {
                    item.getValue(RoomData::class.java)?.let { room -> roomList.add(room) }
                }
                chattingListAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("load Rooms", "error = $error")
            }
        })
    }
}