package com.example.sansaninfo.MyPage

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.sansaninfo.Data.UserData
import com.example.sansaninfo.SignPage.SignInActivity
import com.example.sansaninfo.databinding.FragmentMyPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase


class MyPageFragment : Fragment() {

    private lateinit var binding: FragmentMyPageBinding
    private var _binding: FragmentMyPageBinding? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    //    fun initializeDbRef() {
//        // [START initialize_database_ref]
//        database = Firebase.database.reference
//        // [END initialize_database_ref]
//    }
    fun writeNewUser(userId: String, name: String, email: String, nickname: String) {
        val user = UserData(name, email, nickname)

        database.child("users").child(userId).setValue(user)
    }
    // [END rtdb_write_new_user]

    fun writeNewUserWithTaskListeners(
        userId: String,
        name: String,
        email: String,
        nickname: String
    ) {
        val user = UserData(name, email, nickname)

        // [START rtdb_write_new_user_task]
        database.child("users").child(userId).setValue(user)
            .addOnSuccessListener {
                // Write was successful!
                // ...
            }
            .addOnFailureListener {
                // Write failed
                // ...
            }
        // [END rtdb_write_new_user_task]
    }

    //    lateinit var firebaseDatabase: FirebaseDatabase
//    lateinit var databaseReference: DatabaseReference
    var firebaseDatabase = FirebaseDatabase.getInstance().reference
//    var mDatabase = FirebaseDatabase.getInstance().getReference("users/$uid/nickname")
//    val myRef = mDatabase.key
//    val uid = mAuth.currentUser?.uid
//    val userReference = mDatabase.child(uid!!)
//    var changeNickname = ""
//    val userData: ArrayList<UserData> = ArrayList()
//    val database = Firebase.database

    companion object {
        fun newInstance() = MyPageFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding = FragmentMyPageBinding.inflate(inflater, container, false)

        //FirebaseAuth 연결
        auth = FirebaseAuth.getInstance()

        //로그인 페이지로 이동과 로그아웃
        binding.myPageTvLogout.setOnClickListener {
            signOut()
            Toast.makeText(activity, "로그아웃이 완료되었습니다.", Toast.LENGTH_SHORT).show()
            val intent = Intent(activity, SignInActivity::class.java)
            startActivity(intent)
        }

        //로그인페이지로 이동과 회원탈퇴
        binding.myPageTvSecession.setOnClickListener {
            revokeAccess()
            Toast.makeText(activity, "회원탈퇴가 완료되었습니다.", Toast.LENGTH_SHORT).show()
            val intent = Intent(activity, SignInActivity::class.java)
            startActivity(intent)
        }

        //수정 버튼
        binding.myPageIvNickname.setOnClickListener {

            val intent = Intent(activity, ChangeNicknameActivity::class.java)
            startActivity(intent)
        }
        val uid = auth.currentUser?.uid ?: ""
        firebaseDatabase.child("users").child(uid).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val userData = snapshot.getValue(UserData::class.java)
                    if (userData != null) {
                        binding.myPageEtNickname.text = userData.nickname
                    }
                }
            }
        })


        return binding.root
    }
//    private fun updateNickname(change : String) {
//        userReference.child("nickname").setValue(change)
//            .addOnCompleteListener { task ->
// 닉네임 업데이트 성공 시
//                if(task.isSuccessful) {
//                    Toast.makeText(activity, "업데이트 성공", Toast.LENGTH_SHORT).show()
//                    binding.myPageEtNickname.setText(change)
//                }
//                else {
//                    Toast.makeText(activity, "업데이트 실패", Toast.LENGTH_SHORT).show()
//                }
//            }
//    }


    //로그아웃
    private fun signOut() {
        FirebaseAuth.getInstance().signOut()
    }

    //회원탈퇴
    private fun revokeAccess() {
        auth.currentUser?.delete()
    }

    private fun getUserProfile() {
        val user = Firebase.auth.currentUser
        user?.let {
            val name = it.displayName
            val email = it.email
            val emailVerified = it.isEmailVerified
            val uid = it.uid
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}