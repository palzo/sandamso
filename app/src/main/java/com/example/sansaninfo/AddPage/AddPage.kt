package com.example.sansaninfo.AddPage

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import android.Manifest
import android.content.Intent
import android.provider.ContactsContract.CommonDataKinds.Nickname
import android.provider.ContactsContract.DisplayPhoto
import android.widget.ImageView
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.sansaninfo.Data.FBRef
import com.example.sansaninfo.DetailPage.DetailPage
import com.example.sansaninfo.Main.MainActivity
import com.example.sansaninfo.R
import com.example.sansaninfo.databinding.ActivityAddPageBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.FileOutputStream
import java.io.IOException

class AddPage : AppCompatActivity() {

    private val binding by lazy { ActivityAddPageBinding.inflate(layoutInflater) }

    val storage = Firebase.storage("gs://sansaninfo-7819a.appspot.com")

//        private lateinit var imageuri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // firebase database 키 생성하기
        fun addItem(user: User) {
            val id = FBRef.myRef.push().key!!
            user.id = id
            FBRef.myRef.child(id).setValue(user)
        }
        with(binding) {
            button.setOnClickListener {
                val title = addPageTvTitle.text.toString()
                val maintext = textView7.text.toString()
                val user = User(title, maintext)
                addItem(user)

//                uploadImage(imageuri)

                val intent = Intent(this@AddPage, DetailPage::class.java)
                intent.putExtra("dataFromAddPageTitle", title)
                intent.putExtra("dataFromAddPageMaintext", maintext)
//                intent.putExtra("dataFromAddPageImage",imageuri)
                startActivity(intent)
            }
        }

        binding.addPageIvAdd.setOnClickListener {
            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        //뒤로가기
        binding.addPageIvBackbutton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

        //권한 요청하기
        val permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    galleryLauncher.launch("image/*")
                } else {
                    Toast.makeText(baseContext, "외부 저장소 읽기 권한을 승인해야 사용할 수 있습니다.", Toast.LENGTH_LONG)
                        .show()
                }
            }

        //이미지 갤러리 불러오기
        val galleryLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
                if (uri != null) {
                    uploadImage(uri)
                }
                binding.addPageIvAddPhoto.isVisible = true
                binding.addPageIvAddPhoto.setImageURI(uri)
            }

    fun uploadImage(uri: Uri) {
        val fullPath = makeFilePath("images", "temp", uri)
        val imageRef = storage.getReference(fullPath)
        val uploadTask = imageRef.putFile(uri)

        // 업로드 실행 및 결과 확인
        uploadTask.addOnFailureListener {
            Log.d("Storage", "Fail -> ${it.message}")
        }.addOnSuccessListener { taskSnapshot ->
            Log.d(
                "Storage",
                "Success Address -> ${fullPath}"
            )
        }
    }

    fun makeFilePath(path: String, userId: String, uri: Uri): String {
        val mimeType = contentResolver.getType(uri) ?: "/none" // MIME 타입 ex) images/jpeg
        val ext = mimeType.split("/")[1] // 확장자 ex) jpeg
        val timeSuffix = System.currentTimeMillis() // 시간값 ex) 1235421532
        val filename = "${path}/${userId}_${timeSuffix}.${ext}" // 완성!
        return filename
    }
}

class User {
    var id: String = ""
    var title: String = ""
    var maintext: String = ""

    constructor()

    constructor(title: String, maintext: String) {
        this.title = title
        this.maintext = maintext
    }
}


