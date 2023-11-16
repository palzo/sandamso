package com.sandamso.sansaninfo.signpage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sandamso.sansaninfo.databinding.ActivityFindpwResultBinding

class FindpwResultActivity : AppCompatActivity() {
    private val binding by lazy { ActivityFindpwResultBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.findpwresultBtn.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        binding.findpwresultBtnBack.setOnClickListener {
            finish()
        }
    }
}