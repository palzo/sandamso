package com.example.sansaninfo.InfoPage

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sansaninfo.R
import com.example.sansaninfo.databinding.WeatherItemBinding

class InfoPageAdapter : RecyclerView.Adapter<InfoPageAdapter.WeatherHolder>() {

    private val list = arrayListOf<WeatherData>()
    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoPageAdapter.WeatherHolder {
        val binding = WeatherItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherHolder, position: Int) {
        holder.setItem(list[position])
    }

    fun addItem(item: MutableList<WeatherData>) {
        list.clear()
        list.addAll(item)
        notifyDataSetChanged()
    }

    inner class WeatherHolder(private val binding : WeatherItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setItem(item : WeatherData) = with(binding) {
            binding.weatherItemTvHour.text = formatBaseTime(item.baseTime)
            binding.weatherItemTvTemperature.text = formatTmp(item.tmp)
            binding.weatherItemIv.setImageResource(R.drawable.ic_cloud)
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
}
