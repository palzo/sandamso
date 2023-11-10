package com.sandamso.sansaninfo.ChattingPage

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.sandamso.sansaninfo.Data.FBRoom
import com.sandamso.sansaninfo.Data.MessageData
import com.sandamso.sansaninfo.Data.RoomData
import com.sandamso.sansaninfo.databinding.ActivityChattingPageBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class ChatRoomActivity:AppCompatActivity() {

    private val binding by lazy { ActivityChattingPageBinding.inflate(layoutInflater) }
    private lateinit var auth: FirebaseAuth
    var currentUser = ""
    val roomList = mutableListOf<RoomData>()
    lateinit var msgRef: DatabaseReference

    var roomId: String = ""
    var roomTitle: String = ""

    val msgList = mutableListOf<MessageData>()
    lateinit var adapter: MsgListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        if(auth.currentUser != null) {
            currentUser = auth.currentUser?.uid!!
        }

        roomId = intent.getStringExtra("roomId") ?: "none"
        roomTitle = intent.getStringExtra("dataFromdetailPageTitle") ?: ""

        msgRef = FBRoom.roomRef.child(roomId).child("messages")
        adapter = MsgListAdapter(msgList, currentUser)
        Log.d("currentUser", "$currentUser")

        with(binding){
            recyclerMessages.adapter = adapter
            recyclerMessages.layoutManager = LinearLayoutManager(baseContext)

            txtTItle.text = roomTitle
            imgbtnQuit.setOnClickListener { finish() }
            btnSubmit.setOnClickListener { sendMsg() }
        }

        loadMsgs()
    }

    private fun loadMsgs() {
        msgRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                msgList.clear()
                for (item in snapshot.children) {
                    item.getValue(MessageData::class.java)?.let { msg ->
                        msgList.add(msg)
                    }
                }
                adapter.notifyDataSetChanged()
                binding.recyclerMessages.scrollToPosition(adapter.itemCount -1)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("load Message","error")
            }
        })
    }

    private fun sendMsg() {
        with(binding){
            if (edtMessage.text.isNotEmpty()) {
                setNickname { nickname ->
                    val message = MessageData(edtMessage.text.toString(), nickname)
                    val msgId = msgRef.push().key!!
                    message.id = msgId
                    msgRef.child(msgId).setValue(message)
                    edtMessage.setText("")

                    adapter.notifyDataSetChanged()
                    recyclerMessages.scrollToPosition(adapter.itemCount -1)
                }
            }
        }
    }

    //     Firebase에서 닉네임 가져오기
    private fun setNickname(onNickNameFetched: (String) -> Unit) {
        val uid = Firebase.auth.currentUser?.uid ?: ""
        FirebaseDatabase.getInstance().reference.child("users").child(uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val userData =
                            snapshot.getValue(com.sandamso.sansaninfo.Data.UserData::class.java)
                        if (userData != null) {
                            val nickname = userData.nickname
                            onNickNameFetched(nickname)
                            Log.d("Nickname", "작성자: ${nickname}")
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("Nickname", "error = $error")
                }
            })
    }
}