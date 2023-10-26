package com.example.sansaninfo.InfoPage

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import coil.load
import androidx.lifecycle.lifecycleScope
import com.example.sansaninfo.API.ModelData.Item
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sansaninfo.API.ModelData.RegionLocation
import com.example.sansaninfo.API.ModelData.Root
import com.example.sansaninfo.API.Retrofit.WeatherClient
import com.example.sansaninfo.Main.MainActivity
import com.example.sansaninfo.R
import com.example.sansaninfo.SearchPage.MntModel
import com.example.sansaninfo.databinding.ActivityInfoPageBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.launch
import retrofit2.Response

class InfoPage : AppCompatActivity(), OnMapReadyCallback {

    private val binding by lazy { ActivityInfoPageBinding.inflate(layoutInflater) }
    private lateinit var mGoogleMap: GoogleMap
    lateinit var fusedLocationClient: FusedLocationProviderClient //gps를 이용해 위치 확인
    lateinit var locationCallBack: LocationCallback // 위치 값 요청에 대한 갱신 정보를 받는 변수
    lateinit var locationPermission: ActivityResultLauncher<Array<String>>
    private var userLocationSet = false // 사용자 위치를 한 번 설정했는지 여부를 추적하기 위한 변수
    private var latitude = 0.0
    private var longitude = 0.0
    private var mountainAddress: String? = null
    private var mountainHeight: String? = null

    //private val handler = Handler(Looper.getMainLooper())
    private val itemList: List<Item> = mutableListOf()
    private var weatherData = mutableListOf<WeatherData>()
    private var weatherDataList = mutableListOf<WeatherData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.infoPageRvWeather.adapter = InfoPageAdapter(weatherData)
        binding.infoPageRvWeather.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        communicateWeather()

        binding.infoPageBtnBackArrow.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        binding.infoPageTvFake.setOnClickListener {
            //검색하고싶은 위도,경도 순으로 입력하기 현재서울시청
            val uri = Uri.parse("http://www.google.com/maps?q=37.5667, 126.9784")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }

        // 맵 위젯 연결
        locationPermission = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { result ->
            if (result.all { it.value }) {
                (supportFragmentManager.findFragmentById(R.id.info_page_iv_map) as SupportMapFragment)!!.getMapAsync(
                    this
                )
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
        initView()
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
                } else if(removeSpecialCharacters(it.mntSubInfo) != "") {
                    binding.infoPageTvMountainIntro.text = removeSpecialCharacters(it.mntSubInfo)
                    Log.d("test", "mntSubInfo : ${removeSpecialCharacters(it.mntSubInfo)}")
                } else{
                    binding.infoPageTvMountainIntro.text = removeSpecialCharacters(it.mntLastInfo)
                    Log.d("test", "mntLastInfo : ${removeSpecialCharacters(it.mntLastInfo)}")
                }
                if (it.mntImgURL == "") {
                    it.mntImgCode?.let { it1 -> binding.infoPageIvMountain.setImageResource(it1) }
                } else {
                    val img = "https://www.forest.go.kr/images/data/down/mountain/" + it.mntImgURL
                    binding.infoPageIvMountain.load(img){
                        size(100, 100)
                    }
                }
            }
        }

        binding.infoPageBtnBackArrow.setOnClickListener {
            finish()
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

    private fun convertAddressToLatLng(address: String): LatLng? {
        val geocoder = Geocoder(this)
        try {
            val addresses = geocoder.getFromLocationName(address, 1)
            if (addresses != null) {
                if (addresses.isNotEmpty()) {
                    val location = addresses[0]
                    latitude = location.latitude
                    longitude = location.longitude
                    val locationLatLng = LatLng(latitude, longitude)
//                    onMapReady(locationLatLng, latitude, longitude)
                    return locationLatLng
                } else {
                    Toast.makeText(this, "주소를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            Log.e("주소 변환 오류", e.message.toString())
        }
        return null
    }

    override fun onMapReady(p0: GoogleMap) {

        val mountainLocation = LatLng(latitude, longitude)  // 파란(산위치) 마커 위치

        mGoogleMap = p0
        mGoogleMap.mapType = GoogleMap.MAP_TYPE_NORMAL

        if (mountainAddress != null) {
            val markerOptions = MarkerOptions()
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
            markerOptions.position(mountainLocation)
            markerOptions.title(mountainAddress)   // 마커 클릭시 산 이름 표시
            if (mountainHeight != null) {
                markerOptions.snippet("높이: $mountainHeight m")
            }

            mGoogleMap.addMarker(markerOptions) // 마커 추가

            val cameraPosition =
                CameraPosition.Builder().target(mountainLocation).zoom(10.0f).build()
            mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))   // 산 위치 기반의 카메라 위치 조정
            mGoogleMap.uiSettings.isZoomControlsEnabled = true  // 확대/축소 버튼을 활성화
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        GlobalScope.launch(Dispatchers.Main) {
            updateLocation()
        }

    }

    //현위치잡기
    private fun updateLocation() {
        val locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        locationCallBack = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult?.let {
                    for (location in it.locations) {
                        Log.d("위치정보", "위도:${location.latitude} 경도: ${location.longitude}")
                        setInitialUserLocation(location)
                    }
                }
            }
        }
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        // 코루틴 내에서 위치 업데이트 요청을 비동기로 처리
        GlobalScope.launch(Dispatchers.Main) {
            fusedLocationClient.requestLocationUpdates(
                locationRequest, locationCallBack,
                Looper.myLooper()!!
            )
        }
    }


    fun setInitialUserLocation(lastLocation: Location) {
        if (!userLocationSet) {
            val LATLNG = LatLng(lastLocation.latitude, lastLocation.longitude)
            val makerOptions = MarkerOptions().position(LATLNG).title("현재 위치입니다.")
            mGoogleMap.addMarker(makerOptions)
            userLocationSet = true // 사용자 위치를 설정했음을 표시
        }
    }


    // 날씨 API 데이터 가져오기
    @SuppressLint("SuspiciousIndentation", "NotifyDataSetChanged")
    private fun communicateWeather() = lifecycleScope.launch {
        val baseTime = listOf("0200", "0500", "0800", "1100", "1400", "1700", "2000", "2300")
        for (region in RegionLocation().regionList) {
            for (baseTime in baseTime) {
                val response: Response<Root> = WeatherClient.apiService.getWeatherInfo(
                    "JSON",
                    "20",
                    "1",
                    "20231025",
                    baseTime,
                    region.regionX.toString(),
                    region.regionY.toString()
                )
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        for (item in itemList) {
                            val baseTime = item.baseTime
                            val tmp = item.fcstValue
                            // tmp로 가져올 값 수정하기
                            val weatherItemData =
                                WeatherData(baseTime, tmp)
                            Log.d("weatherItemData", "$weatherItemData")
                            weatherDataList.add(weatherItemData)
                            Log.d("weatherData", "$weatherData")
                        }
                    }
                    weatherData.addAll(weatherDataList)
                    binding.infoPageRvWeather.adapter?.notifyDataSetChanged()
                }
            }
        }
    }
}