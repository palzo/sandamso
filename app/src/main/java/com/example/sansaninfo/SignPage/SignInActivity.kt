package com.example.sansaninfo.SignPage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.sansaninfo.Main.MainActivity
import com.example.sansaninfo.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val binding by lazy { ActivitySignInBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 비밀번호 찾기 누를 경우
        binding.signinTvFindpw.setOnClickListener {
            val dialogIntent = Intent(this, FindpwActivity::class.java)
            startActivity(dialogIntent)
        }

        // Firebase Authentication과 연결
        auth = FirebaseAuth.getInstance()

        // 로그인 버튼을 누를 경우
        binding.btnSignin.setOnClickListener {
            val email = binding.signinEtEmail.text.toString()
            val pw = binding.signinEtPw.text.toString()

            // 이메일 및 패스워드가 비어있지 않을 경우
            if (email.isNotEmpty() && pw.isNotEmpty()) {
                // Firebase에서 로그인 정보 가져오기
                auth.signInWithEmailAndPassword(email, pw)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // 로그인 성공 시
                            val user = auth.currentUser
                            val signInIntent = Intent(this, MainActivity::class.java)
                            Toast.makeText(this, "로그인 성공 !", Toast.LENGTH_SHORT).show()
                            startActivity(signInIntent)
                        } else {
                            // 로그인 실패 시
                            Toast.makeText(this, "아이디 및 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
            }
            else {
                Toast.makeText(this, "로그인 정보를 입력해 주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        // 테스트로만 사용 후 삭제
        binding.textView5.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // 회원가입 버튼 누를 경우 회원가입 페이지로 이동
        binding.btnSignup.setOnClickListener {
            val signupIntent = Intent(this, SignUpActivity::class.java)
            startActivity(signupIntent)
        }
    }
}