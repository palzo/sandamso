package com.example.sansaninfo.MyPage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.sansaninfo.SignPage.SignInActivity
import com.example.sansaninfo.databinding.FragmentMyPageBinding
import com.google.firebase.auth.FirebaseAuth

class MyPageFragment : Fragment() {

    private lateinit var binding: FragmentMyPageBinding
    private lateinit var mContext: Context
    private lateinit var mAuth: FirebaseAuth

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
        mAuth = FirebaseAuth.getInstance()

        //로그인 페이지로 이동과 로그아웃
        binding.myPageTvLogout.setOnClickListener {
            signOut()
            Toast.makeText(mContext, "로그아웃이 완료되었습니다.", Toast.LENGTH_SHORT).show()
            val intent = Intent(activity, SignInActivity::class.java)
            startActivity(intent)
        }

        //로그인페이지로 이동과 회원탈퇴
        binding.myPageTvSecession.setOnClickListener {
            revokeAccess()
            Toast.makeText(mContext, "회원탈퇴가 완료되었습니다.", Toast.LENGTH_SHORT).show()
            val intent = Intent(activity, SignInActivity::class.java)
            startActivity(intent)
        }


//        binding.myPageTvLogout.setOnClickListener {
//            val intent = Intent(mContext, ActivitySignInBinding::class.java)
//            startActivity(intent)
////            signOut()
//            Toast.makeText(mContext, "로그아웃이 완료되었습니다.", Toast.LENGTH_SHORT).show()
//        }
//        binding.myPageTvSecession.setOnClickListener {
//            revokeAccess()
//            Toast.makeText(mContext, "회원탈퇴가 완료되었습니다.", Toast.LENGTH_SHORT).show()
//        }
        return binding.root
    }

    //로그아웃
    private fun signOut() {
        FirebaseAuth.getInstance().signOut()

    }

    //회원탈퇴
    private fun revokeAccess() {
        mAuth.currentUser?.delete()

    }
//    private fun deleted(){
//        FirebaseAuth.getInstance().currentUser!!.delete().addOnCompleteListener { task ->
//            if (task.isSuccessful){
//                Toast.makeText(mContext, "회원 탈퇴가 완료되었습니다.", Toast.LENGTH_SHORT).show()
//
//                //로그아웃처리
//                FirebaseAuth.getInstance().signOut()
//            }else{
//                Toast.makeText(mContext, task.exception.toString(),Toast.LENGTH_SHORT).show()
//            }
//        }
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        binding = null
//    }
}