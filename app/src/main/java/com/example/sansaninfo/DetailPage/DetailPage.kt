package com.example.sansaninfo.DetailPage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.sansaninfo.databinding.ActivityDetailPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.concurrent.thread

class DetailPage : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private val binding by lazy { ActivityDetailPageBinding.inflate(layoutInflater) }

    private var firebaseDatabase = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        Toast.makeText(this, "게시글 입력 완료", Toast.LENGTH_SHORT).show()

        init()
        dataView()

        binding.detailPageIvBack.setOnClickListener {
            finish()
        }
    }

    // 프로그래스 바
    fun init() {
        blockLayoutTouch()
        showProgress(true)
        thread(start = true) {
            Thread.sleep(2000)
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

    fun dataView() {
        auth = FirebaseAuth.getInstance()

        val titleData = intent.getStringExtra("dataFromAddPageTitle")
        val maintextData = intent.getStringExtra("dataFromAddPageMaintext")
        val imageData = intent.getStringExtra("dataFromAddPageimage")
        val date = getData()

        binding.detailPageTvTitle.text = titleData
        binding.detailPageTvMemo.text = maintextData
        binding.detailPageTvDate.text = "작성일: ${date}"
        Log.d("Image Tag", "$imageData")

        viewNickname()

        // 이미지 URI를 사용하여 Glide를 통해 이미지를 표시
        val storage = FirebaseStorage.getInstance()
        val pathReference = storage.reference.child("images/${imageData}")

        pathReference.downloadUrl.addOnSuccessListener {
            Log.d("Image Tag222", "$it")
            Glide.with(this).load(it).into(binding.detailPageIvMain)
        }.addOnFailureListener {
            Log.d("Image Tag333", "$it")
        }
    }

    fun getData(): String {
        val currentDateTime = Calendar.getInstance().time
        return SimpleDateFormat("yyyy.MM.dd", Locale.KOREA).format(currentDateTime)
    }

    fun viewNickname() {
        val uid = auth.currentUser?.uid ?: ""
        firebaseDatabase.child("users").child(uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val userData =
                            snapshot.getValue(com.example.sansaninfo.Data.UserData::class.java)
                        if (userData != null) {
                            val nickname = userData.nickname
                            binding.detailPageTvName.text = "작성자: ${nickname}"
                            Log.d("Nickname", "작성자: ${nickname}")
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("Nickname", "error = $error")
                }
            })
    }
}