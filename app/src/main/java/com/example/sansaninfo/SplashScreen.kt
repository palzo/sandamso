package com.example.sansaninfo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.sansaninfo.Main.MainActivity
import com.example.sansaninfo.SignPage.SignInActivity
import com.example.sansaninfo.databinding.ActivitySplashScreenBinding

class SplashScreen : AppCompatActivity() {

    private val binding by lazy { ActivitySplashScreenBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_down_enter, R.anim.slide_down_exit)
            finish()
        }, 3000)


    }
}