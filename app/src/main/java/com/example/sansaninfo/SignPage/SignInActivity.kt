package com.example.sansaninfo.SignPage

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sansaninfo.Main.MainActivity
import com.example.sansaninfo.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.user.UserApiClient

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
                if (emailPattern.matches(binding.signinEtEmail.text))
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
                if (pwPattern.matches(binding.signinEtPw.text))
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
                            if (user != null && user.isEmailVerified) {
                                val signInIntent = Intent(this, MainActivity::class.java)
                                toastMessage("로그인 성공 !")
                                saveData()
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
            } else {
                toastMessage("로그인 정보를 확인해 주세요.")
            }

        }


        // 회원가입 버튼 누를 경우 회원가입 페이지로 이동
        binding.btnSignup.setOnClickListener {
            val signupIntent = Intent(this, SignUpActivity::class.java)
            startActivity(signupIntent)
        }

        /**
         *   출시 전에 릴리즈 해시키 값 가져와야 함
         */
        // 로그인 정보 확인 (테스트 용)
        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) {
//                Toast.makeText(this, "${error.message}", Toast.LENGTH_SHORT).show()
            } else if (tokenInfo != null) {
//                Toast.makeText(this, "토큰 정보 보기 성공", Toast.LENGTH_SHORT).show()
            }
        }
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                when {
                    error.toString() == AuthErrorCause.AccessDenied.toString() -> {
                        Toast.makeText(this, "접근이 거부 됨(동의 취소)", Toast.LENGTH_SHORT).show()
                    }

                    error.toString() == AuthErrorCause.InvalidClient.toString() -> {
                        Toast.makeText(this, "유효하지 않은 앱", Toast.LENGTH_SHORT).show()
                    }

                    error.toString() == AuthErrorCause.InvalidGrant.toString() -> {
                        Toast.makeText(this, "인증 수단이 유효하지 않아 인증할 수 없는 상태", Toast.LENGTH_SHORT)
                            .show()
                    }

                    error.toString() == AuthErrorCause.InvalidRequest.toString() -> {
                        Toast.makeText(this, "요청 파라미터 오류", Toast.LENGTH_SHORT).show()
                    }

                    error.toString() == AuthErrorCause.InvalidScope.toString() -> {
                        Toast.makeText(this, "유효하지 않은 scope ID", Toast.LENGTH_SHORT).show()
                    }
                }
            } else if (token != null) {
                UserApiClient.instance.me { user, error ->
                    Toast.makeText(
                        this,
                        "${user?.kakaoAccount?.profile?.nickname.toString()} 님 로그인 성공",
                        Toast.LENGTH_SHORT
                    ).show()
//                    user?.kakaoAccount?.email
                }
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                finish()
            }
        }

        // 소셜 로그인 테스트 (카카오 버튼)
        binding.signinBtnKakao.setOnClickListener {
            // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오 계정으로 로그인
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                UserApiClient.instance.loginWithKakaoTalk(this, callback = callback)
            } else {
                UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
            }
        }
        // 테스트 하고 지울 버튼 (카카오 로그인)
        binding.signinTitle.setOnClickListener {
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                UserApiClient.instance.loginWithKakaoTalk(this, callback = callback)
            } else {
                UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
            }
        }
        loadData()
    }

    private fun saveData() = with(binding) {
        val autoLogin = getSharedPreferences("prefLogin", 0)
        val saveEmail = getSharedPreferences("prefEmail", 0)
        val autoLoginEdit = autoLogin.edit()
        val saveEmailEdit = saveEmail.edit()
        // 데이터 저장
        if (signinSwitchAutoLogin.isChecked) {
            autoLoginEdit.putString("login", "1")
            autoLoginEdit.putString("pw", signinEtPw.text.toString())
            autoLoginEdit.apply()
        } else {
            saveEmailEdit.putString("login", "0")
            autoLoginEdit.putString("pw", "0")
            saveEmailEdit.apply()
            autoLoginEdit.apply()
        }
        if (signinSwitchSaveMail.isChecked) {
            saveEmailEdit.putString("check", "1")
            saveEmailEdit.putString("email", signinEtEmail.text.toString())
            saveEmailEdit.apply()
        } else {
            saveEmailEdit.putString("check", "0")
            saveEmailEdit.apply()
        }
    }

    private fun loadData() = with(binding) {
        val autoLogin = getSharedPreferences("prefLogin", 0)
        val saveEmail = getSharedPreferences("prefEmail", 0)
        val email = saveEmail.getString("email", "")

        if (saveEmail.getString("check", "") == "1") {
            // 이메일 불러오기
            signinSwitchSaveMail.isChecked = true
            signinEtEmail.setText(saveEmail.getString("email", ""))
        }
    }

    private fun toastMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}