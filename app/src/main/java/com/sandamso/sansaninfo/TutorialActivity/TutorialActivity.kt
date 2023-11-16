package com.sandamso.sansaninfo.TutorialActivity

import android.content.Intent
import com.sandamso.sansaninfo.R
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.sandamso.sansaninfo.databinding.ActivityTutorialBinding
import com.sandamso.sansaninfo.signpage.SignInActivity


class TutorialActivity : AppCompatActivity() {

    private val binding by lazy { ActivityTutorialBinding.inflate(layoutInflater) }
    private var currentIdx: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 건너뛰기 버튼 클릭시 메인화면으로 이동
        binding.tutorialBtnSkip.setOnClickListener {
            sharedPreference()
        }

        // 조건문을 통해 버튼 하나로 두개의 상황을 실행
        binding.tutorialBtnNext.setOnClickListener {
            binding.tutorialIv.scaleType = ImageView.ScaleType.FIT_XY
            currentIdx++
            if (currentIdx < 8) {
                // 마지막 페이지가 아니라면 다음 페이지로 이동
                when (currentIdx) {
                    1 -> {
                        binding.tutorialIv.setImageResource(R.drawable.tutorial02)
                    }

                    2 -> {
                        binding.tutorialIv.setImageResource(R.drawable.tutorial03)
                    }

                    3 -> {
                        binding.tutorialIv.setImageResource(R.drawable.tutorial04)
                    }

                    4 -> {
                        binding.tutorialIv.setImageResource(R.drawable.tutorial05)
                    }

                    5 -> {
                        binding.tutorialIv.setImageResource(R.drawable.tutorial06)
                    }

                    6 -> {
                        binding.tutorialIv.setImageResource(R.drawable.tutorial07)
                    }

                    7 -> {
                        binding.tutorialIv.setImageResource(R.drawable.tutorial08)
                        binding.tutorialBtnNext.text = "시작"
                    }
                }
            } else {
                sharedPreference()
            }
        }
    }
    private fun sharedPreference(){
        val loginInfo = getSharedPreferences("prefLogin", 0)
        val checkTutorial = loginInfo.getString("tutorial", "")
        if(checkTutorial == ""){
            val tutorialEdit = loginInfo.edit()
            tutorialEdit.putString("tutorial", "0")
            tutorialEdit.apply()
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit)
            finish()
        }else if (checkTutorial == "0"){
            finish()
        }
    }
}