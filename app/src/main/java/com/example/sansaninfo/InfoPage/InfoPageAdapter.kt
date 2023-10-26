package com.example.sansaninfo.InfoPage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sansaninfo.R
import com.example.sansaninfo.databinding.WeatherItemBinding

class InfoPageAdapter(private val weatherData : MutableList<WeatherData>) : RecyclerView.Adapter<InfoPageAdapter.WeatherHolder>() {
    override fun getItemCount(): Int {
        return weatherData.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherHolder {
        val binding = WeatherItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherHolder(binding)
    }


    override fun onBindViewHolder(holder: WeatherHolder, position: Int) {
        val data = weatherData[position]

        holder.setItem(data)
    }

    inner class WeatherHolder(val binding : WeatherItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setItem(item : WeatherData) {
            val timeTextView = binding.weatherItemTvHour
            val tmpTextView = binding.weatherItemTvTemperature
            val image = binding.weatherItemIv

            timeTextView.text = item.baseTime
            tmpTextView.text = item.tmp
            image.setImageResource(R.drawable.ic_cloud)
        }
    }
}
