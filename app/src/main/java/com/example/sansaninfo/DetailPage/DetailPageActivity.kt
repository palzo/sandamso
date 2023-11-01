package com.example.sansaninfo.DetailPage

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import coil.load
import com.example.sansaninfo.AddPage.AddPageActivity
import com.example.sansaninfo.Data.PostModel
import com.example.sansaninfo.R
import com.example.sansaninfo.databinding.ActivityDetailPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.concurrent.thread

class DetailPageActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private val binding by lazy { ActivityDetailPageBinding.inflate(layoutInflater) }

    private var firebaseDatabase = FirebaseDatabase.getInstance().reference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        init()
        deleteData()

        binding.detailPageLlRevise.setOnClickListener {
            val intent = Intent(this@DetailPageActivity, AddPageActivity::class.java)
            startActivity(intent)
        }

        binding.detailPageIvBack.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        dataView()
        calculateDday()
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

        val postId = intent.getStringExtra("dataFromAddPageId") ?: ""
        Log.d("postId", "$postId")

        firebaseDatabase.child("POST").child(postId).get().addOnCompleteListener {
            if (it.isSuccessful) {
                val userData =
                    it.result.getValue(PostModel::class.java)
                Log.d("userData", "$userData")

                if (userData == null) return@addOnCompleteListener
                val titleData = userData.title
                val maintextData = userData.maintext
                val dateData = userData.date
                val nicknameData = userData.nickname
                val deadlineData = userData.deadlinedate
                val imageData = userData.image
                val kakaoData = userData.kakao

                binding.detailPageTvTitle.text = titleData
                binding.detailPageTvMemo.text = maintextData
                binding.detailPageTvDate.text = "작성일: ${dateData}"
                binding.detailPageTvName.text = "작성자: ${nicknameData}"
                binding.detailPageTvGather.text = "${deadlineData}까지 모집"
                Log.d("Image Tag", "$imageData")

                // 카카오톡 오픈채팅으로 이동하기
                binding.detailPageLlKakaoChat.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(kakaoData))
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

    // 삭제하기 - 다이얼로그 구현하기
    fun deleteData() {
        binding.detailPageLlDelete.setOnClickListener {
            var builder = AlertDialog.Builder(this)
            builder.setTitle("게시글을 삭제하시겠습니까?")
            builder.setMessage("삭제된 게시글은 다시 복구할 수 없습니다.")
            builder.setIcon(R.drawable.alert_triangle)

            val listener = object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, p1: Int) {
                    when (p1) {
                        DialogInterface.BUTTON_POSITIVE -> {
                            deletePostData()
                        }

                        DialogInterface.BUTTON_NEGATIVE -> {
                        }
                    }
                }
            }
            builder.setPositiveButton("확인", listener)
            builder.setNegativeButton("취소", listener)
            builder.show()
        }
    }

    // DB에 저장된 게시물 데이터 삭제하기
    fun deletePostData() {
        val postId = intent.getStringExtra("dataFromAddPageId")
        val database = FirebaseDatabase.getInstance()
        val postReference = postId?.let { database.getReference("POST").child(it) }

        postReference?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // 게시물 데이터를 찾았을 때 삭제 작업을 수행
                    postReference.removeValue().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            deleteImage()
                        } else {
                            Toast.makeText(
                                this@DetailPageActivity,
                                "삭제 중 오류가 발생했습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    // 게시물 데이터를 찾을 수 없을 때 오류 처리
                    Toast.makeText(this@DetailPageActivity, "게시물을 찾을 수 없습니다.", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // 오류 처리
                Log.e("Firebase", "Firebase Database Error: ${error.message}")
                Toast.makeText(this@DetailPageActivity, "Firebase Database 오류", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    // Storage에 저장된 이미지 삭제하기
    fun deleteImage() {
        val imageData = intent.getStringExtra("dataFromAddPageimage")
        val storage = FirebaseStorage.getInstance()
        val imageReference = storage.reference.child("images/${imageData}")

        imageReference.delete().addOnSuccessListener {
            Toast.makeText(this, "게시글이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
            finish()
        }.addOnFailureListener {
            Log.d("fail", "image delete")
        }
    }
}