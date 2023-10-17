package com.example.sansaninfo.Main

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.sansaninfo.R
import com.example.sansaninfo.databinding.ActivityMainBinding
import nl.joery.animatedbottombar.AnimatedBottomBar

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewPagerAdapter by lazy {
        MainViewPagerAdapter(this@MainActivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()

    }

    private fun initView() = with(binding) {
        activityMainViewpager.adapter = viewPagerAdapter
        activityMainViewpager.isUserInputEnabled = false
        mainActivityBottomBar.setupWithViewPager2(activityMainViewpager)
        mainActivityBottomBar.indicatorMargin = 100


//        mainActivityBottomBar.onTabSelected = {
//            Log.d("bottom_bar", "Selected tab: " + it.title)
//        }
    }
}