package com.sandamso.sansaninfo.mypage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.sandamso.sansaninfo.data.UserData
import com.sandamso.sansaninfo.signpage.FindpwActivity
import com.sandamso.sansaninfo.signpage.SignInActivity
import com.sandamso.sansaninfo.databinding.FragmentMyPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sandamso.sansaninfo.R
import com.sandamso.sansaninfo.TutorialActivity.TutorialActivity


class MyPageFragment : Fragment() {

    private lateinit var binding: FragmentMyPageBinding
    private var _binding: FragmentMyPageBinding? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    var firebaseDatabase = FirebaseDatabase.getInstance().reference

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
            showSignOutDialog()
        }

        //로그인페이지로 이동과 회원탈퇴
        binding.myPageTvSecession.setOnClickListener {
            revokeAccess()
        }

        //수정 버튼
        binding.myPageIvNickname.setOnClickListener {

            val intent = Intent(activity, ChangeNicknameActivity::class.java)
            startActivity(intent)
        }

        //비밀번호 변경
        binding.myPageTvChangePw.setOnClickListener {
            val intent = Intent(activity, FindpwActivity::class.java)
            startActivity(intent)
        }

        binding.myPageTvTutorial.setOnClickListener{
            val intent = Intent(activity, TutorialActivity::class.java)
            startActivity(intent)
        }

        newNickname()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        // Fragment가 다시 활성화될 때 데이터를 다시 불러오기
        newNickname()
    }

    private fun newNickname() {
        // 이름, 닉네임 띄우기
        val uid = auth.currentUser?.uid ?: ""
        firebaseDatabase.child("users").child(uid).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val userData = snapshot.getValue(UserData::class.java)
                    if (userData != null) {
                        binding.myPageTvName.text = userData.name
                        binding.myPageEtNickname.text = userData.nickname
                    }
                }
            }
        })
    }

    private fun showSignOutDialog() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("로그아웃")
        alertDialogBuilder.setMessage("정말로 로그아웃을 하시겠습니까?")

        alertDialogBuilder.setPositiveButton("확인") { _, _ ->
            signOut()
            showToast("로그아웃이 완료되었습니다.")
            val intent = Intent(activity, SignInActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
        alertDialogBuilder.setNegativeButton("취소") { dialog, _ ->
            dialog.dismiss()
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    //로그아웃
    private fun signOut() {
        FirebaseAuth.getInstance().signOut()

        // 자동로그인 설정되어있는 경우 해제
        val loginInfo = activity?.getSharedPreferences("prefLogin", Context.MODE_PRIVATE)

        loginInfo?.edit()?.apply {
            if(loginInfo.getString("loginType", "0") == "1"){
                putString("pw", "0")
            }else{
                putString("loginType", "0")
                putString("email", "0")
                putString("pw", "0")
            }
            apply()
        }
    }

    //회원탈퇴
    private fun revokeAccess() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("회원 탈퇴")
        alertDialogBuilder.setMessage("정말 회원을 탈퇴하시겠습니까?")

        alertDialogBuilder.setPositiveButton("확인") { _, _ ->
            // realtimedatabase에서 제거
            val user = auth.currentUser
            val dataBase = FirebaseDatabase.getInstance()
            val userReference = dataBase.getReference("users")
            if(user != null){
                userReference.child(user.uid).removeValue()
            }
            // authentication에서 제거
            auth.currentUser?.delete()

            val loginInfo = activity?.getSharedPreferences("prefLogin", Context.MODE_PRIVATE)

            loginInfo?.edit()?.apply {
                putString("loginType", "0")
                putString("login", "0")
                putString("pw", "0")
                apply()
            }
            val intent = Intent(activity, SignInActivity::class.java)
            startActivity(intent)
            activity?.finish()
            showToast("회원탈퇴가 완료되었습니다.")
        }
        // 취소 버튼을 눌렀을 때
        alertDialogBuilder.setNegativeButton("취소") { dialog, _ ->
            // 다이얼로그 닫기
            dialog.dismiss()
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun showToast(message: String) {
        val inflater = layoutInflater
        val layout = inflater.inflate(R.layout.toast_msg, requireActivity().findViewById(R.id.toast_layout))
        val text = layout.findViewById<TextView>(R.id.tm_text)
        text.text = message
        with(Toast(requireContext())) {
            duration = Toast.LENGTH_SHORT
            view = layout
            show()
        }
    }
}