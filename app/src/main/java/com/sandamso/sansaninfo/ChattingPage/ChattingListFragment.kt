package com.sandamso.sansaninfo.ChattingPage

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.sandamso.sansaninfo.Data.FBRoom
import com.sandamso.sansaninfo.Data.RoomData
import com.sandamso.sansaninfo.databinding.FragmentChattingListBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.sandamso.sansaninfo.R

class ChattingListFragment : Fragment() {

    val roomList = mutableListOf<RoomData>()

    private val chattingListAdapter by lazy {
        ChattingListAdapter(roomList)
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
        binding.recyclerViewChattingRoom.adapter = chattingListAdapter

        loadRooms()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chattingListAdapter.setOnItemLongClickListener(object :
            ChattingListAdapter.OnItemLongClickListener {

            // 채팅방 나가기 기능 구현
            override fun onItemLongClick(position: Int) {

                val roomIdToDelete = roomList[position].id
                val usersInRoom = roomList[position].users

                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("채팅방 나가기")
                builder.setMessage("채팅방에서 나가시겠습니까?")
                builder.setIcon(R.drawable.alert_triangle)

                val listener = DialogInterface.OnClickListener { dialog, p1 ->
                    when (p1) {
                        DialogInterface.BUTTON_POSITIVE -> {

                            // 해당 위치가 리스트 범위 내에 있는지 확인하기
                            if (position >= 0 && position < roomList.size) {
                                FBRoom.roomRef.child(roomIdToDelete).child("users").child(userId)
                                    .setValue(null)
                                    .addOnSuccessListener {

                                        // 채팅방에 다른 유저가 없으면 채팅방 정보 삭제하기
                                        if (usersInRoom.size <= 1) {
                                            FBRoom.roomRef.child(roomIdToDelete).removeValue()
                                                .addOnSuccessListener {

                                                    // 리스트에서 아이템 삭제 전에 해당 위치가 여전히 유효한지 확인하기
                                                    if (position < roomList.size) {
                                                        roomList.removeAt(position)
                                                        chattingListAdapter.notifyItemRemoved(
                                                            position
                                                        )
                                                    }
                                                }
                                        } else {

                                            // 채팅방에 다른 유저가 있다면 DB에서 현재 사용자만 삭제하기
                                            // 리스트에서 아이템 삭제 전에 해당 위치가 여전히 유효한지 확인
                                            if (position < roomList.size) {
                                                roomList.removeAt(position)
                                                chattingListAdapter.notifyItemRemoved(position)
                                            }
                                        }
                                    }
                            }
                        }

                        DialogInterface.BUTTON_NEGATIVE -> {
                        }
                    }
                }

                builder.setPositiveButton("확인", listener)
                builder.setNegativeButton("취소", listener)
                builder.show()
            }
        })
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
                Log.d("RoomData", "들어옴")
                for (item in snapshot.children) {
                    val room = item.getValue(RoomData::class.java)
                    Log.d("RoomData", "RoomData = $room")
                    if (room != null && userId in room.users.values) {
                        Log.d("joinUser", "joinUser = $userId")
                        totalUser(FBRoom.roomRef.child(room.id).child("users"), room)
                        Log.d("RoomData", "room = $room")
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("load Rooms", "Error loading rooms")
            }
        })
    }
    private fun totalUser(usersRef: DatabaseReference, room: RoomData){
        usersRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("RoomData", "snapshot.childrenCount = ${snapshot.childrenCount}")
                room.userCount = snapshot.childrenCount
                Log.d("RoomData", "room.userCount = ${room.userCount}")
                roomList.add(room)
                chattingListAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    // 닉네임 비교해서 들어온 본인이 아닌 경우
    fun alarm(roomId: String) {
        loadRooms()

    }
}
