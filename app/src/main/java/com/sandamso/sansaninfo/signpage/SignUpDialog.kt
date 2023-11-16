package com.sandamso.sansaninfo.signpage

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.sandamso.sansaninfo.databinding.SignupDialogBinding

class SignUpDialog(context : Context) : Dialog(context) {
    private val binding by lazy { SignupDialogBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.signupDialogBtn.setOnClickListener {
            val intent = Intent(context, SignInActivity::class.java)
            context.startActivity(intent)
            dismiss()
        }
    }
}