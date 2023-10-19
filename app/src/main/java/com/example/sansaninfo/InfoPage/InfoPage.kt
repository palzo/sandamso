package com.example.sansaninfo.InfoPage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sansaninfo.R
import com.example.sansaninfo.SearchPage.MntModel
import com.example.sansaninfo.databinding.ActivityInfoPageBinding

class InfoPage : AppCompatActivity() {

    private val binding by lazy { ActivityInfoPageBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()

    }
    private fun initView() = with(binding) {
        // Intent에서 Bundle을 가져옴
        val receivedBundle = intent.extras

        if (receivedBundle != null && receivedBundle.containsKey("mntList")){
            val mntList = receivedBundle.getParcelableArrayList<MntModel>("mntList")

            displayMountainInfo(mntList)

        }

        binding.infoPageBtnBackArrow.setOnClickListener {
            finish()
        }
    }

    private fun displayMountainInfo(mntList: List<MntModel>?) {
        if(!mntList.isNullOrEmpty()) {
            val mntInfo = mntList[0]

            binding.infoPageTvMountainName.text = mntInfo.mntName
            binding.infoPageTvMountainAddress.text = mntInfo.mntAddress
            binding.infoPageTvMountainHeight.text = mntInfo.mntHgt + "m"
            if(mntInfo.mntMainInfo.isNotEmpty()){
                binding.infoPageTvMountainIntro.text = mntInfo.mntMainInfo
            }else{
                binding.infoPageTvMountainIntro.text = mntInfo.mntSubInfo
            }
        }
    }
}