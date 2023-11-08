package com.example.sansaninfo.Chatting

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sansaninfo.Data.FBRoom
import com.example.sansaninfo.Data.RoomData
import com.example.sansaninfo.databinding.FragmentChattingListBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class ChattingListFragment : Fragment() {

    val roomList = mutableListOf<RoomData>()

    private val chatRoomListAdapter by lazy {
        ChatRoomListAdapter(roomList)
    }

    private var _binding: FragmentChattingListBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = ChattingListFragment()
        var userId: String = ""
        var userName: String = ""
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentChattingListBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.recyclerViewChattingRoom.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewChattingRoom.adapter = chatRoomListAdapter

        binding.chattingButton.setOnClickListener { openCreateRoom() }

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
                roomList.clear()
                for (item in snapshot.children) {
                    item.getValue(RoomData::class.java)?.let { room ->
                        roomList.add(room)
                    }
                }
                chatRoomListAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("load Rooms", "error")
            }
        })
    }

    private fun openCreateRoom() {
        val editTitle = EditText(requireContext())
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("방 이름을 적어주세요.")
            .setView(editTitle)
            .setPositiveButton("만들기") { dlg, id ->
                createRoom(editTitle.text.toString())
            }
        dialog.show()
    }

    private fun createRoom(title: String) {
        val room = RoomData(title, userName)
        val roomId = FBRoom.roomRef.push().key!!
        room.id = roomId
        FBRoom.roomRef.child(roomId).setValue(room)
    }
}
