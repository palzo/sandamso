package com.example.sansaninfo.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.sansaninfo.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val binding by lazy { ActivitySignUpBinding.inflate(layoutInflater) }
    private lateinit var resultpw: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Firebase Authentication과 연결
        auth = FirebaseAuth.getInstance()
        val email = binding.editSignupemail.text.toString()
        val nick = binding.editSignupnick.text.toString()
        val name = binding.editSignupname.text.toString()
        val pw = binding.editSignuppw.text.toString()
        val checkpw = binding.editSignupemail.text.toString()

        /*수정 필요 부분
        =======================================
        회원가입 정보 입력칸의 형식 조건 추가 필요
        =======================================*/

        val emailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

        // 이메일 형식대로 입력하고 회원가입 정보를 제대로 입력한 경우
        binding.btnSignupaccept.setOnClickListener {
            val email = binding.editSignupemail.text.toString()
            val nick = binding.editSignupnick.text.toString()
            val name = binding.editSignupname.text.toString()
            val pw = binding.editSignuppw.text.toString()
            val checkpw = binding.editSignupcheck.text.toString()

            val emailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

            if (email.isNotEmpty() && nick.isNotEmpty() && name.isNotEmpty() && pw.isNotEmpty() && checkpw.isNotEmpty()) {
                // 이메일 형식이 아닌 경우
                if (emailValid) {
                    if (pw == checkpw) {
                        resultpw = pw
                        auth.createUserWithEmailAndPassword(email, resultpw)
                            .addOnCompleteListener { task ->
                                // 회원가입 성공 시
                                if (task.isSuccessful) {
                                    Toast.makeText(this, "회원가입 성공!", Toast.LENGTH_SHORT).show()
                                    val user = auth.currentUser
                                    val intent = Intent(this, SignInActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                                // 회원가입 실패 시
                                else {
                                    val error = task.exception?.message ?: "알 수 없는 오류"
                                    Toast.makeText(this, "회원가입에 실패했습니다. 오류: $error", Toast.LENGTH_SHORT).show()
                                }
                            }
                    } else {
                        Toast.makeText(this, "비밀번호가 일치하지 않습니다. (비밀번호는 최소 6자리)", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "올바른 이메일 주소를 입력해주세요.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "올바른 정보를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}