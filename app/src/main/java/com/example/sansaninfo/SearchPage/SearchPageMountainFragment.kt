package com.example.sansaninfo.SearchPage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.parcel.Parcelize
import androidx.recyclerview.widget.GridLayoutManager
import com.example.sansaninfo.InfoPage.InfoPage
import com.example.sansaninfo.MountainInfoData.ApiClient
import com.example.sansaninfo.MountainInfoData.XmlResponse
import com.example.sansaninfo.R
import com.example.sansaninfo.databinding.FragmentSearchPageMountainBinding
import retrofit2.Call
import retrofit2.Callback


class SearchPageMountainFragment : Fragment() {

    private var _binding: FragmentSearchPageMountainBinding? = null
    private val binding get() = _binding!!

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

        _binding = FragmentSearchPageMountainBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchPageRecyclerview.layoutManager = GridLayoutManager(context, 2)
        binding.searchPageRecyclerview.adapter = searchPageAdapter

        // 산 이름으로 검색 시
        binding.searchPageIvSearch.setOnClickListener {
            val imm =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.searchPageEtSearchText.windowToken, 0)

            getMntInfo(0,binding.searchPageEtSearchText.text.trim().toString())
        }

        // 지역명으로 검색 시
        binding.searchPageSpinnerSido.setOnSpinnerItemSelectedListener<String> { _, _, _, sido ->

            getMntInfo(1, sido)
        }

        // 산으로 검색 버튼 클릭 시 UI 초기화
        binding.searchPageBtMountain.setOnClickListener {
            searchSwitch(1)
        }
        // 지역으로 검색 버튼 클릭 시 UI 초기화
        binding.searchPageBtRegion.setOnClickListener {
            searchSwitch(2)
        }

        // recyclerview 초기화 - adapter
        binding.searchPageRecyclerview.apply {
            adapter = searchPageAdapter
            clickMntItem()
        }
    }

    private fun getMntInfo(position: Int, inputValue: String) {

        val apiKey =
            "4bpUeSQaXnUDSalDsumQ5dkxA+bJXWN4dhwsYexJp6wAJnadjR+UoIVo1Dhac/spEq1HRVngbbHuY8QLzUwVBg=="

        var mntName: String = ""
        var mntRegion: String = ""
        if (position == 0) {
            mntName = inputValue
        } else {
            mntRegion = inputValue
        }

        ApiClient.mntNetwork.getMountainInfo(
            mntName = mntName,
            mntRegion = mntRegion,
            key = apiKey
        )?.enqueue(object : Callback<XmlResponse?> {
            override fun onResponse(
                call: Call<XmlResponse?>,
                response: retrofit2.Response<XmlResponse?>
            ) {
                mntList.clear()
                response.body().let { XmlResponse ->
                    XmlResponse?.body?.items?.item?.forEach { item ->
                        mntList.add(
                            MntModel(
                                mntName = item.mntName,
                                mntHgt = item.mntHeight,
                                mntMainInfo = item.mntInfo,
                                mntSubInfo = item.mntSubInfo,
                                mntAddress = item.mntAddress,
                                mntCode = MountainMapping.getMountainCode(
                                    item.mntName.trim().toString()
                                )
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

    private fun clickMntItem() {
        searchPageAdapter.setOnClickListener(object : SearchPageAdapter.ItemClick {
            override fun onClick(view: View, position: Int, model: MntModel) {
                Toast.makeText(context, "Item Position: $position", Toast.LENGTH_SHORT).show()

                navigateToInfoPage()
            }
        })
    }

    private fun navigateToInfoPage() {
        val intent = Intent(activity, InfoPage::class.java)
        // InfoPage로 이동 시 Bundle을 함께 전달
        val bundle = Bundle()
        bundle.putParcelableArrayList("mntList", mntList)
        intent.putExtras(bundle)
        startActivity(intent)
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

        } else if (position == 2 && searchPageSpinnerSido.visibility == View.INVISIBLE) {

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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

@Parcelize
data class MntModel(
    val mntName: String,
    val mntHgt: String,
    val mntAddress: String,
    val mntMainInfo: String,
    val mntSubInfo: String,
    val mntCode: String?
) : Parcelable

