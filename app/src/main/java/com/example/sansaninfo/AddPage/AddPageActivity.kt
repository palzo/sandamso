package com.example.sansaninfo.AddPage

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.annotation.RequiresApi
import com.example.sansaninfo.Data.FBRef
import com.example.sansaninfo.Data.PostModel
import com.example.sansaninfo.DetailPage.DetailPageActivity
import com.example.sansaninfo.Main.MainActivity
import com.example.sansaninfo.databinding.ActivityAddPageBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.concurrent.thread

class AddPageActivity : AppCompatActivity() {

    private val binding by lazy { ActivityAddPageBinding.inflate(layoutInflater) }

    val storage = Firebase.storage("gs://sansaninfo-7819a.appspot.com")

    // 높은 API 버전에도 권한 요청하기
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private val api33 = arrayOf(
        Manifest.permission.READ_MEDIA_IMAGES
    )

    private val permission = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        titleCount()
        textCount()

        init()

        with(binding) {
            button.setOnClickListener {

                // 값을 모두 입력할 수 있도록 이벤트 설정해주기
                val title = addPageTvTitle.text.toString()
                val maintext = addPageEtText.text.toString()
                val kakao = addPageTvKakaoOpen.text.toString()
                val imageuri = addPageIvAdd.tag?.toString()

                if (title.isEmpty()) {
                    toastMessage("제목을 입력해주세요.")

                } else if (title.length >= 30) {
                    toastMessage("제목은 30글자 이하로 입력해주세요.")

                } else if (imageuri == null || imageuri.isEmpty()) {
                    toastMessage("이미지를 첨부해 주세요.")

                } else if (maintext.isEmpty()) {
                    toastMessage("본문 내용을 입력해주세요.")


                } else if (maintext.length >= 5000) {
                    toastMessage("본문은 5000글자까지만 입력이 가능합니다.")

                } else if (kakao.isEmpty()) {
                    toastMessage("카카오 오픈채팅 링크를 입력해주세요.")

                } else {
                    showProgress(true)
                    goneData()
                    data()
                    Toast.makeText(this@AddPageActivity, "게시글 입력 완료", Toast.LENGTH_SHORT).show()
                    thread(start = true) {
                        Thread.sleep(2500)
                        runOnUiThread {
                            showProgress(false)
                        }
                    }
                }
            }
        }

