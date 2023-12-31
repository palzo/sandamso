/*
 * Copyright 2023 skydoves (Jaewoong Eum)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sandamso.sansaninfo.searchpage

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.sandamso.sansaninfo.BuildConfig
import com.sandamso.sansaninfo.infopage.InfoPage
import com.sandamso.sansaninfo.mountainimageapi.ImgResponse
import com.sandamso.sansaninfo.mountainimageapi.MntImgClient
import com.sandamso.sansaninfo.mountaininfoapi.ApiClient
import com.sandamso.sansaninfo.mountaininfoapi.XmlResponse
import com.sandamso.sansaninfo.R
import com.sandamso.sansaninfo.databinding.FragmentSearchPageMountainBinding
import kotlinx.android.parcel.Parcelize
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchPageMountainFragment : Fragment() {

    private var _binding: FragmentSearchPageMountainBinding? = null
    private val binding get() = _binding!!
    private var city = ""

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

    @SuppressLint("ClickableViewAccessibility", "NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchPageRecyclerview.layoutManager = GridLayoutManager(context, 2)
        binding.searchPageRecyclerview.adapter = searchPageAdapter

        // 산 이름으로 검색 시
        binding.searchPageIvSearch.setOnClickListener {
            val imm =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.searchPageEtSearchText.windowToken, 0)

            if(binding.searchPageEtSearchText.text.isEmpty()){
                searchFail()
            }else{
                getMntInfo(0, binding.searchPageEtSearchText.text.trim().toString())
            }

        }

        // 지역명으로 검색 시 spinner 1
        binding.searchPageSpinnerSido.setOnSpinnerItemSelectedListener<String> { _, _, _, sido ->

            getMntInfo(1, sido)
            // spinner 2 초기화 (첫 번째 spinner에 알맞은 구, 군을 가져옴)
            initSpinner(sido)

        }

        // 산으로 검색 버튼 클릭 시 UI 초기화
        binding.searchPageBtMountain.setOnClickListener {
            searchSwitch(1)
        }
        // 지역으로 검색 버튼 클릭 시 UI 초기화
        binding.searchPageBtRegion.setOnClickListener {
            searchSwitch(2)
        }

        // 지역명으로 검색 시 spinner 2
        binding.searchPageSpinnerGoo.setOnSpinnerItemSelectedListener<String> { _, _, _, goo ->

            getMntInfo(1, "$city $goo")
        }

        // recyclerview 초기화 - adapter
        binding.searchPageRecyclerview.apply {
            adapter = searchPageAdapter
            clickMntItem()
        }

        // 스피너 이외의 전체 레이아웃을 선택하면 스피너 자동 닫힘
        binding.searchPageRecyclerview.setOnTouchListener { _, event ->
            if(event.action == MotionEvent.ACTION_DOWN) {
                if(!isPointInsideView(event.rawX, event.rawY, binding.searchPageSpinnerSido)) {
                    binding.searchPageSpinnerSido.dismiss()
                }
                if(!isPointInsideView(event.rawX, event.rawY, binding.searchPageSpinnerGoo)) {
                    binding.searchPageSpinnerGoo.dismiss()
                }
            }
            false
        }

        // 변경사항 UI 적용
        searchPageAdapter.notifyDataSetChanged()
    }


    // 스피너 이외의 레이아웃 부분을 선택한 경우
    fun isPointInsideView(x : Float, y : Float, view : View) : Boolean {
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        val viewX = location[0]
        val viewY = location[1]
        return (x > viewX && x < viewX + view.width && y > viewY && viewY < viewY + view.height)
    }

    // 탭바를 선택할 때도 닫히도록 수정
    fun onTabSelected() {
        if(_binding != null) {
            if(binding.searchPageSpinnerSido.isShowing) {
                binding.searchPageSpinnerSido.dismiss()
            }
            if(binding.searchPageSpinnerGoo.isShowing) {
                binding.searchPageSpinnerGoo.dismiss()
            }
        }
    }

    private fun initSpinner(city: String) = with(binding) {
        searchPageSpinnerGoo.setItems(GooData.getRegionList(city))
        searchPageSpinnerGoo.dismiss()
    }

    // 산 이름, 산 지역을 검색할 경우 정보를 가져옴
    private fun getMntInfo(position: Int, inputValue: String) {

        var mntName = ""
        var mntRegion = ""
        if (position == 0) {
            mntName = inputValue
        } else {
            mntRegion = inputValue
        }

        ApiClient.mntNetwork.getMountainInfo(
            mntName = mntName,
            mntRegion = mntRegion,
            key = BuildConfig.WEATHER_API_KEY
        )?.enqueue(object : Callback<XmlResponse?> {
            override fun onResponse(
                call: Call<XmlResponse?>,
                response: Response<XmlResponse?>
            ) {
                mntList.clear()
                response.body().let { XmlResponse ->
                    XmlResponse?.body?.items?.item?.forEach { item ->
                        // 중복되는 산 이름 제거
                        val isAlreadyAdded = mntList.any { it.mntName == item.mntName }
                        if (!isAlreadyAdded) {
                            mntList.add(
                                MntModel(
                                    mntName = item.mntName,
                                    mntHgt = item.mntHeight,
                                    mntMainInfo = item.mntInfo,
                                    mntSubInfo = item.mntSubInfo,
                                    mntAddress = item.mntAddress,
                                    mntLastInfo = item.mntLastInfo,
                                    mntImgCode = MountainMapping.getMountainCode(
                                        item.mntName.trim()
                                    ),
                                    mntImgURL = "",
                                )
                            )
                        }
                    }
                }
                findImgURL()
                if (isAdded) {
                    requireActivity().runOnUiThread {
                        binding.searchPageIvBackpackers.visibility = View.GONE
                        binding.searchPageTvBackpackers.visibility = View.GONE
                        searchPageAdapter.addItems(mntList)
                    }
                }
            }

            override fun onFailure(call: Call<XmlResponse?>, t: Throwable) {
                Log.e("test", "getMntInfo onFailure: ${t.message}")
                searchFail()
            }
        })
    }

    private fun searchFail(){
        binding.searchPageIvBackpackers.visibility = View.VISIBLE
        binding.searchPageTvBackpackers.visibility = View.VISIBLE
        mntList.clear()
        searchPageAdapter.itemsClear()
        Toast.makeText(activity, "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show()
    }

    // 산 코드를 이용하여 이미지 URL을 받아오는 부분
    private fun findImgURL() {
        mntList.forEach { item ->
            if (item.mntImgCode != 0) {
                item.mntImgCode?.let { mntImgCode ->
                    MntImgClient.mntNetwork.getMntImgCode(

                        mntCodeNum = mntImgCode,
                        key = BuildConfig.WEATHER_API_KEY,

                        ).enqueue(object : Callback<ImgResponse?> {
                        override fun onResponse(
                            call: Call<ImgResponse?>,
                            response: Response<ImgResponse?>
                        ) {
                            response.body().let {
                                it?.body?.items?.item?.forEach {
                                    if (it.imgURL != "") {
                                        item.mntImgURL = it.imgURL
                                    }
                                }
                            }
                        }

                        override fun onFailure(call: Call<ImgResponse?>, t: Throwable) {
                            Log.e("test", "findImgURL onFailure: ${t.message}")
                        }
                    })
                }
            }
        }
    }

    private fun clickMntItem() {
        searchPageAdapter.setOnClickListener(object : SearchPageAdapter.ItemClick {
            override fun onClick(view: View, position: Int, model: MntModel) {
                navigateToInfoPage(position)
            }
        })
    }

    private fun navigateToInfoPage(position: Int) {
        val intent = Intent(activity, InfoPage::class.java)
        // InfoPage로 이동 시 Bundle을 함께 전달
        val bundle = Bundle()
        bundle.putParcelable("mntList", mntList[position])
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
            binding.searchPageSpinnerSido.dismiss()
            binding.searchPageSpinnerGoo.dismiss()
            binding.searchPageIvBackpackers.visibility = View.VISIBLE
            binding.searchPageTvBackpackers.visibility = View.VISIBLE

        } else if (position == 2 && searchPageSpinnerSido.visibility == View.INVISIBLE) {

            mntList.clear()
            searchPageAdapter.itemsClear()
            searchPageEtSearchText.visibility = View.INVISIBLE
            searchPageIvSearch.visibility = View.INVISIBLE
            searchPageSpinnerSido.visibility = View.VISIBLE
            searchPageSpinnerGoo.visibility = View.VISIBLE
            searchPageBtMountain.setBackgroundResource(btn)
            searchPageBtRegion.setBackgroundResource(clickBtn)
            binding.searchPageIvBackpackers.visibility = View.VISIBLE
            binding.searchPageTvBackpackers.visibility = View.VISIBLE

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
    val mntLastInfo: String,
    var mntImgCode: Int?,
    var mntImgURL: String
) : Parcelable
