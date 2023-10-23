package com.example.sansaninfo.DetailPage

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sansaninfo.CommunityPage.CommunityPageFragment
import com.example.sansaninfo.R
import com.example.sansaninfo.databinding.ActivityDetailPageBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DetailPage : AppCompatActivity() {

    private val binding by lazy { ActivityDetailPageBinding.inflate(layoutInflater) }

    lateinit var communityPageFragment: CommunityPageFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_page)

        Toast.makeText(this, "게시글 입력 완료", Toast.LENGTH_SHORT).show()

        val titleData = intent.getStringExtra("dataFromAddPageTitle")
        val title = findViewById<TextView>(R.id.detail_page_tv_title)
        val maintextData = intent.getStringExtra("dataFromAddPageMaintext")
        val maintext = findViewById<TextView>(R.id.detail_page_tv_memo)
        val imageData = intent.getStringExtra("dataFromAddPageimage")
        val date = getData()
        val datetime = findViewById<TextView>(R.id.detail_page_tv_date)

        title.text = titleData
        maintext.text = maintextData
        datetime.text = "작성일: ${date}"
        Log.d("Image Tag", "${imageData}")

        // 이미지 URI를 사용하여 Glide를 통해 이미지를 표시
        val imageView = findViewById<ImageView>(R.id.detail_page_iv_main)
        val storage = FirebaseStorage.getInstance()
        val pathReference = storage.reference.child("images/${imageData}")

        pathReference.downloadUrl.addOnSuccessListener {
            Log.d("Image Tag222", "${it}")
            Glide.with(this).load(it).into(imageView)
        }.addOnFailureListener {
            Log.d("Image Tag333", "${it}")
        }

        binding.detailPageIvBack.setOnClickListener {
            communityPageFragment
        }
    }

    fun getData(): String {
        val currentDateTime = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.KOREA).format(currentDateTime)
        return dateFormat
    }
}