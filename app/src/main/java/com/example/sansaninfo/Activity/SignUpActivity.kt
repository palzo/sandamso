package com.example.sansaninfo.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.sansaninfo.Data.UserData
import com.example.sansaninfo.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val binding by lazy { ActivitySignUpBinding.inflate(layoutInflater) }
    private lateinit var resultpw: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Firebase Authentication과 연결
        auth = FirebaseAuth.getInstance()

        /*수정 필요 부분
        =======================================
        회원가입 정보 입력칸의 형식 조건 추가 필요
        =======================================*/
        // 이메일 형식대로 입력하고 회원가입 정보를 제대로 입력한 경우
        binding.btnSignupaccept.setOnClickListener {
            val email = binding.signupEtEmail.text.toString()
            val nick = binding.signupEtNickname.text.toString()
            val name = binding.signupEtName.text.toString()
            val pw = binding.signupEtPw.text.toString()
            val checkpw = binding.signupEtCheckpw.text.toString()

            val emailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

            if (email.isNotEmpty() && nick.isNotEmpty() && name.isNotEmpty() && pw.isNotEmpty() && checkpw.isNotEmpty()) {
                // 이메일 형식이 아닌 경우
                if (emailValid) {
                    if (pw == checkpw) {
                        resultpw = pw

                        // 올바른 정보 입력 후, 확인 버튼 클릭 시 Firebase의 Auth에 데이터 저장
                        auth.createUserWithEmailAndPassword(email, resultpw)
                            .addOnCompleteListener { task ->
                                // 회원가입 성공 시
                                if (task.isSuccessful) {
                                    Toast.makeText(this, "회원가입 성공!", Toast.LENGTH_SHORT).show()
                                    val user = auth.currentUser
                                    //  RealTimeDB 사용자 정보에 간단하게 이름, 아이디, 성별, 닉네임 DB 생성
                                    val userData = UserData(name, email, nick)
                                    saveUserData(userData)
                                    // 회원가입 성공 후 로그인 화면으로 돌아가기
                                    val intent = Intent(this, SignInActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                                // 회원가입 실패 시
                                else {
                                    val error = task.exception?.message ?: "알 수 없는 오류"
                                    Toast.makeText(
                                        this,
                                        "회원가입에 실패했습니다. 오류: $error",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    } else {
                        Toast.makeText(this, "비밀번호가 일치하지 않습니다. (비밀번호는 최소 6자리)", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(this, "올바른 이메일 주소를 입력해주세요.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "올바른 정보를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
        // 회원가입 취소 버튼 클릭 시 로그인 페이지로 돌아가기
        binding.btnSignupcancel.setOnClickListener {
            finish()
        }
    }

    private fun saveUserData(user: UserData) {
        val dataBase = FirebaseDatabase.getInstance()
        val userReference = dataBase.getReference("users")
        val userId = userReference.push().key

        userId?.let {
            userReference.child(it).setValue(user)
                .addOnCompleteListener { task ->
                    // 회원가입 성공 시
                    if (task.isSuccessful) {
                        Toast.makeText(this, "RealTime userData 생성 성공!", Toast.LENGTH_SHORT).show()
                    }
                    // 회원가입 실패 시
                    else {
                        val error = task.exception?.message ?: "알 수 없는 오류"
                        Toast.makeText(
                            this,
                            "RealTime userData 생성 실패! 오류: $error",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }
}