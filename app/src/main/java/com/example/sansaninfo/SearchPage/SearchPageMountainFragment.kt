package com.example.sansaninfo.SearchPage

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.sansaninfo.BuildConfig
import com.example.sansaninfo.MountainInfoData.ApiClient
import com.example.sansaninfo.databinding.FragmentSearchPageMountainBinding
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.http.Query

class SearchPageMountainFragment : Fragment() {

    private lateinit var binding: FragmentSearchPageMountainBinding

    companion object {
        fun newInstance() = SearchPageMountainFragment()
    }

    private val SearchPageAdapter by lazy {
        SearchPageAdapter()
    }

    private val mntList = arrayListOf<BindingModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSearchPageMountainBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchPageRecyclerview.layoutManager = GridLayoutManager(context, 2)
        binding.searchPageRecyclerview.adapter = SearchPageAdapter


        val apiKey =
            "4bpUeSQaXnUDSalDsumQ5dkxA+bJXWN4dhwsYexJp6wAJnadjR+UoIVo1Dhac/spEq1HRVngbbHuY8QLzUwVBg=="

        binding.searchPageIvSearch.setOnClickListener {
            val mntName = binding.searchPageEtSearchText.text.trim().toString()
            Toast.makeText(requireContext(), "검색 클릭", Toast.LENGTH_SHORT).show()

            CoroutineScope(Dispatchers.IO).launch {
                try {

                    Log.d("test", "setOnClick")

                    val responseData = ApiClient.mntNetWork.getMountainInfo(
                        mntName = mntName,
                        key = apiKey
                    )

                    Log.d("test", "responseData")



                    requireActivity().runOnUiThread {

                    }


                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.d("test", "message : ${e.message}")
                }
            }
        }
    }
}

data class BindingModel(
    @SerializedName("mntnnm")  // 산 이름
    val mntName: String,
)