        binding.addPageIvAdd.setOnClickListener {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED)
                    requestPermissions(api33, 100)
                else {
                    galleryLauncher.launch("image/*")
                }
            } else {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermissions(permission, 100)
                } else {
                    galleryLauncher.launch("image/*")
                }
            }

            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)

        }

        //뒤로가기
        binding.addPageIvBackbutton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    // 데이터 입력한 값 넘겨주기
    fun data() {
        with(binding) {
            val title = addPageTvTitle.text.toString()
            val maintext = addPageEtText.text.toString()
            val kakao = addPageTvKakaoOpen.text.toString()
            val uri = Uri.parse(addPageIvAdd.tag.toString())
            uploadImage(uri) {
                if (it != null) {

                    // 날짜 데이터 저장
                    val date = getData()

                    // 파이어 베이스에 데이터 추가하기
                    var user = PostModel(
                        title = title,
                        maintext = maintext,
                        image = it,
                        kakao = kakao,
                        date = date,
                        writer = Firebase.auth.currentUser?.uid
                    )

                    // 닉네임도 넣어주기
                    setNickname { nickname ->
                        user = user.copy(nickname = nickname)
                        addItem(user)

                        val intent = Intent(this@AddPageActivity, DetailPageActivity::class.java)
                        intent.putExtra("dataFromAddPageTitle", title)
                        intent.putExtra("dataFromAddPageMaintext", maintext)
                        intent.putExtra("dataFromAddPageimage", it)
                        intent.putExtra("dataFromAddPagekakao", kakao)
                        intent.putExtra("dataFromAddPagedate", date)
                        intent.putExtra("dataFromAddPagenickname", nickname)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }

    }

    // firebase database 키 생성하기
    fun addItem(user: PostModel): String {
        val id = FBRef.myRef.push().key!!
        user.id = id
        FBRef.myRef.child(id).setValue(user)
        return id
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
            binding.addPageIvAdd.setImageURI(uri)
            binding.addPageIvAdd.tag = uri.toString()
        }

    // 권한 요청 처리 결과 수신하기
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            when (requestCode) {
                100 -> {
                    Toast.makeText(this@AddPageActivity, "갤러리 불러오기 권한이 허용되었습니다.", Toast.LENGTH_SHORT).show()
                    galleryLauncher.launch("image/*")
                }
            }
        }
    }

    fun uploadImage(uri: Uri, onSuccess: (String?) -> Unit) {
        val fullPath = makeFilePath("images", "temp", uri)
        val imageRef = storage.getReference(fullPath)
        val uploadTask = imageRef.putFile(uri)

        // 업로드 실행 및 결과 확인
        uploadTask.addOnFailureListener {
            Log.d("Storage", "Fail -> ${it.message}")
        }.addOnSuccessListener { taskSnapshot ->
            Log.d(
                "Storage",
                "Success Address -> ${taskSnapshot.metadata?.name}"
            )
            onSuccess(taskSnapshot.metadata?.name)
        }
    }

    fun makeFilePath(path: String, userId: String, uri: Uri): String {
        val mimeType = contentResolver.getType(uri) ?: "/none" // MIME 타입 ex) images/jpeg
        val ext = mimeType.split("/")[1] // 확장자 ex) jpeg
        val timeSuffix = System.currentTimeMillis() // 시간값 ex) 1235421532
        val filename = "${path}/${userId}_${timeSuffix}.${ext}" // 완성!
        return filename
    }

    // 프로그래스 바
    private fun init() {
        showProgress(false)
    }

    fun showProgress(isShow: Boolean) {
        if (isShow) binding.progressBar.visibility = View.VISIBLE
        else binding.progressBar.visibility = View.GONE
    }

    fun goneData() {
        with(binding) {
            addPageGroup.visibility = View.GONE
        }
    }

    // 텍스트 실시간 데이터 반영해 글자 수 변경하기
    private fun titleCount() {
        with(binding) {
            addPageTvTitle.addTextChangedListener(object : TextWatcher {
                // 작성 전
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    addPageTvTitleLimit.text = "0 / 30"
                }

                // 작성 중
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val userinput = addPageTvTitle.text.toString()
                    val inputlength = userinput.length
                    addPageTvTitleLimit.text = "$inputlength / 30"

                    // 30글자 이상 입력 방지
                    if (inputlength > 30) {
                        addPageTvTitle.error = "30글자를 초과하셨습니다."
                    } else {
                        // 30글자 이하이면 경고 메시지 제거하기
                        addPageTvTitle.error = null
                    }
                }

                // 작성 후
                override fun afterTextChanged(s: Editable?) {
                    var userinput = addPageTvTitle.text.toString()
                    val inputlength = userinput.length
                    addPageTvTitleLimit.text = "$inputlength / 30"
                }
            })
        }
    }

    // 텍스트 실시간 데이터 반영해 글자 수 변경하기 22
    private fun textCount() {
        with(binding) {
            addPageEtText.addTextChangedListener(object : TextWatcher {
                // 작성 전
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    addPageTvTextLimit.text = "0 / 5000"
                }

                // 작성 중
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    var userinput = addPageEtText.text.toString()
                    val inputlength = userinput.length
                    addPageTvTextLimit.text = "$inputlength / 5000"

                    // 5000글자 이상 입력 방지
                    if (inputlength > 5000) {
                        addPageTvTitle.error = "5000글자를 초과하셨습니다."
                    } else {
                        // 5000글자 이하이면 경고 메시지 제거하기
                        addPageTvTitle.error = null
                    }
                }

                // 작성 후
                override fun afterTextChanged(s: Editable?) {
                    var userinput = addPageEtText.text.toString()
                    val inputlength = userinput.length
                    addPageTvTextLimit.text = "$inputlength / 5000"
                }
            })
        }
    }

    // 날짜 데이터 표시
    fun getData(): String {
        val currentDateTime = Calendar.getInstance().time
        return SimpleDateFormat("yyyy.MM.dd", Locale.KOREA).format(currentDateTime)
    }

    // Firebase에서 닉네임 가져오기
    fun setNickname(onNickNameFetched: (String) -> Unit) {
        val uid = Firebase.auth.currentUser?.uid ?: ""
        FirebaseDatabase.getInstance().reference.child("users").child(uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val userData =
                            snapshot.getValue(com.example.sansaninfo.Data.UserData::class.java)
                        if (userData != null) {
                            val nickname = userData.nickname
                            onNickNameFetched(nickname)
                            Log.d("Nickname", "작성자: ${nickname}")
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("Nickname", "error = $error")
                }
            })
    }

    private fun toastMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}


