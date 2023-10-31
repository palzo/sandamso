package com.example.sansaninfo.DetailPage

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import coil.load
import com.example.sansaninfo.databinding.ActivityDetailPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.concurrent.thread

class DetailPageActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private val binding by lazy { ActivityDetailPageBinding.inflate(layoutInflater) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        init()
        dataView()
        calculateDday()

        binding.detailPageIvBack.setOnClickListener {
            finish()
        }
    }

    // 프로그래스 바
    fun init() {
        blockLayoutTouch()
        showProgress(true)
        thread(start = true) {
            Thread.sleep(1000)
            runOnUiThread {
                clearBlockLayoutTouch()
                showProgress(false)
            }
        }
    }

    fun showProgress(isShow: Boolean) {
        if (isShow) binding.progressBar.visibility = View.VISIBLE
        else binding.progressBar.visibility = View.GONE
    }

    // 화면 터치 막기
    private fun blockLayoutTouch() {
        window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    // 화면 터치 풀기
    private fun clearBlockLayoutTouch() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    // Add Page에서 입력한 데이터 가져오기
    fun dataView() {
        auth = FirebaseAuth.getInstance()

        val titleData = intent.getStringExtra("dataFromAddPageTitle")
        val maintextData = intent.getStringExtra("dataFromAddPageMaintext")
        val imageData = intent.getStringExtra("dataFromAddPageimage")
        val kakaoData = intent.getStringExtra("dataFromAddPagekakao")
        val dateData = intent.getStringExtra("dataFromAddPagedate")
        val nicknameData = intent.getStringExtra("dataFromAddPagenickname")
        val deadlineData = intent.getStringExtra("dataFromAddPagedday")

        binding.detailPageTvTitle.text = titleData
        binding.detailPageTvMemo.text = maintextData
        binding.detailPageTvDate.text = "작성일: ${dateData}"
        binding.detailPageTvName.text = "작성자: ${nicknameData}"
        binding.detailPageTvGather.text = "${deadlineData}까지 모집"
        Log.d("Image Tag", "$imageData")

        // 카카오톡 오픈채팅으로 이동하기
        binding.detailPageLlKakaoChat.setOnClickListener {
            var intent = Intent(Intent.ACTION_VIEW, Uri.parse(kakaoData))
            startActivity(intent)
        }

        // 이미지 URI를 사용하여 이미지 표시
        val storage = FirebaseStorage.getInstance()
        val pathReference = storage.reference.child("images/${imageData}")

        pathReference.downloadUrl.addOnSuccessListener {
            Log.d("Image Tag222", "$it")
            binding.detailPageIvMain.load(it)
        }.addOnFailureListener {
            Log.d("Image Tag333", "$it")
        }
    }

    // 디데이 계산하기
    fun calculateDday() {
        val dateData = intent.getStringExtra("dataFromAddPagedday")
        val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA)
        val currentDate = Date()
        val targetDate = dateFormat.parse(dateData)

        if (targetDate != null) {
            val timeDiff = targetDate.time - currentDate.time
            val dday = timeDiff / (1000 * 60 * 60 * 24)

            if (dday.toInt() == 0) {
                binding.detailPageTvDday.text = "D-Day"
            } else if (dday > 0) {
                binding.detailPageTvDday.text = "D-${dday}"
            } else if (dday < 0) {
                val outday = -dday
                binding.detailPageTvDday.text = "D+${outday}"
            }
        } else {
            binding.detailPageTvDday.text = "유효하지 않은 날짜"
        }
    }
}