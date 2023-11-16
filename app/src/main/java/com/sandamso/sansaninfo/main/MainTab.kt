package com.sandamso.sansaninfo.main

import androidx.fragment.app.Fragment
import com.sandamso.sansaninfo.communitypage.CommunityPageFragment
import com.sandamso.sansaninfo.searchpage.SearchPageMountainFragment

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