package com.sandamso.sansaninfo.AddPage

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import coil.load
import com.sandamso.sansaninfo.Data.FBRef
import com.sandamso.sansaninfo.Data.PostModel
import com.sandamso.sansaninfo.DetailPage.DetailPageActivity
import com.sandamso.sansaninfo.Main.MainActivity
import com.sandamso.sansaninfo.R
import com.sandamso.sansaninfo.databinding.ActivityAddPageBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.sandamso.sansaninfo.ChattingPage.ChatRoomListAdapter
import com.sandamso.sansaninfo.Data.FBRoom
import com.sandamso.sansaninfo.Data.RoomData
import com.sandamso.sansaninfo.SearchPage.MountainMapping
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.CompletableFuture
import java.util.function.Supplier
import kotlin.concurrent.thread

class AddPageActivity : AppCompatActivity() {

    private val binding by lazy { ActivityAddPageBinding.inflate(layoutInflater) }

    private val roomList = mutableListOf<RoomData>()

    private val chatRoomListAdapter by lazy {
        ChatRoomListAdapter(roomList)
    }

    private var addImage = false

    private var firebaseDatabase = FirebaseDatabase.getInstance().reference

    private val storage = Firebase.storage("gs://sansaninfo-7819a.appspot.com")


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
        editData()
        btnChange()

        mntCheck()

        with(binding) {
            postButton.setOnClickListener {

                if (addPageTvTitle.text.toString().isEmpty()) {
                    toastMessage("제목을 입력해주세요.")

                } else if (addPageTvTitle.text.toString().length >= 30) {
                    toastMessage("제목은 30글자 이하로 입력해주세요.")

                } else if (!addImage) {
                    toastMessage("이미지를 첨부해 주세요.")

                } else if (addPageTvDday.text.toString().isEmpty()) {
                    toastMessage("모임 마감일을 설정해주세요.")

                } else if (addPageEtText.text.toString().isEmpty()) {
                    toastMessage("본문 내용을 입력해주세요.")

                } else if (addPageEtText.text.toString().length >= 5000) {
                    toastMessage("본문은 5000글자까지만 입력이 가능합니다.")

                } else if (addPageEtMnt.text.toString().isEmpty()) {
                    toastMessage("산이름을 입력해주세요.")

                } else {
                    if (addPageBtnMnt.text == "변경") {
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
                    } else {
                        toastMessage("확인 버튼을 눌러주세요.")
                    }
                }
            }
        }


        // 날짜 다이얼로그
        binding.addPageClDday.setOnClickListener {
            dateDialogue()
        }

        // 이미지 추가하기
        binding.addPageIvAdd.setOnClickListener {
            addImage()
        }

