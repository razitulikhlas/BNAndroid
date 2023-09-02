package com.razitulikhlas.banknagari.ui.petaCustomer

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.razitulikhlas.banknagari.BuildConfig
import com.razitulikhlas.banknagari.adapter.MappingCustomerAdapter
import com.razitulikhlas.banknagari.databinding.ActivityMapsBinding
import com.razitulikhlas.core.util.dummy.DataMapCustomer
import com.razitulikhlas.core.util.maps.DefaultLocationClient
import com.razitulikhlas.core.util.maps.LocationsClient
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject


class MapsActivity : AppCompatActivity(), OnMapReadyCallback,
    MappingCustomerAdapter.MappingCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val settingClient: SettingsClient by inject()
    private lateinit var mLocationRequest : LocationRequest
    private lateinit var  locationSettingRequest: LocationSettingsRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var mCurrentLocation: Location
    private var mRequestLocationUpdate = false
    private var mLocationPermissionGranted = false
    private val CODE_REQUEST_PERMISSION = 1000

    private val mapAdapter: MappingCustomerAdapter by inject()

    private lateinit var locationClient: LocationsClient
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        supportActionBar?.elevation = 0f

        permission()

        locationClient = DefaultLocationClient(
            applicationContext,
            LocationServices.getFusedLocationProviderClient(applicationContext)
        )
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        val data = DataMapCustomer.dataDummy()
        mapAdapter.setData(data)
        mapAdapter.setListener(this@MapsActivity)
        with(binding.rcv){
            layoutManager = LinearLayoutManager(this@MapsActivity,LinearLayoutManager.HORIZONTAL,false)
            setHasFixedSize(true)
            adapter = mapAdapter
        }

//        val position = binding.rcv.getCurrentPosition()

        Log.d("TAG", "onCreate: ")

        binding.rcv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val position: Int = getCurrentItem()
                Log.e("TAG", "onScrollStateChanged: $position")
                val cs =data.get(position)
                val sydney = LatLng(cs.latitude, cs.longitude)
//                MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title("Hello Maps")
                val marker = MarkerOptions().position(sydney).title(cs.name)
                marker.icon(BitmapDescriptorFactory.fromResource(com.razitulikhlas.banknagari.R.drawable.marker_store))

// Changing marker icon
//                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.my_marker_icon)));

                mMap.addMarker(marker // below line is use to add custom marker on our map.
                )
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney,17f))
            }
        })

        val snapHelper:SnapHelper=LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.rcv)



