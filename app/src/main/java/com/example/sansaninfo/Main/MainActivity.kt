package com.example.sansaninfo.Main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.sansaninfo.databinding.ActivityMainBinding
import com.example.spartube.main.MainViewModel
import com.example.spartube.util.ConnectWatcher

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewPagerAdapter by lazy {
        MainViewPagerAdapter(this@MainActivity)
    }
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
        initViewModel()

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

    // 인터넷 미연결시 예외처리
    private fun initViewModel() = with(binding) {
        ConnectWatcher(this@MainActivity).observe(this@MainActivity) { connection ->
            mainViewModel.setNetworkStatus(connection)
        }
        mainViewModel.networkStatus.observe(this@MainActivity) { isAvailable ->
            mainActivityNetworkMessage.isVisible = !isAvailable
            mainActivityNetworkProgressbar.isVisible = !isAvailable
            activityMainViewpager.isVisible = isAvailable
            mainActivityBottomBar.isEnabled = isAvailable
        }
    }
}