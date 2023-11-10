package com.sandamso.sansaninfo.ChattingPage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.sandamso.sansaninfo.Data.FBRoom
import com.sandamso.sansaninfo.Data.RoomData
import com.sandamso.sansaninfo.databinding.FragmentChattingListBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class ChattingListFragment : Fragment() {

    val roomList = mutableListOf<RoomData>()

    private val chatRoomListAdapter by lazy {
        ChatRoomListAdapter(roomList)
    }

    private var _binding: FragmentChattingListBinding? = null
    private val binding get() = _binding!!

    private val userId: String by lazy {
        Firebase.auth.currentUser?.uid ?: ""
    }

    companion object {
        fun newInstance() = ChattingListFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentChattingListBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.recyclerViewChattingRoom.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewChattingRoom.adapter = chatRoomListAdapter

        loadRooms()

        return view
    }

//    override fun onResume() {
//        super.onResume()
//        loadRooms()
//    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    // 채팅방 목록 만들기
    private fun loadRooms() {
        FBRoom.roomRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                roomList.clear()

                for (item in snapshot.children) {
                    val room = item.getValue(RoomData::class.java)
                    Log.d("RoomData", "RoomData = $room")

                    if (room != null && userId in room.users.values) {
                        Log.d("joinUser", "joinUser = $userId")
                        roomList.add(room)
                    }
                }
                chatRoomListAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("load Rooms", "Error loading rooms")
            }
        })
    }
}
