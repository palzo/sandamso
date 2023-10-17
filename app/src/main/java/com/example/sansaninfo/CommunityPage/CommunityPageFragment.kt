package com.example.sansaninfo.CommunityPage

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sansaninfo.AddPage.AddPage
import com.example.sansaninfo.InfoPage.InfoPage
import com.example.sansaninfo.R
import com.example.sansaninfo.databinding.FragmentCommunityPageBinding

class CommunityPageFragment : Fragment() {

    private lateinit var binding : FragmentCommunityPageBinding
    companion object{
        fun newInstance() = CommunityPageFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCommunityPageBinding.inflate(inflater, container, false)

        binding.communityPageFab.setOnClickListener {
//            val intent = Intent(requireContext(), InfoPage::class.java)
//            startActivity(intent)
            val intent = Intent(requireContext(), AddPage::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}