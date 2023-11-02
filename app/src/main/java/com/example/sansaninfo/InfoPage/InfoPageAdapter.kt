package com.example.sansaninfo.InfoPage

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sansaninfo.R
import com.example.sansaninfo.databinding.WeatherItemBinding

class InfoPageAdapter : RecyclerView.Adapter<InfoPageAdapter.WeatherHolder>() {

    private val list = arrayListOf<WeatherData>()
    private val skyList = arrayListOf<SkyData>()

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoPageAdapter.WeatherHolder {
        val binding = WeatherItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherHolder, position: Int) {
        val weatherData = list[position]
        val skyData = skyList[position]

        holder.setItem(weatherData, skyData)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addItem(item: MutableList<WeatherData>, skyItem : MutableList<SkyData>) {
        list.clear()
        list.addAll(item)
        skyList.clear()
        skyList.addAll(skyItem)
        notifyDataSetChanged()
    }

    inner class WeatherHolder(private val binding : WeatherItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setItem(item : WeatherData, skyItem : SkyData) = with(binding) {
            binding.weatherItemTvHour.text = formatBaseTime(item.baseTime)
            binding.weatherItemTvTemperature.text = formatTmp(item.tmp)
            binding.weatherItemIv.setImageResource(ptyIcon(skyItem.pty, skyItem.sky))
        }
    }

    // 0200, 0500 등을 02시, 05시로 변경
    private fun formatBaseTime(baseTime : String) : String {
        val formatTime = baseTime.removeSuffix("00")
        return "${formatTime}시"
    }

    // 온도 뒤에 ºC 추가
    private fun formatTmp(tmp : String) : String {
        return "${tmp}ºC"
    }

    private fun ptyIcon(ptyNum : String, skyNum : String) : Int {
        return when (ptyNum) {
            "0" -> skyIcon(skyNum)
            // 비
            "1" -> {
                Log.d("weatherIcon", "rainy")
                return R.drawable.ic_snow
            }
            // 비/눈
            "2" -> {
                Log.d("weatherIcon", "snow & rainy")
                return R.drawable.ic_snow
            }
            // 눈
            "3" -> {
                Log.d("weatherIcon", "snow")
                return R.drawable.ic_snow
            }
            // 소나기
            "4" -> {
                Log.d("weatherIcon", "shower")
                return R.drawable.ic_snow
            }
            else -> {
                Log.d("SKY ERROR", "SKY DATA ERROR")
            }
        }
    }

    private fun skyIcon(skyNum : String) : Int {
        return when(skyNum) {
            // 맑음
            "1" -> {
                Log.d("weatherIcon", "sunny")
                return R.drawable.ic_snow
            }
            // 구름많음
            "3" -> {
                Log.d("weatherIcon", "lots of clouds")
                return R.drawable.ic_snow
            }
            // 흐림
            "4" -> {
                Log.d("weatherIcon", "cloudy")
                return R.drawable.ic_snow
            }
            else ->{
                Log.d("SKY ERROR", "SKY DATA ERROR")
            }
        }
    }
}
