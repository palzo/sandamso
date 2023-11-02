package com.example.sansaninfo.InfoPage


import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.sansaninfo.API.ModelData.Item
import com.example.sansaninfo.API.ModelData.Weather
import com.example.sansaninfo.API.Retrofit.WeatherClient
import com.example.sansaninfo.BuildConfig
import com.example.sansaninfo.Main.MainActivity
import com.example.sansaninfo.R
import com.example.sansaninfo.SearchPage.MntModel
import com.example.sansaninfo.databinding.ActivityInfoPageBinding
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date

class InfoPage : AppCompatActivity(), OnMapReadyCallback {

    private val binding by lazy { ActivityInfoPageBinding.inflate(layoutInflater) }

    private var latitude = 0.0
    private var longitude = 0.0
    private var mountainAddress: String? = null
    private var mountainHeight: String? = null
    private lateinit var mapView: MapView // 네이버 지도
    private lateinit var naverMap: NaverMap
    private val LOCATION_PERMISSION_REQUEST_CODE: Int = 1000

    val currentDate = Date()

    @SuppressLint("SimpleDateFormat")
    val dateFormat = SimpleDateFormat("yyyyMMdd")
    val today = dateFormat.format(currentDate)
    private val infoPageAdapter by lazy {
        InfoPageAdapter()
    }

    //private val handler = Handler(Looper.getMainLooper())
    private val itemList: List<Item> = mutableListOf()
    private val weatherDataList = mutableListOf<WeatherData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.infoPageBtnBackArrow.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // 맵 위젯 연결
        val locationPermission = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { result ->
            if (result.all { it.value }) {
                initializeNaverMap()//네이버맵초기화
            } else {//문제시 예외처리
                Toast.makeText(this, "권한 승인이 필요합니다.", Toast.LENGTH_SHORT).show()
            }
        }

        //맵 권한 요청
        locationPermission.launch(
            arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
        initializeNaverMap()
        initView()
    }

