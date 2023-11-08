package com.sandamso.sansaninfo.Chatting

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.sandamso.sansaninfo.Data.FBRoom
import com.sandamso.sansaninfo.Data.MessageData
import com.sandamso.sansaninfo.Data.RoomData
import com.sandamso.sansaninfo.databinding.ActivityChattingPageBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener


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
        roomTitle = intent.getStringExtra("roomTitle") ?: "없음"

        msgRef = FBRoom.roomRef.child(roomId).child("messages")
        adapter = MsgListAdapter(msgList, currentUser)

        with(binding){
            recyclerMessages.adapter = adapter
            recyclerMessages.layoutManager = LinearLayoutManager(baseContext)

            txtTItle.setText(roomTitle)
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
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("load Message","error")
            }
        })
    }

    fun sendMsg() {
        with(binding){
            if (edtMessage.text.isNotEmpty()) {
                val message = MessageData(edtMessage.text.toString(), ChattingListFragment.userName)
                val msgId = msgRef.push().key!!
                message.id = msgId
                msgRef.child(msgId).setValue(message)
                edtMessage.setText("")
            }
        }
    }
}