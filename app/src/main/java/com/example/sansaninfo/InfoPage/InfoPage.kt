package com.example.sansaninfo.InfoPage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sansaninfo.R
import com.example.sansaninfo.databinding.ActivityInfoPageBinding

class InfoPage : AppCompatActivity() {

    private val binding by lazy { ActivityInfoPageBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 산림청_산_정보 인증키 - 4bpUeSQaXnUDSalDsumQ5dkxA+bJXWN4dhwsYexJp6wAJnadjR+UoIVo1Dhac/spEq1HRVngbbHuY8QLzUwVBg==

        initView()

    }

    private fun initView() = with(binding) {

    }
}