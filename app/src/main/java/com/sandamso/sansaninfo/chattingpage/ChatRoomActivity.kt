package com.sandamso.sansaninfo.chattingpage

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.sandamso.sansaninfo.data.FBRoom
import com.sandamso.sansaninfo.data.MessageData
import com.sandamso.sansaninfo.databinding.ActivityChattingPageBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.sandamso.sansaninfo.R
import com.sandamso.sansaninfo.UserPopUp
import com.sandamso.sansaninfo.data.LastMessageManager

class ChatRoomActivity:AppCompatActivity() {

    private val binding by lazy { ActivityChattingPageBinding.inflate(layoutInflater) }
    private val chattingListFragment by lazy{
        ChattingListFragment()
    }
    private lateinit var auth: FirebaseAuth
    var currentUser = ""
    lateinit var msgRef: DatabaseReference
    private lateinit var recyclerView : RecyclerView

    var roomId: String = ""
    var roomTitle: String = ""
    var UID: String = ""

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

        recyclerView = findViewById(R.id.recycler_messages)
        adapter = MsgListAdapter(msgList, currentUser, recyclerView)
        recyclerView.adapter = adapter

        with(binding){
            recyclerMessages.adapter = adapter
            recyclerMessages.layoutManager = LinearLayoutManager(baseContext)

            txtTItle.text = roomTitle
            imgbtnQuit.setOnClickListener { finish() }
            btnSubmit.setOnClickListener { sendMsg() }
        }

        loadMsgs()
        loadUser()
    }

    private fun loadUser() {
        with(binding){
            chattingPageIvConversationPartner.setOnClickListener{
                UserPopUp(this@ChatRoomActivity, FBRoom.roomRef.child(roomId)).show()

            }
        }
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

                    LastMessageManager.updateLastMessage(roomId, UID, edtMessage.text.toString())

                    // 보내는 사람이 현재 사용자가 아닌 경우에만 알림 보내기
                    if (nickname != message.userName) {
                        sendNotification("새로운 메시지가 도착했습니다.", message.msg)
                    }else{
                    }
                    edtMessage.setText("")
                    adapter.notifyDataSetChanged()
                    recyclerMessages.scrollToPosition(adapter.itemCount -1)
                }
            }
        }
    }

    // Firebase에서 닉네임 가져오기
    private fun setNickname(onNickNameFetched: (String) -> Unit) {
        UID = Firebase.auth.currentUser?.uid ?: ""
        FirebaseDatabase.getInstance().reference.child("users").child(UID)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val userData =
                            snapshot.getValue(com.sandamso.sansaninfo.data.UserData::class.java)
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

    // 메시지 알림 설정
    private fun sendNotification(title: String, message: String) {
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val builder: NotificationCompat.Builder
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 26 버전 이상
            val channelId = "one-channel"
            val channelName = "My Channel One"
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "My Channel One Description"
                setShowBadge(true)
            }
            manager.createNotificationChannel(channel)
            builder = NotificationCompat.Builder(this, channelId)
        } else {
            // 26 버전 이하
            builder = NotificationCompat.Builder(this)
        }

        val intent = Intent(this, ChatRoomActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        intent.putExtra("roomId",roomId)
        intent.putExtra("dataFromdetailPageTitle",roomTitle)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        builder.run {
            setSmallIcon(R.drawable.ic_logo_msg)
            setWhen(System.currentTimeMillis())
            setContentTitle(title)
            setContentText(message)
            setStyle(NotificationCompat.BigTextStyle()
                .bigText(message))
            setLargeIcon(null)
            setContentIntent(pendingIntent)
        }

        manager.notify(11, builder.build())
    }
}