package com.razitulikhlas.banknagari.ui.disposisi

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.util.Log.e
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.razitulikhlas.banknagari.R
import com.razitulikhlas.banknagari.databinding.ActivityMapsUsahaBinding
import com.razitulikhlas.banknagari.ui.mapping.getAddress
import com.razitulikhlas.banknagari.ui.permohonan.OfficerViewModel
import com.razitulikhlas.banknagari.utils.AppConstant
import com.razitulikhlas.banknagari.utils.AppPermission
import com.razitulikhlas.core.data.source.remote.network.ApiResponse
import com.razitulikhlas.core.data.source.remote.response.DataImage
import com.razitulikhlas.core.util.maps.DefaultLocationClient
import com.razitulikhlas.core.util.maps.LocationsClient
import com.razitulikhlas.core.util.prepareFilePart
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.ArrayList

class MapsUsahaActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsUsahaBinding
    private lateinit var permission : AppPermission
    private val settingClient: SettingsClient by inject()
    private lateinit var  locationSettingRequest: LocationSettingsRequest
    private lateinit var locationClient: LocationsClient
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    var latitude : Double? = null
    var longitude : Double? = null
    var address : String? = null

    var images = ArrayList<String>()

    private val viewModel : OfficerViewModel by viewModel()

    var images1 : Uri? = null
    var images2 : Uri? = null
    var images3 : Uri? = null
    var images4 : Uri? = null
    var images5 : Uri? = null
    var images6 : Uri? = null
    var images7 : Uri? = null
    var images8 : Uri? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsUsahaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        clearImage()
        imageClick()
        delImage()

        permission = AppPermission()
        locationClient = DefaultLocationClient(
            applicationContext,
            LocationServices.getFusedLocationProviderClient(applicationContext)
        )
        locationSettingRequest = LocationSettingsRequest.Builder().apply {
            addLocationRequest(LocationRequest.create())
            setAlwaysShow(true)
        }.build()
        if(permission.isLocationOk(this)){
            e("TAG", "onCreate: request Permission ok")
            checkGPS()
        }else if(permission.showPermissionRequestPermissionRationale(this)){
            e("TAG", "onCreate: request Permission ok1")
            permission.showDialogShowRequestPermissionRationale(this)
        }else{
            e("TAG", "onCreate: request Permission ok2")
            permission.requestPermissionLocation(this)
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.btnLocation.setOnClickListener {
//            val img1 = prepareFilePart("image1", images1, this)
            val id = intent.getIntExtra("id",0)
            val dataImage = DataImage(null,
                null,id.toString(),
                null,address,
                null,longitude.toString(),
                null,latitude.toString())
            lifecycleScope.launch {
                viewModel.insertInfoUsaha(
                    dataImage,
                    prepareFilePart("image1", images1, this@MapsUsahaActivity),
                    prepareFilePart("image2", images2, this@MapsUsahaActivity),
                    prepareFilePart("image3", images3, this@MapsUsahaActivity),
                    prepareFilePart("image4", images4, this@MapsUsahaActivity),
                    prepareFilePart("image5", images5, this@MapsUsahaActivity),
                    prepareFilePart("image6", images6, this@MapsUsahaActivity),
                    prepareFilePart("image7", images7, this@MapsUsahaActivity),
                    prepareFilePart("image8", images8, this@MapsUsahaActivity),
                ).observeForever {
                    when (it) {
                        is ApiResponse.Success -> {
                            showMessage("Sukses upload foto")
                            val intent = Intent()
                            intent.putExtra("location",address)
                            intent.putExtra("images",images)
                            setResult(RESULT_OK, intent)
                            finish()
                        }
                        is ApiResponse.Error -> {
                            showMessage(it.errorMessage)
                        }
                        is ApiResponse.Empty -> {
                            showMessage(it.toString())
                        }
                        else -> {

                        }
                    }
                }
            }
        }
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

    private fun showMessage(message:String){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

//        // Add a marker in Sydney and move the camera
//        val sydney = LatLng(-34.0, 151.0)
//        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }


    @SuppressLint("MissingPermission")
    private fun checkGPS(){
        settingClient.checkLocationSettings(locationSettingRequest).apply {
            addOnSuccessListener {
                locationClient
                    .getLocationUpdates(100000L)
                    .catch { e -> e.printStackTrace() }
                    .onEach { location ->
                        serviceScope.launch(Dispatchers.Main) {
                            latitude = location.latitude
                            longitude = location.longitude

                            e("tag","latitude:$latitude,longi:$longitude")

                            val sydney = LatLng(latitude!!, longitude!!)

                            mMap.addMarker(
                                MarkerOptions().position(sydney)
                                    .title("Lokasi Anda") // below line is use to add custom marker on our map.
                            )
                            mMap.isMyLocationEnabled =true
                            mMap.isTrafficEnabled = true
                            mMap.uiSettings.isCompassEnabled =true
                            mMap.uiSettings.isMapToolbarEnabled = true
                            mMap.uiSettings.isZoomControlsEnabled = true
                            mMap.isIndoorEnabled = true
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney,19f))

                            val local = Locale("id", "Indonesia")
                            val geocode = Geocoder(this@MapsUsahaActivity,local)
                            geocode.getAddress(latitude!!,longitude!!){
                                if(it != null){
                                    address = it.getAddressLine(0)
                                    binding.tvLocations.text = address
                                }
                            }
                        }
                    }
                    .launchIn(serviceScope)
            }
            addOnFailureListener {
                when ((it as ApiException).statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        try {
                            val re = it as ResolvableApiException
                            re.startResolutionForResult(this@MapsUsahaActivity,  0x1)
                        } catch (sie: IntentSender.SendIntentException) {
                            e("TAG", "Error")
                        }
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        val alertDialog = AlertDialog.Builder(this@MapsUsahaActivity)
                        alertDialog.setTitle("Bank Nagari")
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


    private fun clearImage() {
        with(binding) {
            cv1.strokeColor = ContextCompat.getColor(this@MapsUsahaActivity, R.color.normal)
            cv2.strokeColor = ContextCompat.getColor(this@MapsUsahaActivity, R.color.normal)
            cv3.strokeColor = ContextCompat.getColor(this@MapsUsahaActivity, R.color.normal)
            cv4.strokeColor = ContextCompat.getColor(this@MapsUsahaActivity, R.color.normal)
            cv5.strokeColor = ContextCompat.getColor(this@MapsUsahaActivity, R.color.normal)
            cv6.strokeColor = ContextCompat.getColor(this@MapsUsahaActivity, R.color.normal)
            cv7.strokeColor = ContextCompat.getColor(this@MapsUsahaActivity, R.color.normal)
            cv8.strokeColor = ContextCompat.getColor(this@MapsUsahaActivity, R.color.normal)

            image1.setImageResource(R.drawable.border)
            image2.setImageResource(R.drawable.border)
            image3.setImageResource(R.drawable.border)
            image4.setImageResource(R.drawable.border)
            image5.setImageResource(R.drawable.border)
            image6.setImageResource(R.drawable.border)
            image7.setImageResource(R.drawable.border)
            image8.setImageResource(R.drawable.border)

            del1.visibility = View.GONE
            del2.visibility = View.GONE
            del3.visibility = View.GONE
            del4.visibility = View.GONE
            del5.visibility = View.GONE
            del6.visibility = View.GONE
            del7.visibility = View.GONE
            del8.visibility = View.GONE
        }
    }

    private fun delImage(){
        with(binding){
            del1.setOnClickListener {
                images1 = null
                image1.setImageResource(R.drawable.border)
                del1.visibility = View.GONE
            }
            del2.setOnClickListener {
                images2 = null
                image2.setImageResource(R.drawable.border)
                del2.visibility = View.GONE
            }
            del3.setOnClickListener {
                images3 = null
                image3.setImageResource(R.drawable.border)
                del3.visibility = View.GONE
            }
            del4.setOnClickListener {
                images4 = null
                image4.setImageResource(R.drawable.border)
                del4.visibility = View.GONE
            }
            del5.setOnClickListener {
                images5 = null
                image5.setImageResource(R.drawable.border)
                del5.visibility = View.GONE
            }
            del6.setOnClickListener {
                images6 = null
                image6.setImageResource(R.drawable.border)
                del6.visibility = View.GONE
            }
            del7.setOnClickListener {
                images7 = null
                image7.setImageResource(R.drawable.border)
                del7.visibility = View.GONE
            }
            del8.setOnClickListener {
                images8 = null
                image8.setImageResource(R.drawable.border)
                del8.visibility = View.GONE
            }
        }
    }

    private fun imageClick(){
        with(binding){
            image1.setOnClickListener {
                e("TAG", "imageClick1: ", )
                if(images1 == null){
                    getImage(1)
                }
            }
            image2.setOnClickListener {
                e("TAG", "imageClick1: ", )
                if(images2 == null){
                    getImage(2)
                }
            }
            image3.setOnClickListener {
                e("TAG", "imageClick1: ", )
                if(images3 == null){
                    getImage(3)
                }
            }
            image4.setOnClickListener {
                e("TAG", "imageClick1: ", )
                if(images4 == null){
                    getImage(4)
                }
            }
            image5.setOnClickListener {
                e("TAG", "imageClick1: ", )
                if(images5 == null){
                    getImage(5)
                }
            }
            image6.setOnClickListener {
                e("TAG", "imageClick1: ", )
                if(images6 == null){
                    getImage(6)
                }
            }
            image7.setOnClickListener {
                e("TAG", "imageClick1: ", )
                if(images7 == null){
                    getImage(7)
                }
            }
            image8.setOnClickListener {
                e("TAG", "imageClick1: ", )
                if(images8 == null){
                    getImage(8)
                }
            }
        }
    }

    private fun getImage(image:Int){
        if(image == 1){
            ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1000,1000)
                .createIntent {
                    startForProfileImageResult1.launch(it)
                }
        }else if(image ==2){
            ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1000,1000)
                .createIntent {
                    startForProfileImageResult2.launch(it)
                }
        }else if(image == 3){
            ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1000,1000)
                .createIntent {
                    startForProfileImageResult3.launch(it)
                }
        }else if(image == 4){
            ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1000,1000)
                .createIntent {
                    startForProfileImageResult4.launch(it)
                }
        }else if(image == 5){
            ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1000,1000)
                .createIntent {
                    startForProfileImageResult5.launch(it)
                }
        }else if(image == 6){
            ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1000,1000)
                .createIntent {
                    startForProfileImageResult6.launch(it)
                }
        }else if(image == 7){
            ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1000,1000)
                .createIntent {
                    startForProfileImageResult7.launch(it)
                }
        }else if(image == 8){
            e("TAG", "getImage8: ", )
            ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1000,1000)
                .createIntent {
                    startForProfileImageResult8.launch(it)
                }
        }

    }

    private val startForProfileImageResult1 =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    //Image Uri will not be null for RESULT_OK
                    images1 = data?.data!!
