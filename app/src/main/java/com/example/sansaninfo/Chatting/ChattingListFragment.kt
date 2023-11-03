package com.example.sansaninfo.Chatting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sansaninfo.R
import com.example.sansaninfo.databinding.FragmentChattingListBinding

class ChattingListFragment : Fragment() {

    private val chattingListAdapter by lazy {
        ChattingListAdapter()
    }

    private var _binding: FragmentChattingListBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = ChattingListFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentChattingListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}