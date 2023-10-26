package com.example.sansaninfo.SignPage

import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.example.sansaninfo.Main.MainActivity
import com.example.sansaninfo.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val binding by lazy { ActivitySignInBinding.inflate(layoutInflater) }
    private var emailCheck = false
    private var pwCheck = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        

        // EditText 유효성 검사 기능 추가
        binding.signinEtEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val emailPattern = Regex("\\w+@\\w+\\.\\w+(\\.\\w+)?")
                if(emailPattern.matches(binding.signinEtEmail.text))
                    emailCheck = true
                else {
                    binding.signinEtEmail.error = "이메일 형식으로 입력"
                    emailCheck = false
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.signinEtPw.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val pwPattern = Regex("^(?![가-힣]).{8,15}\$")
                if(pwPattern.matches(binding.signinEtPw.text))
                    pwCheck = true
                else {
                    binding.signinEtPw.error = "8 ~ 15자리 이내 입력(한글제외)"
                    pwCheck = false
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })


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
            if (email.isNotEmpty() && pw.isNotEmpty() && emailCheck && pwCheck) {
                // Firebase에서 로그인 정보 가져오기
                auth.signInWithEmailAndPassword(email, pw)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // 로그인 성공 시
                            val user = auth.currentUser
                            if(user != null && user.isEmailVerified) {
                                val signInIntent = Intent(this, MainActivity::class.java)
                                toastMessage("로그인 성공 !")
                                startActivity(signInIntent)
                            }
                            // 이메일 인증 안했을 경우
                            else {
                                toastMessage("이메일 확인 후 로그인이 가능합니다.")
                            }
                        } else {
                            // 로그인 실패 시
                            toastMessage("아이디 또는 비밀번호가 일치하지 않습니다.")
                        }
                    }
            }
            else {
                toastMessage("로그인 정보를 입력해 주세요.")
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
    private fun toastMessage(message : String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}