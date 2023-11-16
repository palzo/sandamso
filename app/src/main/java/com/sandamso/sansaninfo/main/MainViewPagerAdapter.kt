package com.sandamso.sansaninfo.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.sandamso.sansaninfo.chattingpage.ChattingListFragment
import com.sandamso.sansaninfo.communitypage.CommunityPageFragment
import com.sandamso.sansaninfo.mypage.MyPageFragment
import com.sandamso.sansaninfo.searchpage.SearchPageMountainFragment

class MainViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    private val fragments = ArrayList<MainTab>()

    // 스피너 자동 닫힘
    fun closeSpinner() {
        for(tab in fragments) {
            tab.onTabSelected()
        }
    }

    init {
        fragments.add(
            MainTab(
                CommunityPageFragment.newInstance()
            )
        )
        fragments.add(
            MainTab(
                ChattingListFragment.newInstance()
            )
        )
        fragments.add(
            MainTab(
                SearchPageMountainFragment.newInstance()
            )
        )
        fragments.add(
            MainTab(
                MyPageFragment.newInstance()
            )
        )
    }

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position].fragment
    }
}