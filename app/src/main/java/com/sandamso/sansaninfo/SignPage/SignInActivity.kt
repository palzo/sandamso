package com.sandamso.sansaninfo.SignPage

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.sandamso.sansaninfo.main.MainActivity
import com.sandamso.sansaninfo.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth
import com.sandamso.sansaninfo.BaseActivity

class SignInActivity : BaseActivity() {
    private lateinit var auth: FirebaseAuth
    private val binding by lazy { ActivitySignInBinding.inflate(layoutInflater) }
    private var emailCheck = false
    private var pwCheck = false

    private lateinit var  oneTapClient: SignInClient
    private lateinit var  signInRequest: BeginSignInRequest
    private val REQ_ONE_TAP = 2  // Can be any integer unique to the Activity
    private var showOneTapUI = true

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
                                showtoast("로그인 성공 !")
                                saveData()
                                startActivity(signInIntent)
                            }
                            // 이메일 인증 안했을 경우
                            else {
                                showtoast("이메일 인증 후 로그인이 가능합니다.")
                            }
                        } else {
                            // 로그인 실패 시
                            showtoast("아이디 또는 비밀번호가 일치하지 않습니다.")
                        }
                    }
            } else {
                showtoast("로그인 정보를 확인해 주세요.")
            }
        }

        // 회원가입 버튼 누를 경우 회원가입 페이지로 이동
        binding.btnSignup.setOnClickListener {
            val signupIntent = Intent(this, SignUpActivity::class.java)
            startActivity(signupIntent)
        }
        loadData()


//        binding.signinTitle.setOnClickListener {
//            oneTapClient = Identity.getSignInClient(this)
//            signInRequest = BeginSignInRequest.builder()
//                .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
//                    .setSupported(true)
//                    .build())
//                .setGoogleIdTokenRequestOptions(
//                    BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
//                        .setSupported(true)
//                        // Your server's client ID, not your Android client ID.
////                        .setServerClientId("797720929790-2og2l3aaka8efbchrsikabn4hlsja82c.apps.googleusercontent.com")
//                        .setServerClientId(getString(R.string.firebase_web_client_id))
//                        // Only show accounts previously used to sign in.
//                        .setFilterByAuthorizedAccounts(true)
//                        .build())
//                // Automatically sign in when exactly one credential is retrieved.
//                .setAutoSelectEnabled(true)
//                .build()
//
//            oneTapClient.beginSignIn(signInRequest)
//                .addOnSuccessListener(this) { result ->
//                    try {
//                        startIntentSenderForResult(
//                            result.pendingIntent.intentSender, REQ_ONE_TAP,
//                            null, 0, 0, 0, null)
//                    } catch (e: IntentSender.SendIntentException) {
//                        Log.d("google", "Couldn't start One Tap UI: ${e.localizedMessage}")
//                    }
//                }
//                .addOnFailureListener(this) { e ->
//                    // No saved credentials found. Launch the One Tap sign-up flow, or
//                    // do nothing and continue presenting the signed-out UI.
//                    Log.d("google", e.localizedMessage)
//                }
//        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        when (requestCode) {
//            REQ_ONE_TAP -> {
//                try {
//                    val credential = oneTapClient.getSignInCredentialFromIntent(data)
//                    val idToken = credential.googleIdToken
//                    val username = credential.id
//                    val password = credential.password
//                    when {
//                        idToken != null -> {
//                            // Got an ID token from Google. Use it to authenticate
//                            // with your backend.
//                            Log.d("google", "Got ID token.")
//                        }
//                        password != null -> {
//                            // Got a saved username and password. Use them to authenticate
//                            // with your backend.
//                            Log.d("google", "Got password.")
//                        }
//                        else -> {
//                            // Shouldn't happen.
//                            Log.d("google", "No ID token or password!")
//                        }
//                    }
//                } catch (e: ApiException) {
//                    // ...
//                }
//            }
//        }
//    }

    private fun saveData() = with(binding) {
        val loginInfo = getSharedPreferences("prefLogin", 0)
        val loginInfoEdit = loginInfo.edit()

        // 데이터 저장
        if (signinSwitchAutoLogin.isChecked) {
            // 자동 로그인이 체크되어있는 경우
            loginInfoEdit.putString("loginType", "2")
            loginInfoEdit.putString("email", signinEtEmail.text.toString())
            loginInfoEdit.putString("pw", signinEtPw.text.toString())
        } else if (signinSwitchSaveMail.isChecked) {
            // 이메일 저장만 체크되어있는 경우
            loginInfoEdit.putString("loginType", "1")
            loginInfoEdit.putString("email", signinEtEmail.text.toString())
            loginInfoEdit.putString("pw", "0")
        } else {
            // 아무것도 체크되어있지 않은 경우
            loginInfoEdit.putString("loginType", "0")
            loginInfoEdit.putString("email", "0")
            loginInfoEdit.putString("pw", "0")
        }
        loginInfoEdit.apply()
    }
    private fun loadData() = with(binding) {
        val loginInfo = getSharedPreferences("prefLogin", 0)

        if (loginInfo.getString("loginType", "") == "1") {
            // 이메일 불러오기
            signinSwitchSaveMail.isChecked = true
            signinEtEmail.setText(loginInfo.getString("email", ""))
        }
    }

    private fun toastMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}