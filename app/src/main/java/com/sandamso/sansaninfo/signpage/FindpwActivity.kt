package com.sandamso.sansaninfo.signpage

import android.content.Intent
import android.os.Bundle
import com.sandamso.sansaninfo.databinding.ActivityFindpwBinding
import com.google.firebase.auth.FirebaseAuth
import com.sandamso.sansaninfo.BaseActivity

class FindpwActivity : BaseActivity() {
    private lateinit var auth: FirebaseAuth
    private val binding by lazy { ActivityFindpwBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        // 뒤로가기 버튼 클릭 시
        binding.findpwBtnBack.setOnClickListener {
            finish()
        }

        // 비밀번호 찾기 버튼 클릭 시
        binding.findpwBtnFind.setOnClickListener {
            val email = binding.findpwEtEmail.text.toString()
            // 이메일이 비어있지 않은 경우, 비밀번호 재설정 이메일 전송
            if (email.isNotEmpty()) {
                resetPW(email)
            } else {
                showtoast("이메일 주소를 입력해주세요.")
            }
        }
    }

    // 비밀번호 재설정 기능
    private fun resetPW(email: String) {
        // 해당 이메일로 재설정 이메일 전송
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { resetTask ->
                if (resetTask.isSuccessful) {
                    val resultIntent = Intent(this, FindpwResultActivity::class.java)
                    startActivity(resultIntent)
                } else {
                    showtoast("이메일 전송에 실패했습니다. 가입하신 이메일 주소를 확인해주세요.")
                }
            }
    }
}