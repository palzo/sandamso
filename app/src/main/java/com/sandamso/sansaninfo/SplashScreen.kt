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
import java.lang.Exception

class SplashScreen : AppCompatActivity() {

    private val binding by lazy { ActivitySplashScreenBinding.inflate(layoutInflater) }
    private val auth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val loginInfo = getSharedPreferences("prefLogin", 0)
        val email = loginInfo.getString("email", "")
        val pw = loginInfo.getString("pw", "")
        // 저장된 데이터 불러오기
        if (loginInfo.getString("loginType", "") == "2") {
            // 자동 로그인 기능
            if (email != null && pw != null) {
                try{
                    auth.signInWithEmailAndPassword(email, pw)
                        .addOnCompleteListener {
                            val user = auth.currentUser
                            if (user != null && user.isEmailVerified) {
                                Handler(Looper.getMainLooper()).postDelayed({
                                    startMainActivity()
                                }, 0)
                            }
                        }
                }catch (e: Exception){
                    val loginInfo = getSharedPreferences("prefLogin", 0)
                    val loginInfoEdit = loginInfo.edit()
                    loginInfoEdit.putString("loginType", "0")
                    loginInfoEdit.putString("pw", "0")
                    loginInfoEdit.apply()
                    Handler(Looper.getMainLooper()).postDelayed({
                        startSignInActivity()
                    }, 0)
                }
            }
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                startSignInActivity()
            }, 0)
        }
    }
    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        finish()
    }
    private fun startSignInActivity() {
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        finish()
    }
}