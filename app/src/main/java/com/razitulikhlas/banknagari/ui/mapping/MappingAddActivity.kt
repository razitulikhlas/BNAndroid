package com.razitulikhlas.banknagari.ui.mapping

import android.Manifest
import android.app.Activity
import android.content.ContentProviderOperation
import android.content.OperationApplicationException
import android.content.pm.PackageManager
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.RemoteException
import android.provider.ContactsContract
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.gms.location.LocationServices
import com.razitulikhlas.banknagari.R
import com.razitulikhlas.banknagari.databinding.ActivityMappingAddBinding
import com.razitulikhlas.banknagari.viewmodel.CustomerViewModel
import com.razitulikhlas.core.data.source.local.mapping.CustomerEntity
import com.razitulikhlas.core.util.format.RupiahFormat
import com.razitulikhlas.core.util.maps.DefaultLocationClient
import com.razitulikhlas.core.util.maps.LocationsClient
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class MappingAddActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMappingAddBinding

    private val viewModel: CustomerViewModel by viewModel()
    private lateinit var locationClient: LocationsClient
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)


    var photo : Uri? = null
    var latitude : Double? = null
    var longitude : Double? = null
    var address : String? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMappingAddBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.toolbar.title = "Data Nasabah"
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);


        locationClient = DefaultLocationClient(
            applicationContext,
            LocationServices.getFusedLocationProviderClient(applicationContext)
        )





        val type = arrayOf("tidak ada","Bank Nagari","BRI","BNI","Mandiri","BCA","BSI")

        val typeResponse = arrayOf("tidak tertarik","tertarik (tidak dalam waktu dekat)","tertarik (dalam waktu dekat)")
        val adapter = ArrayAdapter(
            this,
            R.layout.dropdown_menu_popup_item,
            type
        )

        val adapterTypeResponse = ArrayAdapter(
            this,
            R.layout.dropdown_menu_popup_item,
            typeResponse
        )

        ActivityCompat.requestPermissions(this,arrayOf(Manifest.permission.WRITE_CONTACTS),PackageManager.PERMISSION_GRANTED)


        with(binding){
            loanStatus.setAdapter(adapter)
            responseStatus.setAdapter(adapterTypeResponse)

            btnLocation.setOnClickListener {
               getDeviceLocation()
            }
            ivPhoto.setOnClickListener {
                getImage()
            }
            btnSave.setOnClickListener {
                saveData()
            }

            edPlatfond.addTextChangedListener(RupiahFormat(edPlatfond))
            edBakiDebe.addTextChangedListener(RupiahFormat(edBakiDebe))


        }
    }

    private fun saveData() {
        with(binding){
            if(edUsaha.text.isNullOrBlank()){
                showMessage("nama usaha tidak boleh kosong")
            }else if(edPemilik.text.isNullOrBlank()){
                showMessage("nama pemilik usaha tidak boleh kosong")
            }else if(edPhone.text.isNullOrBlank()){
                showMessage("nama pemilik usaha tidak boleh kosong")
            }else if(loanStatus.text.isNullOrBlank()){
                showMessage("status pinjaman tidak boleh kosong")
            }else if(responseStatus.text.isNullOrBlank()){
                showMessage("response nasabah tidak boleh kosong")
            }else if(edPlatfond.text.isNullOrBlank()){
                showMessage("silahkan masukan data platfond")
            }else if(edBakiDebe.text.isNullOrBlank()){
                showMessage("silahkan masukan data baki debet")
            }else if(edInformation.text.isNullOrBlank()){
                showMessage("nama pemilik usaha tidak boleh kosong")
            }else{
                val customerEntity = CustomerEntity(null,edUsaha.text.toString(),edPemilik.text.toString(),
                    edPhone.text.toString(), loanStatus.text.toString(),responseStatus.text.toString(),edPlatfond.text.toString(),edBakiDebe.text.toString(),edInformation.text.toString(),photo.toString(),address,latitude,longitude)
                lifecycleScope.launch {
                    saveContact()
                    viewModel.save(customerEntity)
                    finish()
                }
            }
        }
    }

    private fun showMessage(message:String){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()

    }

    private fun getImage(){
       ImagePicker.with(this)
           .crop()
           .compress(1024)
           .maxResultSize(1000,1000)
           .createIntent {
               startForProfileImageResult.launch(it)
           }
    }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    //Image Uri will not be null for RESULT_OK
                    val fileUri = data?.data!!
                    photo = fileUri
                    binding.ivPhoto.setImageURI(fileUri)
                }
                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }
        }

    private fun saveContact(){
        val content = ArrayList<ContentProviderOperation>()

        content.add(ContentProviderOperation.newInsert(
            ContactsContract.RawContacts.CONTENT_URI)
            .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
            .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
            .build())

        content.add(ContentProviderOperation
            .newInsert(ContactsContract.Data.CONTENT_URI)
            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,0)
            .withValue(ContactsContract.Data.MIMETYPE,
                ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
            .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                binding.edPemilik.text.toString())
            .build())

        content.add(ContentProviderOperation
            .newInsert(ContactsContract.Data.CONTENT_URI)
            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,0)
            .withValue(ContactsContract.Data.MIMETYPE,
                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
            .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,
                binding.edPhone.text.toString())
            .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
            .build())

        try {
            contentResolver.applyBatch(ContactsContract.AUTHORITY, content)
        } catch (e: OperationApplicationException) {
            e.printStackTrace()
        } catch (e: RemoteException) {
            e.printStackTrace()
        }

    }

    private fun getDeviceLocation(){
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
            ),
            0
        )
        locationClient
            .getLocationUpdates(100000L)
            .catch { e -> e.printStackTrace() }
            .onEach { location ->
                serviceScope.launch(Dispatchers.Main) {
                    latitude = location.latitude
                    longitude = location.longitude

                    val local = Locale("id", "Indonesia")
                    val geocode = Geocoder(this@MappingAddActivity,local)
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

    override fun onStop() {
        super.onStop()
        serviceScope.cancel()
    }



//    private fun getDeviceLocation() {
//        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
//            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
//            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),100)
//            return
//        }
//
//        settingClient.checkLocationSettings(locationSettingRequest).apply {
//            addOnSuccessListener {
//
//                fusedLocationProviderClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY,object :
//                    CancellationToken(){
//                    override fun onCanceledRequested(p0: OnTokenCanceledListener): CancellationToken = CancellationTokenSource().token
//
//                    override fun isCancellationRequested(): Boolean = false
//                }).addOnSuccessListener {
//                    latitude = it?.latitude
//                    longitude = it?.longitude
//
//                    val local = Locale("id", "Indonesia")
//                    val geocode = Geocoder(this@MappingAddActivity,local)
//                    geocode.getAddress(latitude!!,longitude!!){
//                        if(it != null){
//                            address = it.getAddressLine(0)
//                            binding.tvLocations.text = address
//                        }
//                    }
//                }
//
//            }
//            addOnFailureListener {
//                when ((it as ApiException).statusCode) {
//                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
//                        try {
//                            val re = it as ResolvableApiException
//                            re.startResolutionForResult(this@MappingAddActivity,  0x1)
//                        } catch (sie: IntentSender.SendIntentException) {
//                            e("TAG", "Error")
//                        }
//                    }
//                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
//                        val alertDialog = AlertDialog.Builder(this@MappingAddActivity)
//                        alertDialog.setTitle("Rami mart")
//                        alertDialog.setMessage("aplikasi membutuhkan gps mode high accuracy untuk menemukan lokasi anda")
//                        alertDialog.setIcon(R.mipmap.ic_launcher)
//                        alertDialog.setPositiveButton(
//                            "YA"
//                        ) { dialog, _ ->
//                            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
//                            dialog.dismiss()
//                        }
//                        alertDialog.setNegativeButton("BATAL") { dialog, _ ->
//                            dialog.dismiss()
//                        }
//                        alertDialog.show()
//                    }
//                }
//            }
//        }
//    }
}

public fun Geocoder.getAddress( latitude: Double,
                                 longitude: Double,
                                 address: (android.location.Address?) -> Unit){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getFromLocation(latitude, longitude, 1) { address(it.firstOrNull()) }
        return
    }

    try {
        address(getFromLocation(latitude, longitude, 1)?.firstOrNull())
    } catch(e: Exception) {
        //will catch if there is an internet problem
        address(null)
    }

}
