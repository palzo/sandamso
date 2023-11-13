package com.sandamso.sansaninfo.MyPage

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
import com.sandamso.sansaninfo.Data.UserData
import com.sandamso.sansaninfo.SignPage.FindpwActivity
import com.sandamso.sansaninfo.SignPage.SignInActivity
import com.sandamso.sansaninfo.databinding.FragmentMyPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kakao.sdk.user.UserApiClient
import com.sandamso.sansaninfo.R


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

        // 카카오
        UserApiClient.instance.logout { error ->
            if (error != null) {
//                Toast.makeText(requireContext(), "로그아웃 실패 $error", Toast.LENGTH_SHORT).show()
            } else {
//                Toast.makeText(requireContext(), "로그아웃 성공", Toast.LENGTH_SHORT).show()
            }
        }

        // 자동로그인 설정되어있는 경우 해제
        val autoLogin = activity?.getSharedPreferences("prefLogin", Context.MODE_PRIVATE)
        val saveEmail = activity?.getSharedPreferences("prefEmail", Context.MODE_PRIVATE)
        autoLogin?.edit()?.apply {
            putString("login", "0")
            apply()
        }
        saveEmail?.edit()?.apply {
            putString("pw", "0")
            apply()
        }
    }

    //회원탈퇴
    private fun revokeAccess() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("회원 탈퇴")
        alertDialogBuilder.setMessage("정말 회원을 탈퇴하시겠습니까?")

        alertDialogBuilder.setPositiveButton("확인") { _, _ ->
            auth.currentUser?.delete()

            // 카카오
            UserApiClient.instance.unlink { error ->
                if (error != null) {
//                Toast.makeText(requireContext(), "회원 탈퇴 실패 $error", Toast.LENGTH_SHORT).show()
                } else {
//                Toast.makeText(requireContext(), "회원 탈퇴 성공", Toast.LENGTH_SHORT).show()
                }
            }

            val autoLogin = activity?.getSharedPreferences("prefLogin", Context.MODE_PRIVATE)
            val saveEmail = activity?.getSharedPreferences("prefEmail", Context.MODE_PRIVATE)
            autoLogin?.edit()?.apply {
                putString("login", "0")
                putString("pw", "")
                apply()
            }
            saveEmail?.edit()?.apply {
                putString("check", "0")
                putString("email", "")
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