//        locationCallback = object : LocationCallback() {
//            override fun onLocationResult(p0: LocationResult) {
//                super.onLocationResult(p0)
//                if(p0.lastLocation != null){
//                    mCurrentLocation = p0.lastLocation!!
//                    latitude = mCurrentLocation.latitude
//                    longitude = mCurrentLocation.longitude
//
//                    val sydney = LatLng(latitude!!, longitude!!)
//                    mMap.addMarker(
//                        MarkerOptions().position(sydney)
//                            .title("Marker in Sydney") // below line is use to add custom marker on our map.
//                    )
//                    if (ActivityCompat.checkSelfPermission(
//                            this@MapsActivity,
//                            Manifest.permission.ACCESS_FINE_LOCATION
//                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                            this@MapsActivity,
//                            Manifest.permission.ACCESS_COARSE_LOCATION
//                        ) != PackageManager.PERMISSION_GRANTED
//                    ) {
//                        // TODO: Consider calling
//                        //    ActivityCompat#requestPermissions
//                        // here to request the missing permissions, and then overriding
//                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                        //                                          int[] grantResults)
//                        // to handle the case where the user grants the permission. See the documentation
//                        // for ActivityCompat#requestPermissions for more details.
//                        return
//                    }
//                    mMap.isMyLocationEnabled =true
//                    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
//
//                    Log.e("TAG", "onLocationResult: ${mCurrentLocation.latitude}")
//                }
//            }
//        }
//
//        mLocationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
//            .setWaitForAccurateLocation(true)
//            .setMinUpdateIntervalMillis(10000)
//            .setMaxUpdateDelayMillis(1000)
//            .setMinUpdateDistanceMeters(0f)
//            .build()
//
//        locationSettingRequest = LocationSettingsRequest.Builder().apply {
//            addLocationRequest(LocationRequest.create())
//            setAlwaysShow(true)
//        }.build()

    }

    fun RecyclerView?.getCurrentPosition() : Int {
        return (this?.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.e("TAG", "onRequestPermissionsResult: call")
        if (requestCode == CODE_REQUEST_PERMISSION) {
            if(grantResults.isNotEmpty()){
                for(element in grantResults){
                    if (element != PackageManager.PERMISSION_GRANTED) {
                        mLocationPermissionGranted = false
                        Log.e("TAG", "onRequestPermissionsResult: failed")
                        return
                    }
                }
                Log.e("TAG", "onRequestPermissionsResult: granted")
                mLocationPermissionGranted = true
                initMaps()
            }

        }
    }

    private fun getCurrentItem(): Int {
        return (binding.rcv.getLayoutManager() as LinearLayoutManager)
            .findFirstVisibleItemPosition()
    }


    @SuppressLint("MissingPermission")
    private fun initMaps(){
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(com.razitulikhlas.banknagari.R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)



    }



    private fun openSetting(){
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts("package",BuildConfig.APPLICATION_ID,null)
        intent.data = uri
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        locationClient
            .getLocationUpdates(1000000000L)
            .catch { e -> e.printStackTrace() }
            .onEach { location ->
                serviceScope.launch(Dispatchers.Main) {
                    val lat = location.latitude
                    val long = location.longitude

                    val sydney = LatLng(lat, long)
                    mMap.addMarker(
                        MarkerOptions().position(sydney)
                            .title("Lokasi Anda") // below line is use to add custom marker on our map.
                    )
                    mMap.isMyLocationEnabled =true
                    mMap.isTrafficEnabled = true
                    mMap.uiSettings.isCompassEnabled =true
                    mMap.uiSettings.isMapToolbarEnabled = true
                    mMap.isIndoorEnabled = true
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney,17f))
                }
                Log.e("TAG", "onMapReady: " )
            }
            .launchIn(serviceScope)


        // Add a marker in Sydney and move the camera

    }

    private fun permission(){
        Log.e("TAG", "permission: getlocation permission")
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted =true
            initMaps()
        }else{
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                ),
                CODE_REQUEST_PERMISSION
            )
        }

    }


    private fun getDeviceLocation() {
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),100)
            return
        }
        mRequestLocationUpdate = true
        settingClient.checkLocationSettings(locationSettingRequest).apply {
            addOnSuccessListener {
                fusedLocationProviderClient.requestLocationUpdates(mLocationRequest,locationCallback,
                    Looper.myLooper())
            }
            addOnFailureListener {
                when ((it as ApiException).statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        try {
                            val re = it as ResolvableApiException
                            re.startResolutionForResult(this@MapsActivity,  0x1)
                        } catch (sie: IntentSender.SendIntentException) {
                            Log.e("TAG", "Error")
                        }
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        val alertDialog = AlertDialog.Builder(this@MapsActivity)
                        alertDialog.setTitle("Rami mart")
                        alertDialog.setMessage("aplikasi membutuhkan gps mode high accuracy untuk menemukan lokasi anda")
                        alertDialog.setIcon(com.razitulikhlas.banknagari.R.mipmap.ic_launcher)
                        alertDialog.setPositiveButton(
                            "YA"
                        ) { dialog, _ ->
                            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                            dialog.dismiss()
                        }
                        alertDialog.setNegativeButton("BATAL") { dialog, _ ->
                            dialog.dismiss()
                        }
                        alertDialog.show()
                    }
                }
            }
        }
    }

    private fun stopLocationUpdate(){
        fusedLocationProviderClient.removeLocationUpdates(locationCallback).addOnCompleteListener{
            Log.d("TAG", "stopLocationUpdate: ")
        }
    }




    override fun onStop() {
        super.onStop()
        serviceScope.cancel()
    }

    override fun onClick(customer: com.razitulikhlas.core.domain.mapping.model.DataMapCustomer) {
        val uri: Uri = Uri.parse("google.navigation:q=${customer.latitude},${customer.longitude}&mode=d")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage("com.google.android.apps.maps")
        startActivity(intent)
    }


}