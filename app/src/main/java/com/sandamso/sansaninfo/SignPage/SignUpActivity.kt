/*Copyright (c) 2023 PersesTitan

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.*/
package com.sandamso.sansaninfo.SignPage

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.core.content.ContextCompat
import com.sandamso.sansaninfo.data.UserData
import com.sandamso.sansaninfo.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sandamso.sansaninfo.BaseActivity
import com.sandamso.sansaninfo.R
import com.vane.badwordfiltering.BadWordFiltering

class SignUpActivity : BaseActivity() {
    private lateinit var auth: FirebaseAuth
    private val binding by lazy { ActivitySignUpBinding.inflate(layoutInflater) }
    private lateinit var resultpw: String

    private var emailCheck = false      // 이메일
    private var nameCheck = false       // 이름
    private var nickCheck = false       // 닉네임
    private var pwCheck = false         // 비밀번호
    private var pwSameCheck = false     // 비밀번호 확인

    private val badWordFiltering = BadWordFiltering()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        /* ==============================================
           EditText 유효성 검사 기능
           ==============================================
        */
        // 이메일 유효성 검사
        binding.signupEtEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val emailPattern = Regex("\\w+@\\w+\\.\\w+(\\.\\w+)?")
                if (emailPattern.matches(binding.signupEtEmail.text))
                    emailCheck = true
                else {
                    binding.signupEtEmail.error = "이메일 형식으로 입력"
                    emailCheck = false
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        // 이름 유효성 검사
        binding.signupEtName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val namePattern = Regex("^[가-힣]*\$")
                if (namePattern.matches(binding.signupEtName.text))

                    nameCheck = true
                else {
                    binding.signupEtName.error = "한글만 입력 가능"
                    nameCheck = false
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        // 닉네임 유효성 검사 (특수문자 제외 1 ~ 7 입력)
        binding.signupEtNickname.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val nickPattern = Regex("^[^!@#\$%^&*()\\-_=+<>?/|\\[\\]{};:'\",.~`]{1,7}\$")
                if (nickPattern.matches(binding.signupEtNickname.text)) {
                    binding.signupBtnChecknick.setOnClickListener {
                        checkNickDuplicate(binding.signupEtNickname.text.toString())
                    }
                } else {
                    binding.signupEtNickname.error = "1 ~ 7자리 이내로 입력 (특수문자 제외)"
                    nickCheck = false
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        // 비밀번호 유효성 검사
        binding.signupEtPw.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val pwPattern = Regex("^(?![가-힣]).{8,15}\$")
                if (pwPattern.matches(binding.signupEtPw.text))
                    pwCheck = true
                else {
                    binding.signupEtPw.error = "8 ~ 15자리 이내 입력(한글제외)"
                    pwCheck = false
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        // 비밀번호 확인 유효성 검사
        binding.signupEtCheckpw.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (binding.signupEtCheckpw.text.toString() == binding.signupEtPw.text.toString()) {
                    binding.signupEtCheckpw.error = null
                    pwSameCheck = true
                } else {
                    binding.signupEtCheckpw.error = "비밀번호가 일치하지 않습니다."
                    pwSameCheck = false
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        /* ==============================================
           Firebase 회원 정보 등록 기능
           ==============================================
        */
        // Firebase Authentication과 연결
        auth = FirebaseAuth.getInstance()

        // 이메일 형식대로 입력하고 회원가입 정보를 제대로 입력한 경우
        binding.btnSignupaccept.setOnClickListener {
            val email = binding.signupEtEmail.text.toString()
            val nick = binding.signupEtNickname.text.toString()
            val name = binding.signupEtName.text.toString()
            val pw = binding.signupEtPw.text.toString()
            val checkpw = binding.signupEtCheckpw.text.toString()

            //val emailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

            if (email.isNotEmpty() && nick.isNotEmpty() && name.isNotEmpty() && pw.isNotEmpty() && checkpw.isNotEmpty()) {
                // 이메일 형식이 아닌 경우
                if (emailCheck && nameCheck && pwCheck && pwSameCheck) {
                    if (!nickCheck) {
                        showtoast("닉네임 중복체크를 확인해주세요.")
                    } else {
                        if (pw == checkpw) {
                            resultpw = pw
                            // 이메일 중복 확인
                            checkDuplicateEmail(email)
                            // 새로운 사용자 등록
                            createUser(email, name, nick, resultpw)
                        } else {
                            showtoast("비밀번호가 일치하지 않습니다.")
                        }
                    }
                } else {
                    showtoast("올바른 이메일 형식이 아닙니다.")
                }
            } else {
                showtoast("올바른 정보를 입력해주세요.")
            }
        }
        // 회원가입 취소 버튼 클릭 시 로그인 페이지로 돌아가기
        binding.btnSignupcancel.setOnClickListener {
            finish()
        }
    }

    /* ==============================================
         아이디 중복 체크 기능
         ==============================================
      */
    private fun checkNickDuplicate(nick: String) {
        val userReference = FirebaseDatabase.getInstance().getReference("users")

        userReference.orderByChild("nickname").equalTo(nick)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    // 이미 닉네임이 존재하는 경우
                    if (snapshot.exists()) {
                        nickCheck = false
                        binding.signupEtNickname.error = "이미 존재하는 닉네임 입니다."
                    } else if (badWordFiltering.check(nick)){
                        // 닉네임에 욕설이 포함되어 있는 경우
                        nickCheck = false
                        binding.signupEtNickname.error = "사용할 수 없는 닉네임 입니다."
                    } else {
                        nickCheck = true
                        showtoast("사용가능한 닉네임 입니다.")
                    }
                    checkBtnColor()
                }

                override fun onCancelled(error: DatabaseError) {
                    nickCheck = false
                    showtoast("닉네임 중복 확인 중 에러 발생")
                }
            })
    }

    private fun checkBtnColor() {
        if(!nickCheck) {
            changeBtnGrey()
        }
        else {
            changeBtnDesert()
        }
    }

    private fun changeBtnGrey() {
        binding.signupBtnChecknick.setBackgroundResource(R.drawable.signup_btn_border)
        binding.signupBtnChecknick.setTextColor(ContextCompat.getColor(this@SignUpActivity, R.color.black))
    }

    private fun changeBtnDesert() {
        binding.signupBtnChecknick.setBackgroundResource(R.drawable.signup_btn_checked_id)
        binding.signupBtnChecknick.setTextColor(ContextCompat.getColor(this@SignUpActivity, R.color.white))
    }

    private fun createUser(email: String, name: String, nick: String, pw: String) {
        // 올바른 정보 입력 후, 확인 버튼 클릭 시 Firebase의 Auth에 데이터 저장
        auth.createUserWithEmailAndPassword(email, pw)
            .addOnCompleteListener { task ->
                // 회원가입 성공 시
                if (task.isSuccessful) {
                    sendVerifyEmail()
                    SignUpDialog(this).show()
                    val user = auth.currentUser
                    // RealTimeDB 사용자 정보에 간단하게 이름, 아이디, 성별, 닉네임 DB 생성
                    val userData = UserData(name, email, nick)
                    if (user != null) {
                        saveUserData(user.uid, userData)
                    }
                }
                // 회원가입 실패 시
                else {
                    val error = task.exception?.message ?: "알 수 없는 오류"
                    showtoast("회원가입에 실패했습니다. 오류 : $error")
                }
            }
    }


    // RealTimeDB
    private fun saveUserData(uid: String, user: UserData) {
        val dataBase = FirebaseDatabase.getInstance()
        val userReference = dataBase.getReference("users")
        userReference.child(uid).setValue(user)
        val userId = userReference.push().key

        userId?.let {
            userReference.child(uid).setValue(user)
                .addOnCompleteListener { task ->
                    // 회원가입 성공 시
                    if (task.isSuccessful) {
                        //toastMessage("RealTime userData 생성 성공!")
                    }
                    // 회원가입 실패 시
                    else {
                        val error = task.exception?.message ?: "알 수 없는 오류"
                        showtoast("RealTime userData 생성 실패! 오류: $error")
                    }
                }
        }
    }

    // 인증 이메일 발송
    private fun sendVerifyEmail() {
        val user = auth.currentUser
        user?.sendEmailVerification()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // 인증 이메일 전송됨
                } else {
                    showtoast("오류..이메일 전송에 실패했습니다.")
                }
            }
    }

    // 중복 이메일이 있는지 확인하기
    private fun checkDuplicateEmail(email: String) {
        auth.fetchSignInMethodsForEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val signInMethod = task.result?.signInMethods
                    // 이메일이 중복되지 않은 경우
                    if (signInMethod.isNullOrEmpty()) {
                        registerUser(email, resultpw)
                    } else {
                        showtoast("이미 가입된 이메일 주소입니다.")
                    }
                }
            }
    }

    // Firebase에 사용자 등록하기
    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    sendVerifyEmail()
                }
            }
    }
}