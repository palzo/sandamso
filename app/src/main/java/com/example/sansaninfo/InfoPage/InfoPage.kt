package com.example.sansaninfo.InfoPage

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
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


        // 산림청_산_정보 인증키 - 4bpUeSQaXnUDSalDsumQ5dkxA+bJXWN4dhwsYexJp6wAJnadjR+UoIVo1Dhac/spEq1HRVngbbHuY8QLzUwVBg==

        initView()
    }


    private fun initView() = with(binding) {

    }

    override fun onMapReady(p0: GoogleMap) {

        val seoul = LatLng(37.566610, 126.978403)
        mGoogleMap = p0
        mGoogleMap.mapType = GoogleMap.MAP_TYPE_NORMAL // default 노말 생략 가능
        mGoogleMap.apply {
            val markerOptions = MarkerOptions()
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
            markerOptions.position(seoul)
            markerOptions.title("서울시청")
            markerOptions.snippet("Tel:01-120")
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
