package com.example.sansaninfo.SignPage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sansaninfo.R
import com.example.sansaninfo.databinding.ActivityFindpwDialogBinding

class FindpwDialogActivity : AppCompatActivity() {
    private val binding by lazy { ActivityFindpwDialogBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_findpw_dialog)
    }
}