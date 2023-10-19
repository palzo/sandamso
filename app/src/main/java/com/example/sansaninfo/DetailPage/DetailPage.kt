package com.example.sansaninfo.DetailPage

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.sansaninfo.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DetailPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_page)

        Toast.makeText(this, "게시글 입력 완료", Toast.LENGTH_SHORT).show()

        val titleData = intent.getStringExtra("dataFromAddPageTitle")
        val title = findViewById<TextView>(R.id.detail_page_tv_title)
        val maintextData = intent.getStringExtra("dataFromAddPageMaintext")
        val maintext = findViewById<TextView>(R.id.detail_page_tv_memo)
        val date = getData()
        val datetime = findViewById<TextView>(R.id.detail_page_tv_date)
//        val imageuri:Uri? = intent.getParcelableExtra("dataFromAddPageImage")

        title.text = titleData
        maintext.text = maintextData
        datetime.text = "작성일: ${date}"

        val db = Firebase.firestore
        val currentUser = Firebase.auth.currentUser

        if (currentUser != null) {
            db.collection("POST")
                .whereEqualTo("id", currentUser.uid).get().addOnSuccessListener { result ->
                    for (document in result) {
                        val imageUrl = document.getString("imageUrl")
                        if (imageUrl != null) {
                            // 이미지 URL을 사용하여 이미지 로드 및 표시
                            val imageView = findViewById<ImageView>(R.id.detail_page_iv_main)
                            Glide.with(this).load(imageUrl).into(imageView)
                        }
                    }
                }
                .addOnFailureListener { error -> Log.d("Image Tag","Error getting documents.",error) }
        }


//        val storage =Firebase.storage
//        val storageRef = storage.getReference("image/*")
//        // 다운로드된 이미지 URI를 사용하여 Glide를 통해 이미지를 표시
//        Glide.with(this).load(storageRef).into(imageView)
//        // Create a reference to a file from a Google Cloud Storage URI
//        val gsReference = storage.getReferenceFromUrl("gs://bucket/images/stars.jpg")
//        val imageView = findViewById<ImageView>(R.id.detail_page_iv_main)
//        Glide.with(this)
//            .load(gsReference)
//            .into(imageView)

    }

    fun makeFilePath(path: String, userId: String, uri: Uri): String {
        val mimeType = contentResolver.getType(uri) ?: "/none" // MIME 타입 ex) images/jpeg
        val ext = mimeType.split("/")[1] // 확장자 ex) jpeg
        val timeSuffix = System.currentTimeMillis() // 시간값 ex) 1235421532
        val filename = "${path}/${userId}_${timeSuffix}.${ext}" // 완성!
        return filename
    }

    fun getData(): String {
        val currentDateTime = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.KOREA).format(currentDateTime)
        return dateFormat
    }


}