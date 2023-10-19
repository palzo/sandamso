package com.example.sansaninfo.MyPage

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sansaninfo.databinding.ActivityChangeNicknameBinding
import com.google.firebase.auth.FirebaseAuth

class ChangeNicknameActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val binding by lazy { ActivityChangeNicknameBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        // 취소버튼 눌러서 마이페이지로 돌아가기
        binding.nicknameBtnCancel.setOnClickListener {
            finish()
        }
        binding.nicknameBtnOkay.setOnClickListener {
            val email = binding.nicknameEtNickname.text.toString()
            // 수정할 닉네임을 입력했는지 토스트 띄워서 알려주기.
            if (email.isNotEmpty()) {
                Toast.makeText(this, "닉네임 수정이 완료되었습니다.\n잠시 기다려주세요.", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "수정할 닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

    }
}