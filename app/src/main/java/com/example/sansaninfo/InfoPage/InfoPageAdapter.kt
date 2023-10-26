package com.example.sansaninfo.InfoPage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sansaninfo.databinding.WeatherItemBinding

class InfoPageAdapter(private val weatherData : List<WeatherData>) : RecyclerView.Adapter<InfoPageAdapter.WeatherHolder>() {
    override fun getItemCount(): Int {
        return weatherData.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherHolder {
        val binding = WeatherItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherHolder(binding)
    }


    override fun onBindViewHolder(holder: WeatherHolder, position: Int) {
        val data = weatherData[position]

        holder.timeTextView.text = data.baseTime
        holder.tmpTextView.text = data.tmp
    }

    inner class WeatherHolder(val binding : WeatherItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val timeTextView = binding.weatherItemTvHour
        val tmpTextView = binding.weatherItemTvTemperature
    }
}
