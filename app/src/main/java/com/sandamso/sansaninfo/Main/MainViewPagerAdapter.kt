package com.sandamso.sansaninfo.Main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.sandamso.sansaninfo.Chatting.ChattingListFragment
import com.sandamso.sansaninfo.CommunityPage.CommunityPageFragment
import com.sandamso.sansaninfo.MyPage.MyPageFragment
import com.sandamso.sansaninfo.SearchPage.SearchPageMountainFragment

class MainViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    private val fragments = ArrayList<MainTab>()

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