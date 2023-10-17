package com.example.sansaninfo.AddPage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sansaninfo.R
import com.example.sansaninfo.databinding.ActivityMainBinding

class AddPage : AppCompatActivity() {

    private val binding by lazy{ ActivityMainBinding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


    }
}