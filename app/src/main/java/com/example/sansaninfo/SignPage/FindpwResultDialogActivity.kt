package com.example.sansaninfo.SignPage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sansaninfo.R
import com.example.sansaninfo.databinding.ActivityFindpwResultDialogBinding

class FindpwResultDialogActivity : AppCompatActivity() {
    private val binding by lazy { ActivityFindpwResultDialogBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_findpw_result_dialog)
    }
}