package com.example.sansaninfo.InfoPage

import android.content.Intent
import android.content.pm.PackageManager
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

class InfoPage : AppCompatActivity(), OnMapReadyCallback {

    private val binding by lazy { ActivityInfoPageBinding.inflate(layoutInflater) }
    private lateinit var mGoogleMap: GoogleMap
    lateinit var fusedLocationClient: FusedLocationProviderClient //gps를 이용해 위치 확인
    lateinit var locationCallBack: LocationCallback // 위치 값 요청에 대한 갱신 정보를 받는 변수
    lateinit var locationPermission: ActivityResultLauncher<Array<String>>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

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

    override fun onMapReady(p0: GoogleMap) {
        //설악산 위치
        val mountainLocation = LatLng(38.112864, 128.452297)
        mGoogleMap = p0
        mGoogleMap.mapType = GoogleMap.MAP_TYPE_NORMAL // default 노말 생략 가능
        mGoogleMap.apply {
            val markerOptions = MarkerOptions()
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
            markerOptions.position(mountainLocation)
            markerOptions.title("설악산")
            markerOptions.snippet("Tel")
            addMarker(markerOptions)
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        updateLocation()
    }

    fun updateLocation() {
        val locationRequest = LocationRequest.create().apply {
            interval = 1000
            fastestInterval = 500
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        locationCallBack = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult?.let {
                    for (location in it.locations) {
                        Log.d("위치정보", "위도:${location.latitude} 경도: ${location.longitude}")
                        setLastLocation(location)
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
        fusedLocationClient.requestLocationUpdates(
            locationRequest, locationCallBack,
            Looper.myLooper()!!
        )
    }

    fun setLastLocation(lastLocation: Location) {
        val LATLNG = LatLng(lastLocation.latitude, lastLocation.longitude)
        val makerOptions = MarkerOptions().position(LATLNG).title("현재 위치입니다.")
        val cameraPosition = CameraPosition.Builder().target(LATLNG).zoom(15.0f).build()
        mGoogleMap.addMarker(makerOptions)
        mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }
}
