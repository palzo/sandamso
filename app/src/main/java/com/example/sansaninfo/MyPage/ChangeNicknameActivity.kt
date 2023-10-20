package com.example.sansaninfo.MyPage

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sansaninfo.databinding.ActivityChangeNicknameBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ChangeNicknameActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val binding by lazy { ActivityChangeNicknameBinding.inflate(layoutInflater) }
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //FirebaseAuth 연결
        auth = FirebaseAuth.getInstance()

        // 파이어베이스 데이터베이스 초기화
        database = FirebaseDatabase.getInstance().reference

        // 취소버튼 눌러서 마이페이지로 돌아가기
        binding.nicknameBtnCancel.setOnClickListener {
            finish()
        }
        binding.nicknameBtnOkay.setOnClickListener {
            val newNickname = binding.nicknameEtNickname.text.toString()
            if (newNickname.isNotEmpty()) {
                val userId = auth.currentUser?.uid
                if (userId != null) {
                    writeNewUser(userId, newNickname)
                    // 수정할 닉네임을 입력했는지 토스트 띄워서 알려주기.
                    Toast.makeText(this, "닉네임 수정이 완료되었습니다.\n잠시 기다려주세요.", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "사용자 인증을 할 수 없습니다.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "수정할 닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun writeNewUser(userId: String, newNickname: String) {
        database.child("users").child(userId).child("nickname").setValue(newNickname)
    }
    // [START rtdb_write_new_user]
//        fun writeNewUser(userId: String, name: String, email: String, nickname: String) {
//            val nickname = UserData(name, email, nickname)
//            database.child("users").child(userId).child("nickname").setValue(nickname)
//        }
    // [END rtdb_write_new_user]
//        val editText = binding.nicknameEtNickname
//        binding.nicknameBtnOkay.setOnClickListener {
//            val updateNickname = editText.text.toString()
//            writeNewUser("nickname",)
//            finish()
//        }


//    private fun writeNewNickname(uid: String, nickname: String) {
//        val user = UserData(nickname)
//        val uid = auth.currentUser?.uid ?: ""
//        database.child("users").child(uid).child("nickname").setValue(nickname)
//    }


//    private fun updateNickname(userId: String, username: String, title: String, body: String) {
//        val key = database.child("posts").push().key
//        if (key == null) {
//            return
//        }
//
//        val post = Post(userId, username, title, body)
//        val postValues = post.toMap()
//
//        val childUpdates = hashMapOf<String, Any>(
//            "/posts/$key" to postValues,
//            "/user-posts/$userId/$key" to postValues,
//        )
//
//        database.updateChildren(childUpdates)
//    }
//        private fun updateNickname(nickname : String) {
//        userReference.child("nickname").setValue(user)
//            .addOnCompleteListener { task -> //닉네임 업데이트 성공 시
//                if(task.isSuccessful) {
//                    Toast.makeText(this, "업데이트 성공", Toast.LENGTH_SHORT).show()
//                    binding.myPageEtNickname.setText(user)
//                }
//                else {
//                    Toast.makeText(activity, "업데이트 실패", Toast.LENGTH_SHORT).show()
//                }
//            }
//    }
}