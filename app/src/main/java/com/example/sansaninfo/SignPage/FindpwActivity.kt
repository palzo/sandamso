package com.example.sansaninfo.SignPage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.sansaninfo.databinding.ActivityFindpwBinding
import com.google.firebase.auth.FirebaseAuth

class FindpwActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private val binding by lazy { ActivityFindpwBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 뒤로가기 버튼 클릭 시
        binding.findpwBtnBack.setOnClickListener {
            finish()
        }

        // 비밀번호 찾기 버튼 클릭 시
        binding.findpwBtnFind.setOnClickListener {
            val email = binding.findpwEtEmail.text.toString()
            // 이메일이 비어있지 않은 경우, 비밀번호 재설정 이메일 전송
            if (email.isNotEmpty()) {
                val user = auth.currentUser
                resetPW(email)
            } else {
                toastMessage("이메일 주소를 입력해주세요.")
            }
        }
    }

    // 비밀번호 재설정 기능
    private fun resetPW(email: String) {
        val auth = FirebaseAuth.getInstance()

        // Firebase에 등록된 이메일 주소 확인
        auth.fetchSignInMethodsForEmail(email)
            .addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    val signInMethod = task.result?.signInMethods
                    // 이메일 주소가 Firebase에 등록되어 있지 않은 경우
                    if(signInMethod.isNullOrEmpty()) {
                        toastMessage("해당 이메일 주소로 가입된 정보가 존재하지 않습니다.")
                    }
                    // 이메일 주소가 Firebase에 등록되어 있는 경우
                    else {
                        // 해당 이메일로 재설정 이메일 전송
                        auth.sendPasswordResetEmail(email)
                            .addOnCompleteListener { resetTask ->
                                if (resetTask.isSuccessful) {
                                    toastMessage("비밀번호 재설정 이메일 발송 완료 !")
                                    val resultIntent = Intent(this, FindpwResultActivity::class.java)
                                    startActivity(resultIntent)
                                } else {
                                    toastMessage("이메일 전송에 실패했습니다. 가입하신 이메일 주소를 확인해주세요.")
                                }
                            }
                    }
                }
                else {
                    toastMessage("이메일 주소 확인 중 에러가 발생했습니다!\n다시 시도하세요.")
                }
            }
    }

    private fun toastMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}