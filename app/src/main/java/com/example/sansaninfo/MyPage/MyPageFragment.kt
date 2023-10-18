package com.example.sansaninfo.MyPage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.sansaninfo.Data.UserData
import com.example.sansaninfo.SignPage.SignInActivity
import com.example.sansaninfo.databinding.FragmentMyPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class MyPageFragment : Fragment() {

    private lateinit var binding: FragmentMyPageBinding
    private var _binding: FragmentMyPageBinding? = null
    private lateinit var mAuth: FirebaseAuth
    val userData: ArrayList<UserData> = ArrayList()
    private val mDatabase = FirebaseDatabase.getInstance().getReference("users")
    val database = Firebase.database
    val myRef = mDatabase.key
//    val myRef = database.getReference("${uid}")
    var changeNickname = ""

    companion object {
        fun newInstance() = MyPageFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding = FragmentMyPageBinding.inflate(inflater, container, false)
        binding.myPageEtNickname
        //FirebaseAuth 연결
        mAuth = FirebaseAuth.getInstance()

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
//            val nicknameText = myPage
//            readNick()
//            writeNick()
        }
        binding.myPageEtNickname.setText("${myRef}")

        return binding.root
    }

//    private fun readNick() {
//        //읽기
//        myRef.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val test = snapshot.child("nickname")
//                for (ds in test.children)
//                    Log.d("martin", ds.toString())
//            }
//
//            //읽기 예외처리
//            override fun onCancelled(error: DatabaseError) {
//                Log.d("martin", "Failed", error.toException())
//            }
//        })
//    }
//
//    //쓰기
//    private fun writeNick() {
//        changeNickname = myRef.setValue("Hello, World").toString()
//
//    }

    //로그아웃
    private fun signOut() {
        FirebaseAuth.getInstance().signOut()
    }

    //회원탈퇴
    private fun revokeAccess() {
        mAuth.currentUser?.delete()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val profileUpdate = UserProfileChangeRequest.Builder()
//            .setDisplayName("${nickNameText.text}")
//            .build()
//
//        user?.updateProfile(profileUpdate)
//            ?.addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    Toast.makeText(requireContext(), "닉네임을 지정했습니다.", Toast.LENGTH_SHORT).show()
//                    user?.reload()
//                }
//            }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}