package com.example.sansaninfo.MyPage

import android.content.Intent
import android.os.Bundle
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

        binding.nicknameBtnCancel.setOnClickListener {
            finish()



        }

    }
}