package com.sandamso.sansaninfo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.sandamso.sansaninfo.Main.MainActivity
import com.sandamso.sansaninfo.SignPage.SignInActivity
import com.sandamso.sansaninfo.databinding.ActivitySplashScreenBinding
import com.google.firebase.auth.FirebaseAuth

class SplashScreen : AppCompatActivity() {

    private val binding by lazy { ActivitySplashScreenBinding.inflate(layoutInflater) }
    private val auth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val autoLogin = getSharedPreferences("prefLogin", 0)
        val saveEmail = getSharedPreferences("prefEmail", 0)
        val email = saveEmail.getString("email", "")
        // 저장된 데이터 불러오기
        if (autoLogin.getString("login", "") == "1") {
            // 자동 로그인 기능
            val pw = autoLogin.getString("pw", "")
            if (email != null && pw != null) {
                auth.signInWithEmailAndPassword(email, pw)
                    .addOnCompleteListener {
                        val user = auth.currentUser
                        if (user != null && user.isEmailVerified) {
                            Handler(Looper.getMainLooper()).postDelayed({
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                                finish()
                            }, 0)
                        }
                    }
            }
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                finish()
            }, 0)
        }
    }
}