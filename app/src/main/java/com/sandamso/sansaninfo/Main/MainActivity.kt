/*Copyright (c) 2021 Joery Droppers (https://github.com/Droppers)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package com.sandamso.sansaninfo.Main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import com.sandamso.sansaninfo.CommunityPage.CommunityPageFragment
import com.sandamso.sansaninfo.databinding.ActivityMainBinding
import com.sandamso.spartube.main.MainViewModel
import com.sandamso.spartube.util.ConnectWatcher
import nl.joery.animatedbottombar.AnimatedBottomBar

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

        mainActivityBottomBar.setOnTabSelectListener(object : AnimatedBottomBar.OnTabSelectListener {
            override fun onTabSelected(
                lastIndex: Int,
                lastTab: AnimatedBottomBar.Tab?,
                newIndex: Int,
                newTab: AnimatedBottomBar.Tab
            ) {
                viewPagerAdapter.closeSpinner()
            }
        })
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