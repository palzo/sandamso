package com.example.sansaninfo.SearchPage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.sansaninfo.MountainInfoData.ApiClient
import com.example.sansaninfo.databinding.FragmentSearchPageMountainBinding
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback


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

//            GlobalScope.launch(Dispatchers.Main){
//                val dataList = withContext(Dispatchers.IO){
//                    try{
//                        ApiClient.mntNetWork.getMountainInfo(mntName)
//                    }catch (e: Exception) {
//                        // 예외 처리
//                        e.printStackTrace()
//                        null
//                    }
//                }
//                // 메인 스레드에서 API 응답 처리
//                handleApiResponse(dataList)
//            }

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    Log.d("test", "setOnClick")

                    val responseDataList = ApiClient.mntNetwork.getMountainInfo(
                        mntName = mntName,
                        key = apiKey
                    )

                    responseDataList.body.let {
                        it.items.forEach { item ->
                            mntList.add(
                                BindingModel(
                                    mntName = item.mntName
                                )
                            )
                        }
                    }

                    Log.d("test", "$responseDataList")

                    if (isAdded) {
                        requireActivity().runOnUiThread {
                            SearchPageAdapter.addItems(mntList)
                        }
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.d("test", "message : ${e.message}")
                }
            }
        }
    }

//    private fun handleApiResponse(response: Response?) {
//        if(response != null){
//            val item = response.body.items
//            Log.d("test", "검색한 아이템 : $item")
//        }
//    }

}

data class BindingModel(
    @SerializedName("mntnnm")  // 산 이름
    val mntName: String,
)