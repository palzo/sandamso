package com.sandamso.sansaninfo.DetailPage

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
import com.sandamso.sansaninfo.AddPage.AddPageActivity
import com.sandamso.sansaninfo.ChattingPage.ChatRoomActivity
import com.sandamso.sansaninfo.Data.PostModel
import com.sandamso.sansaninfo.R
import com.sandamso.sansaninfo.databinding.ActivityDetailPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import com.sandamso.sansaninfo.Data.FBRoom
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.concurrent.thread

class DetailPageActivity : AppCompatActivity() {

    private val list = ArrayList<PostModel?>()

    private lateinit var auth: FirebaseAuth

    private val binding by lazy { ActivityDetailPageBinding.inflate(layoutInflater) }

    private var firebaseDatabase = FirebaseDatabase.getInstance().reference

    private lateinit var postId: String

    private var roomid = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        init()
        deleteData()
        editData()

        // 작성자인지 확인하는 메소드
        userCheck()

        binding.detailPageIvBack.setOnClickListener {
            finish()
        }

        binding.detailPageLlJoin.setOnClickListener {
            val intent = Intent(this@DetailPageActivity,ChatRoomActivity::class.java)
            intent.putExtra("dataFromdetailPageTitle", binding.detailPageTvTitle.text.toString())
            joinRoom()
            intent.putExtra("roomId",roomid)
            startActivity(intent)
        }
    }

    private fun userCheck() {
        with(binding){
            if (auth.currentUser?.uid == intent.getStringExtra("dataFromAddPageWriter")) {
                detailPageLlRevise.visibility = View.VISIBLE
                detailPageLlDelete.visibility = View.VISIBLE
                detailPageLlJoin.visibility = View.INVISIBLE
            } else {
                detailPageLlRevise.visibility = View.INVISIBLE
                detailPageLlDelete.visibility = View.INVISIBLE
                detailPageLlJoin.visibility = View.VISIBLE
            }
        }
    }
    override fun onResume() {
        super.onResume()
        dataView()
        calculateDday()
    }

    // 프로그래스 바
    private fun init() {
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

    private fun showProgress(isShow: Boolean) {
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
    private fun dataView() {

        postId = intent.getStringExtra("dataFromAddPageId") ?: ""

        firebaseDatabase.child("POST").child(postId).get().addOnCompleteListener {
            if (it.isSuccessful) {
                val userData =
                    it.result.getValue(PostModel::class.java)

                list.clear()
                list.add(userData)

                if (userData == null) return@addOnCompleteListener
                val titleData = userData.title
                val maintextData = userData.maintext
                val dateData = userData.date
                val nicknameData = userData.nickname
                val deadlineData = userData.deadlinedate
                val imageData = userData.image
                val mntData = userData.mountain

                binding.detailPageTvTitle.text = titleData
                binding.detailPageTvMemo.text = maintextData
                binding.detailPageTvDate.text = "작성일: ${dateData}"
                binding.detailPageTvName.text = "작성자: ${nicknameData}"
                binding.detailPageTvGather.text = "${deadlineData}까지 모집"
                binding.detailPageTvMnt.text = "<$mntData>"
                roomid = userData.roomId


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
    private fun calculateDday() {
        val dateData = intent.getStringExtra("dataFromAddPagedday")

        val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA)
        val currentDate = Date()

        if (dateData != null) {
            if(dateData.matches(Regex("\\d{4}년 \\d{2}월 \\d{2}일"))){

            } else{
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
    }

    // 수정하기
    private fun editData() {
        binding.detailPageLlRevise.setOnClickListener {
            val intent = Intent(this@DetailPageActivity, AddPageActivity::class.java)
            intent.putExtra("dataFromAddPageId", postId)
            intent.putExtra("switch", "edit")
            Log.d("id test", "id = $postId")
            startActivity(intent)
            finish()
        }
    }

    // 삭제하기 - 다이얼로그 구현하기
    private fun deleteData() {
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
        Log.d("detailpage", "post# deletePostData postId = $postId")
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
        Log.d("detailpage", "post# deleteImage imageData = $imageData")
        val storage = FirebaseStorage.getInstance()
        val imageReference = storage.reference.child("images/${imageData}")

        imageReference.delete().addOnSuccessListener {
            // 객체가 성공적으로 삭제됐을 때 처리
            Toast.makeText(this, "게시글이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
            finish()
        }.addOnFailureListener { exception ->
            if (exception is StorageException && exception.errorCode == StorageException.ERROR_OBJECT_NOT_FOUND) {
                // 객체가 이미 존재하지 않을 때 처리
                Toast.makeText(this, "삭제할 객체가 이미 존재하지 않습니다.", Toast.LENGTH_SHORT).show()
            } else {
                // 다른 예외처리
                Log.e("Firebase Storage", "객체 삭제 오류: ${exception.message}")
                Toast.makeText(this, "객체 삭제 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 참여하기 눌렀을 때, users 필드에 사용자 아이디 추가하기
    private fun joinRoom(){
//        val roomId = intent.getStringExtra("roomId")
        val currentUser = FirebaseAuth.getInstance().currentUser?.uid

        if (roomid != null && currentUser != null) {
            FBRoom.roomRef.child(roomid).child("users").child(currentUser).setValue(currentUser)
        }
    }
}