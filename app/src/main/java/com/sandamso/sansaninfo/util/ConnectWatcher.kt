package com.sandamso.spartube.util

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.Network
import androidx.lifecycle.LiveData

// 실시간 인터넷 감지를 위한 클래스
class ConnectWatcher(
    context: Context
) : LiveData<Boolean>() {
    private val networkCallback by lazy {
        object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                postValue(true)
            }

            override fun onLost(network: Network) {
                postValue(false)
            }
        }
    }
    private val connectivityManager =
        context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

    init {
        val activeNetwork = connectivityManager.activeNetworkInfo
        postValue(activeNetwork != null && activeNetwork.isConnectedOrConnecting)
    } // 인터넷 연결이 없는채로 앱을 실행했을때 작동하지않았던 문제 해결

    override fun onActive() {
        connectivityManager.registerDefaultNetworkCallback(networkCallback)
    }

    override fun onInactive() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}