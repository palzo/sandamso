package com.example.sansaninfo.SearchPage

import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.sansaninfo.InfoPage.InfoPage
import com.example.sansaninfo.MountainInfoData.ApiClient
import com.example.sansaninfo.MountainInfoData.XmlResponse
import com.example.sansaninfo.R
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

    private val searchPageAdapter by lazy {
        SearchPageAdapter()
    }

    private val mntList = arrayListOf<MntModel>()

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
        binding.searchPageRecyclerview.adapter = searchPageAdapter

        val apiKey =
            "4bpUeSQaXnUDSalDsumQ5dkxA+bJXWN4dhwsYexJp6wAJnadjR+UoIVo1Dhac/spEq1HRVngbbHuY8QLzUwVBg=="

        // 산 이름으로 검색 시
        binding.searchPageIvSearch.setOnClickListener {

            val mntName = binding.searchPageEtSearchText.text.trim().toString()

            ApiClient.mntNetwork.getMountainInfo(
                mntName = mntName,
                key = apiKey
            )?.enqueue(object : Callback<XmlResponse?> {
                override fun onResponse(
                    call: Call<XmlResponse?>,
                    response: retrofit2.Response<XmlResponse?>
                ) {
                    mntList.clear()
//                    Log.d("test", "$response")
//                    Log.d("test", "items body = ${response.body()}")
                    response.body().let { XmlResponse ->
                        XmlResponse?.body?.items?.item?.forEach { item ->
                            mntList.add(
                                MntModel(
                                    mntName = item.mntName,
                                    mntHgt = item.mntHeight,
                                    mntMainInfo = item.mntInfo,
                                    mntSubInfo = item.mntSubInfo,
                                    mntAddress = item.mntAddress
                                )
                            )
                        }
                    }
                    if (isAdded) {
                        requireActivity().runOnUiThread {
                            searchPageAdapter.addItems(mntList)
                        }
                    }
                }

                override fun onFailure(call: Call<XmlResponse?>, t: Throwable) {
                    Log.e("#jblee", "onFailure: ${t.message}")
                    Toast.makeText(requireContext(), "정보를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
                }
            })
        }
        // 산으로 검색 버튼 클릭 시
        binding.searchPageBtMountain.setOnClickListener {
            // 아이템 초기화 및 검색버튼 체인지
            searchSwitch(1)
        }
        // 지역으로 검색 버튼 클릭 시
        binding.searchPageBtRegion.setOnClickListener {
            // 아이템 초기화 및 검색버튼 체인지
            searchSwitch(2)
        }

        // recyclerview 초기화 - adapter
        binding.searchPageRecyclerview.apply {
            adapter = searchPageAdapter
            clickMntItem()
        }
    }

    private fun clickMntItem() {
        searchPageAdapter.setOnClickListener(object : SearchPageAdapter.ItemClick {
            override fun onClick(view: View, position: Int, model: MntModel) {
                Toast.makeText(context, "Item Position: $position", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun searchSwitch(position: Int) = with(binding) {

        val clickBtn = R.drawable.search_page_click_button_background
        val btn = R.drawable.search_page_button_background

        if (position == 1 && searchPageSpinnerSido.visibility == View.VISIBLE) {

            mntList.clear()
            searchPageAdapter.itemsClear()
            searchPageEtSearchText.visibility = View.VISIBLE
            searchPageIvSearch.visibility = View.VISIBLE
            searchPageSpinnerSido.visibility = View.INVISIBLE
            searchPageSpinnerGoo.visibility = View.INVISIBLE
            searchPageBtMountain.setBackgroundResource(clickBtn)
            searchPageBtRegion.setBackgroundResource(btn)
            searchPageEtSearchText.text.clear()

        } else if(position == 2 && searchPageSpinnerSido.visibility == View.INVISIBLE) {

            mntList.clear()
            searchPageAdapter.itemsClear()
            searchPageEtSearchText.visibility = View.INVISIBLE
            searchPageIvSearch.visibility = View.INVISIBLE
            searchPageSpinnerSido.visibility = View.VISIBLE
            searchPageSpinnerGoo.visibility = View.VISIBLE
            searchPageBtMountain.setBackgroundResource(btn)
            searchPageBtRegion.setBackgroundResource(clickBtn)

        }
    }
}
data class MntModel(
    val mntName: String,
    val mntHgt: String,
    val mntAddress: String,
    val mntMainInfo: String,
    val mntSubInfo: String
)
