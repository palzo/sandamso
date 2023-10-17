package com.example.sansaninfo.SearchPage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sansaninfo.R

class SearchPageMountainFragment : Fragment() {

    companion object{
        fun newInstance() = SearchPageMountainFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_page_mountain, container, false)
    }

}