    private fun initializeNaverMap() {
        mapView = findViewById(R.id.info_page_iv_map) as MapView
//        mapView.getMapAsync(this)
    }


    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onMapReady(map: NaverMap) {
        naverMap = map
        naverMap.maxZoom = 18.0
        naverMap.minZoom = 10.0

        // 지도 옵션 설정
        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_TRANSIT, true)
        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_BUILDING, true)
        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_TRAFFIC, true)
        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_TRANSIT, true)
        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_TRANSIT, true)

        // 초기 위치 설정
        val cameraUpdate =
            CameraUpdate.scrollTo(com.naver.maps.geometry.LatLng(37.4979921, 127.028046))
                .animate(CameraAnimation.Easing)
        naverMap.moveCamera(cameraUpdate)

        // 현재 위치 설정
        val uiSettings = naverMap.uiSettings
        uiSettings.isLocationButtonEnabled = false

        // 사용자 위치 추적 모드 설정
        naverMap.locationTrackingMode = LocationTrackingMode.Face

        // 현재 위치 설정
        val locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
        naverMap.locationSource = locationSource

        // Marker 추가
        val marker = Marker()
        marker.position = com.naver.maps.geometry.LatLng(37.521492, 127.259870)
        marker.map = naverMap
        marker.icon = MarkerIcons.BLACK
        marker.iconTintColor = Color.RED
    }


    private fun initView() = with(binding) {
        // Intent에서 Bundle을 가져옴
        val receivedBundle = intent.extras

        receivedBundle?.let {
            val receivedList: MntModel? = it.getParcelable("mntList")

            receivedList?.let {
                mountainAddress = it.mntAddress
                mountainHeight = it.mntHgt
                convertAddressToLatLng(it.mntAddress)
                binding.infoPageTvMountainName.text = it.mntName
                binding.infoPageTvMountainAddress.text = it.mntAddress
                binding.infoPageTvMountainHeight.text = "해발고도 : " + it.mntHgt + "m"
                if (removeSpecialCharacters(it.mntMainInfo) != "") {
                    binding.infoPageTvMountainIntro.text = removeSpecialCharacters(it.mntMainInfo)
                    Log.d("test", "mntMainInfo : ${removeSpecialCharacters(it.mntMainInfo)}")
                } else if (removeSpecialCharacters(it.mntSubInfo) != "") {
                    binding.infoPageTvMountainIntro.text = removeSpecialCharacters(it.mntSubInfo)
                    Log.d("test", "mntSubInfo : ${removeSpecialCharacters(it.mntSubInfo)}")
                } else {
                    binding.infoPageTvMountainIntro.text = removeSpecialCharacters(it.mntLastInfo)
                    Log.d("test", "mntLastInfo : ${removeSpecialCharacters(it.mntLastInfo)}")
                }
                if (it.mntImgURL == "") {
                    it.mntImgCode?.let { it1 -> binding.infoPageIvMountain.setImageResource(it1) }
                } else {
                    val img = "https://www.forest.go.kr/images/data/down/mountain/" + it.mntImgURL
                    binding.infoPageIvMountain.load(img) {
                        size(100, 100)
                    }
                }
            }
        }

        binding.infoPageBtnBackArrow.setOnClickListener {
            finish()
        }
        binding.infoPageRvWeather.adapter = infoPageAdapter
        binding.infoPageRvWeather.layoutManager =
            LinearLayoutManager(this@InfoPage, LinearLayoutManager.HORIZONTAL, false)

        /**
         * 아이템 추가 참고용
         */
//        val testList = mutableListOf<WeatherData>()
//        for(i in 0..6){
//            testList.add(
//                WeatherData(
//                baseTime = "base: $i",
//                tmp = "11"
//            )
//            )
//        }
//        infoPageAdapter.addItem(testList)
        /**
         * 날씨 API
         */
        val baseTimes = listOf("0200", "0500", "0800", "1100", "1400", "1700", "2000", "2300")
        for (baseTime in baseTimes) {
            CoroutineScope(Dispatchers.IO).launch {
                WeatherClient.weatherNetwork.getWeatherInfo(
                    serviceKey = BuildConfig.WEATHER_API_KEY,
                    pageNo = 1,
                    numOfRows = 10,
                    dataType = "JSON",
                    baseDate = today.toInt(),
                    baseTime = baseTime,
                    nx = 21,
                    ny = 132,
                ).enqueue(object : Callback<Weather?> {
                    override fun onResponse(call: Call<Weather?>, response: Response<Weather?>) {
                        response.body().let {
                            it?.response?.body?.items?.item?.forEach { item ->
                                if (item.category == "TMP") {
                                    weatherDataList.add(
                                        WeatherData(
                                            baseTime = "${item.fcstValue}",
                                            tmp = "11"
                                        )
                                    )
                                }
                                Log.d(
                                    "text",
                                    "baseDate : ${item.baseDate}, baseTime : ${item.baseTime}, category : ${item.category}"
                                )
                                Log.d(
                                    "text",
                                    "fxstDate : ${item.fcstTime}, fxstDate : ${item.fcstDate}, fxstValue : ${item.fcstValue}"
                                )
                                Log.d("text", "nx : ${item.nx}, ny : ${item.ny}")

                            }
                        }
                        runOnUiThread {
                            infoPageAdapter.addItem(weatherDataList)
                        }
                    }

                    override fun onFailure(call: Call<Weather?>, t: Throwable) {
                        Log.e("error", "${t.message}")
                    }
                })
            }
        }
    }

    private fun removeSpecialCharacters(inputText: String): String {
        var result = inputText
        result = result.replace("&amp;", "")
        result = result.replace("amp;", "")
        result = result.replace("nbsp;", "")
        result = result.replace("lt;br / gt;", "")
        result = result.replace("br", "")
        result = result.replace("&lt;p&gt;", "")
        result = result.replace("&lt;BR&gt;", "")
        result = result.replace("&lt;&gt;", "")
        result = result.replace("&lt;/p&gt;", "")
        result = result.replace("&quot;", "")
        result = result.replace("lt;", "")
        return result
    }


    private fun convertAddressToLatLng(address: String) {
        val geocoder = Geocoder(this)
        try {
            val addresses = geocoder.getFromLocationName(address, 1)
            if (addresses != null) {
                if (addresses.isNotEmpty()) {
                    val location = addresses[0]
                    latitude = location.latitude
                    longitude = location.longitude
                    // 추가 작업: 위치를 활용한 특정 기능 수행
                } else {
                    Toast.makeText(this, "주소를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            Log.e("주소 변환 오류", e.message.toString())
        }
    }
}