        //뒤로가기
        binding.addPageIvBackbutton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun mntCheck() {
        with(binding) {
            addPageBtnMnt.setOnClickListener {
                val background = resources.getDrawable(R.drawable.add_page_stroke_round_rec)

                // 존재하는 산인지 찾기
                val check = MountainMapping.getMountainCode(addPageEtMnt.text.trim().toString())
                val mntList = listOf("한라산")

                if ((check != 0 || mntList.contains(
                        addPageEtMnt.text.trim().toString()
                    )) && addPageBtnMnt.text == "확인"
                ) {
                    addPageBtnMnt.text = "변경"
                    addPageEtMnt.isEnabled = false
                    addPageEtMnt.background = background
                    addPageErrMsg.visibility = View.INVISIBLE

                } else if ((check != 0 || mntList.contains(
                        addPageEtMnt.text.trim().toString()
                    )) && addPageBtnMnt.text == "변경"
                ) {
                    addPageBtnMnt.text = "확인"
                    addPageEtMnt.isEnabled = true
                    addPageEtMnt.background = background
                    addPageErrMsg.visibility = View.INVISIBLE
                } else {
//                    Toast.makeText(this@AddPageActivity,"존재하지않는 산입니다.", Toast.LENGTH_SHORT).show()
                    val errBackground =
                        resources.getDrawable(R.drawable.add_page_stroke_round_rec_err)
                    addPageEtMnt.background = errBackground
                    addPageErrMsg.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun btnChange() {
//        Toast.makeText(this, "${intent.getStringExtra("switch")}로 들어옴", Toast.LENGTH_SHORT).show()
        with(binding) {
            when (intent.getStringExtra("switch")) {
                "add" -> {
                    postButton.visibility = View.VISIBLE
                    completeButton.visibility = View.INVISIBLE
                    cancelButton.visibility = View.INVISIBLE
                }

                "edit" -> {
                    postButton.visibility = View.INVISIBLE
                    completeButton.visibility = View.VISIBLE
                    cancelButton.visibility = View.VISIBLE
                }

                else -> {

                }
            }
        }
    }

    // 데이터 입력한 값 넘겨주기
    fun data() {
        with(binding) {
            val uri = Uri.parse(addPageIvAdd.tag.toString())

            uploadImage(uri) {
                if (it != null) {

                    // 날짜 데이터 저장
                    val date = getData()

                    // 파이어 베이스에 POST 데이터 추가하기
                    var user = PostModel(
                        title = addPageTvTitle.text.toString(),
                        maintext = addPageEtText.text.toString(),
                        image = it,
                        mountain = addPageEtMnt.text.toString(),
                        date = date,
                        deadlinedate = addPageTvDday.text.toString(),
                        writer = Firebase.auth.currentUser?.uid,
                    )

                    // 닉네임도 넣어주기
                    setNickname { nickname ->
                        user = user.copy(nickname = nickname)
                        val postId = addItem(user)
                        val uid = Firebase.auth.currentUser?.uid ?: ""
                        val roomdata = RoomData()
                        roomdata.title = addPageTvTitle.text.toString()
                        roomdata.postId = postId
                        roomdata.users = mapOf(uid to uid)
//                        roomdata.users[uid] = true

                        // 파이어 베이스에 Rooms 데이터 추가하기
                        val roomId = addMsgData(
                            roomdata
                        )

                        // 파이어 베이스 POST - roomId 보여주기
                        val map = mutableMapOf<String, Any>()
                        map["roomId"] = roomId
                        firebaseDatabase.child("POST").child(postId).updateChildren(map)

                        val intent = Intent(this@AddPageActivity, DetailPageActivity::class.java)
                        intent.putExtra("dataFromAddPageTitle", addPageTvTitle.text.toString())
                        intent.putExtra("dataFromAddPageMaintext", addPageEtText.text.toString())
                        intent.putExtra("dataFromAddPagedday", addPageTvDday.text.toString())
                        intent.putExtra("dataFromAddPageMnt", addPageEtMnt.text.toString())
                        intent.putExtra("dataFromAddPageimage", it)
                        intent.putExtra("dataFromAddPagedate", date)
                        intent.putExtra("dataFromAddPagenickname", nickname)
                        intent.putExtra("dataFromAddPageId", postId)
                        intent.putExtra("dataFromAddPageWriter", Firebase.auth.currentUser?.uid)
                        intent.putExtra("dataFromAddPageRoomId", roomId)

                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }

    // firebase database 키 생성하기
    private fun addItem(user: PostModel): String {
        val id = FBRef.myRef.push().key!!
        user.id = id
        FBRef.myRef.child(id).setValue(user)
        return id
    }

    private fun addMsgData(user: RoomData): String {
        val id = FBRoom.roomRef.push().key!!
        user.id = id
        FBRoom.roomRef.child(id).setValue(user)
        return id
    }

    // 이미지 추가하기
    private fun addImage() {
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

    //권한 요청하기
    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                galleryLauncher.launch("image/*")
            }
        }

    //이미지 갤러리 불러오기
    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            addImage = !(uri == null || uri.toString().isEmpty())
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
                    Toast.makeText(
                        this@AddPageActivity,
                        "갤러리 불러오기 권한이 허용되었습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                    galleryLauncher.launch("image/*")
                }
            }
        }
    }

    // 스토리지에 이미지 저장하기(경로 찾기)
    private fun uploadImage(uri: Uri, onSuccess: (String?) -> Unit) {
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

    private fun makeFilePath(path: String, userId: String, uri: Uri): String {
        val mimeType = contentResolver.getType(uri) ?: "/none" // MIME 타입 ex) images/jpeg
        val ext = mimeType.split("/")[1] // 확장자 ex) jpeg
        val timeSuffix = System.currentTimeMillis() // 시간값 ex) 1235421532
        return "${path}/${userId}_${timeSuffix}.${ext}"
    }

    // 프로그래스 바
    private fun init() {
        showProgress(false)
    }

    private fun showProgress(isShow: Boolean) {
        if (isShow) binding.progressBar.visibility = View.VISIBLE
        else binding.progressBar.visibility = View.GONE
    }

    private fun goneData() {
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
                    val userInput = addPageTvTitle.text.toString()
                    val inputLength = userInput.length
                    addPageTvTitleLimit.text = "$inputLength / 30"

                    // 30글자 이상 입력 방지
                    if (inputLength > 30) {
                        addPageTvTitle.error = "30글자를 초과하셨습니다."
                    } else {
                        // 30글자 이하이면 경고 메시지 제거하기
                        addPageTvTitle.error = null
                    }
                }

                // 작성 후
                override fun afterTextChanged(s: Editable?) {
                    var userInput = addPageTvTitle.text.toString()
                    val inputLength = userInput.length
                    addPageTvTitleLimit.text = "$inputLength / 30"
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
                    var userInput = addPageEtText.text.toString()
                    val inputLength = userInput.length
                    addPageTvTextLimit.text = "$inputLength / 5000"

                    // 5000글자 이상 입력 방지
                    if (inputLength > 5000) {
                        addPageTvTitle.error = "5000글자를 초과하셨습니다."
                    } else {
                        // 5000글자 이하이면 경고 메시지 제거하기
                        addPageTvTitle.error = null
                    }
                }

                // 작성 후
                override fun afterTextChanged(s: Editable?) {
                    var userInput = addPageEtText.text.toString()
                    val inputLength = userInput.length
                    addPageTvTextLimit.text = "$inputLength / 5000"
                }
            })
        }
    }

    // 날짜 데이터 표시
    private fun getData(): String {
        val currentDateTime = Calendar.getInstance().time
        return SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.KOREA).format(currentDateTime)
    }

    // Firebase에서 닉네임 가져오기
    private fun setNickname(onNickNameFetched: (String) -> Unit) {
        val uid = Firebase.auth.currentUser?.uid ?: ""
        FirebaseDatabase.getInstance().reference.child("users").child(uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val userData =
                            snapshot.getValue(com.sandamso.sansaninfo.Data.UserData::class.java)
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

    // 날짜 다이얼로그
    private fun dateDialogue() {

        // 마감 일자 등록하기
        // 현재 날짜를 currentDate로 설정
        val currentDate = Calendar.getInstance()
        currentDate.time = Date()

        //minDate를 currentDate 이전으로 설정
        val minDate = Calendar.getInstance()
        minDate.time = currentDate.time
        minDate.add(Calendar.DAY_OF_MONTH, 0) // 현재 날짜 이전으로 설정

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val listener = DatePickerDialog.OnDateSetListener { datePicker, i, i2, i3 ->
            binding.addPageTvDday.text = "${i}년 ${i2 + 1}월 ${i3}일"
        }

        var picker = DatePickerDialog(this, listener, year, month, day)
        // minDate를 설정하여 현재 날짜 이전의 날짜를 선택할 수 없음.
        picker.datePicker.minDate = minDate.timeInMillis
        picker.show()
    }

    private fun toastMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    // 수정하기
    private fun editData() {
        val id = intent.getStringExtra("dataFromAddPageId")
        if (id != null) {
            firebaseDatabase.child("POST").child(id).get().addOnCompleteListener {
                if (it.isSuccessful) {
                    val userData =
                        it.result.getValue(PostModel::class.java) ?: return@addOnCompleteListener

                    with(binding) {
                        addPageTvTitle.setText(userData.title)
                        addPageTvDday.text = userData.deadlinedate
                        addPageEtText.setText(userData.maintext)
                        addPageEtMnt.setText(userData.mountain)

                        val storage = FirebaseStorage.getInstance()
                        val reference = storage.reference.child("images/${userData.image}")
                        reference.downloadUrl.addOnSuccessListener {
                            binding.addPageIvAdd.load(it)
                            Log.d("Image Tag222", "AddPage image : $it")
                        }.addOnFailureListener {
                            Log.d("Image Tag222", "AddPage image : $it")
                        }
                    }
                }
            }
        }

        binding.completeButton.setOnClickListener {
            saveEditeData()
        }

        binding.cancelButton.setOnClickListener {
            exitEditMode()
        }
    }

    private fun saveEditeData() {
        val id = intent.getStringExtra("dataFromAddPageId")
        if (id != null) {
            firebaseDatabase.child("POST").child(id).get().addOnCompleteListener {
                if (it.isSuccessful) {
                    val originalData = it.result.getValue(PostModel::class.java)
                    if (originalData != null) {
                        with(binding) {
                            // 수정된 데이터로 업데이트 하기
                            val editData = originalData.copy(
                                title = addPageTvTitle.text.toString(),
                                maintext = addPageEtText.text.toString(),
                                deadlinedate = addPageTvDday.text.toString(),
                                mountain = addPageEtMnt.text.toString(),
                            )
                            val intent =
                                Intent(this@AddPageActivity, DetailPageActivity::class.java)
                            intent.putExtra("dataFromAddPageTitle", addPageTvTitle.text.toString())
                            intent.putExtra(
                                "dataFromAddPageMaintext",
                                addPageEtText.text.toString()
                            )
                            intent.putExtra("dataFromAddPagedday", addPageTvDday.text.toString())
                            intent.putExtra("dataFromAddPagekakao", addPageEtMnt.text.toString())
                            intent.putExtra("dataFromAddPageWriter", Firebase.auth.currentUser?.uid)
                            intent.putExtra("dataFromAddPageId", id)
                            // 이미지 수정 시, 업로드하고 URL 업데이트 하기
                            CompletableFuture.supplyAsync(Supplier {
                                imageSetData(editData)
                            }).thenAccept { uploadedImageUrl ->
                            }
                            showProgress(true)
                            Toast.makeText(this@AddPageActivity, "게시글 수정중..", Toast.LENGTH_SHORT)
                                .show()
                            thread(start = true) {
                                Thread.sleep(2500)
                                runOnUiThread {
                                    startActivity(intent)
                                    finish()
                                    showProgress(false)
                                }
                            }
                            firebaseDatabase.child("POST").child(id).setValue(editData)
                        }
                    }
                }
            }
        }
    }

    private fun imageSetData(editData: PostModel) {
        val id = intent.getStringExtra("dataFromAddPageId")
        val imageUri = Uri.parse(binding.addPageIvAdd.tag.toString())
        val userChangedImage = !(imageUri == null || imageUri.toString().isEmpty())
        if (userChangedImage) {
            // 이미지를 변경한 경우
            uploadImage(imageUri) { uploadedImageUrl ->
                if (uploadedImageUrl != null) {
                    // 업로드된 이미지의 URL을 editData에 설정
                    editData.image = uploadedImageUrl
                    if (id != null) {
                        // Firebase에 업데이트
                        firebaseDatabase.child("POST").child(id).setValue(editData)
                    }
                }
            }
        } else {
            // 이미지를 변경하지 않은 경우
            if (id != null) {
                // Firebase에 업데이트
                firebaseDatabase.child("POST").child(id).setValue(editData)
                // DetailPageActivity 시작
                val intent = Intent()
                intent.putExtra("dataFromAddPageimage", editData.image)
                setResult(Activity.RESULT_OK, intent)
            }
        }
    }

    private fun exitEditMode() {
        binding.cancelButton.setOnClickListener {
            var builder = AlertDialog.Builder(this)
            builder.setTitle("수정 취소")
            builder.setMessage("게시글 수정을 취소하고 이전 내용을 유지하시겠습니까?")
            builder.setIcon(R.drawable.alert_triangle)

            val listener = DialogInterface.OnClickListener { dialog, p1 ->
                when (p1) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        finish()
                    }

                    DialogInterface.BUTTON_NEGATIVE -> {
                    }
                }
            }
            builder.setPositiveButton("확인", listener)
            builder.setNegativeButton("취소", listener)
            builder.show()
        }
    }
}