//                    val photo = fileUri
                    with(binding){
                        e("da",images1.toString())

                        images1?.let{
                            image1.setImageURI(it)
                            images.add(it.toString())
                        }
//                        var originalFile = File(images1!!.path!!)
//                        e("TAG", "size sebelum di compress: ${getReadableFileSize(originalFile.length())}")
                        del1.visibility = View.VISIBLE
                    }

                }
                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }
        }



    private val startForProfileImageResult2 =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                val resultCode = result.resultCode
                val data = result.data

                when (resultCode) {
                    Activity.RESULT_OK -> {
                        //Image Uri will not be null for RESULT_OK
                        images2 = data?.data!!
//                    val photo = fileUri
                        with(binding){
                            images2?.let{
                                image2.setImageURI(it)
                                images.add(it.toString())
                            }
                            del2.visibility = View.VISIBLE
                        }
                    }
                    ImagePicker.RESULT_ERROR -> {
                        Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    private val startForProfileImageResult3 =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                    val resultCode = result.resultCode
                    val data = result.data

                    when (resultCode) {
                        Activity.RESULT_OK -> {
                            //Image Uri will not be null for RESULT_OK
                            images3 = data?.data!!
//                    val photo = fileUri
                            with(binding){
                                images3?.let{
                                    image3.setImageURI(it)
                                    images.add(it.toString())
                                }
                                del3.visibility = View.VISIBLE
                            }
                        }
                        ImagePicker.RESULT_ERROR -> {
                            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
    private val startForProfileImageResult4 =
                    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                        val resultCode = result.resultCode
                        val data = result.data

                        when (resultCode) {
                            Activity.RESULT_OK -> {
                                //Image Uri will not be null for RESULT_OK
                                images4 = data?.data!!
                                with(binding){
                                    images4?.let{
                                        image4.setImageURI(it)
                                        images.add(it.toString())
                                    }
                                    del4.visibility = View.VISIBLE
                                }
                            }
                            ImagePicker.RESULT_ERROR -> {
                                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                            }
                            else -> {
                                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
    private val startForProfileImageResult5 =
                        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                            val resultCode = result.resultCode
                            val data = result.data

                            when (resultCode) {
                                Activity.RESULT_OK -> {
                                    //Image Uri will not be null for RESULT_OK
                                    images5 = data?.data!!
//                    val photo = fileUri
                                    with(binding){
                                        images5?.let{
                                            image5.setImageURI(it)
                                            images.add(it.toString())
                                        }
                                        del5.visibility = View.VISIBLE
                                    }
                                }
                                ImagePicker.RESULT_ERROR -> {
                                    Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                                }
                                else -> {
                                    Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }

    private val startForProfileImageResult6 =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    //Image Uri will not be null for RESULT_OK
                    images6 = data?.data!!
//                    val photo = fileUri
                    with(binding){
                        images6?.let{
                            image6.setImageURI(it)
                            images.add(it.toString())
                        }
                        del6.visibility = View.VISIBLE
                    }
                }
                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }
        }

    private val startForProfileImageResult7 =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    //Image Uri will not be null for RESULT_OK
                    images7= data?.data!!
//                    val photo = fileUri
                    with(binding){
                        images7?.let{
                            image7.setImageURI(it)
                            images.add(it.toString())
                        }
                        del7.visibility = View.VISIBLE
                    }
                }
                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }
        }

    private val startForProfileImageResult8 =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    //Image Uri will not be null for RESULT_OK
                    images8 = data?.data!!
//                    val photo = fileUri
                    with(binding){
                        images8?.let{
                            image8.setImageURI(it)
                            images.add(it.toString())
                        }
                        del8.visibility = View.VISIBLE
                    }
                }
                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }
        }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == AppConstant.LOCATION_REQUEST_CODE) {
            Log.e("TAG", "onRequestPermissionsResult: 1")
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.e("TAG", "onRequestPermissionsResult: 2")

                checkGPS()
            }else if(permission.showPermissionRequestPermissionRationale(this)){
                Log.e("TAG", "onRequestPermissionsResult: 3")
                permission.showDialogShowRequestPermissionLocation(this)
            }else{
                Log.e("TAG", "onRequestPermissionsResult: 4")
                permission.showDialogShowRequestPermissionLocation(this)

            }
        }
    }

}