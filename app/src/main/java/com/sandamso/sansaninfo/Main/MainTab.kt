package com.sandamso.sansaninfo.Main

import androidx.fragment.app.Fragment
import com.sandamso.sansaninfo.CommunityPage.CommunityPageFragment
import com.sandamso.sansaninfo.SearchPage.SearchPageMountainFragment

data class MainTab(val fragment: Fragment) {
    fun onTabSelected() {
        if(fragment is CommunityPageFragment) {
            (fragment as CommunityPageFragment).onTabSelected()
        }
        if(fragment is SearchPageMountainFragment) {
            (fragment as SearchPageMountainFragment).onTabSelected()
        }
    }
}