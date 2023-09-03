package com.razitulikhlas.banknagari.ui.disposisi

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.razitulikhlas.banknagari.R
import com.razitulikhlas.banknagari.databinding.ActivityDetailDisposisiBinding
import com.razitulikhlas.banknagari.ui.mapping.getAddress
import com.razitulikhlas.banknagari.ui.permohonan.OfficerViewModel
import com.razitulikhlas.banknagari.utils.AppConstant.LOCATION_REQUEST_CODE
import com.razitulikhlas.banknagari.utils.AppPermission
import com.razitulikhlas.core.data.source.local.client.ClientEntity
import com.razitulikhlas.core.data.source.remote.network.ApiResponse
import com.razitulikhlas.core.data.source.remote.response.DataItemSkim
import com.razitulikhlas.core.util.Constant.LEVEL_CHECK
import com.razitulikhlas.core.util.maps.DefaultLocationClient
import com.razitulikhlas.core.util.maps.LocationsClient
import com.razitulikhlas.core.util.showToastShort
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.NumberFormat
import java.util.*


class DetailDisposisiActivity : AppCompatActivity() {
    lateinit var binding : ActivityDetailDisposisiBinding
    private val settingClient: SettingsClient by inject()
    private lateinit var  locationSettingRequest: LocationSettingsRequest
    private lateinit var customDialog: Dialog
    private lateinit var txtInputKet: EditText
    private lateinit var btnInsertName: Button
    private lateinit var builder : AlertDialog.Builder

    private lateinit var locationClient: LocationsClient
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var permission : AppPermission
    private lateinit var data  : DataItemSkim


    var photo : Uri? = null
    var latitude : Double? = null
    var longitude : Double? = null
    var address : String? = null

    private val viewModel : OfficerViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDetailDisposisiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        builder = AlertDialog.Builder(this)
        permission = AppPermission()
        locationClient = DefaultLocationClient(
            applicationContext,
            LocationServices.getFusedLocationProviderClient(applicationContext)
        )
        locationSettingRequest = LocationSettingsRequest.Builder().apply {
            addLocationRequest(LocationRequest.create())
            setAlwaysShow(true)
        }.build()

        setImageResource()

        binding.btnLocation.setOnClickListener {
            Log.e("TAG", "onCreate: ${data.status}")
            val intent = Intent(this, MapsUsahaActivity::class.java)
            resultLocationRequest.launch(intent)
//            startActivity(Intent(this,MapsUsahaActivity::class.java))
//            if(data.status == 0 || data.status!! > 1){
//                if(binding.tvLokasi.text.isNotEmpty()) {
//                    val uri = Uri.parse("google.navigation:q=${latitude},${longitude}&mode=d")
//                    val intent = Intent(Intent.ACTION_VIEW, uri)
//                    intent.setPackage("com.google.android.apps.maps")
//                    startActivity(intent)
//                }
//                Log.e("TAG", "onCreate: status cancel or diterima", )
//            }else{
//                Log.e("TAG", "onCreate: status di proses", )
//                if(permission.isLocationOk(this)){
//                    Log.e("TAG", "onCreate: request Permission ok", )
//                    checkGPS()
//                }else if(permission.showPermissionRequestPermissionRationale(this)){
//                    Log.e("TAG", "onCreate: request Permission ok1", )
//                    permission.showDialogShowRequestPermissionRationale(this)
//                }else{
//                    Log.e("TAG", "onCreate: request Permission ok2", )
//                    permission.requestPermissionLocation(this)
//                }
//            }
        }
        data = intent.getParcelableExtra<DataItemSkim>("data")!!
        initCustomDialog(data?.id!!)
        initViewComponents()

        Log.e("TAG", "onCreate: ${data}")

        if(data.status == 0 || data.status!! > 1){
            binding.layoutBtn.visibility = View.GONE
//            binding.btnLocation.visibility = View.GONE

            lifecycleScope.launch {
                viewModel.getClientId(data.id!!.toLong()).observeForever {
                   binding.tvLokasi.text = it.address
                    latitude = it.latitude
                    longitude = it.longitude
                }
            }
        }

        builder.setTitle("Proses permohonan")
        builder.setMessage("apakah anda yakin?")
//builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            updateDisposisi(data.id!!,2,"Data di proses karena sesuai dengan persyaratan")
        }

        builder.setNegativeButton(android.R.string.no) { dialog, which ->
            Toast.makeText(applicationContext,
                android.R.string.no, Toast.LENGTH_SHORT).show()
        }


        with(binding){
            tvNameDebitur.text = data?.pemohon
            tvKtpDebitur.text=data?.ktpPemohon
            tvNamePenjamin.text=data?.penjamin
            tvKtpPenjamin.text=data?.ktpPenjamin
            tvSektorUsaha.text=data?.sektorUsaha
            tvPlafond.text= formatRupiahh(data?.plafond!!.toDouble())
            tvJangkaWaktu.text="${data.jangkaWaktu} bulan"
            tvSkimKredit.text=data.skimKredit
            tvInfo.text = data.keterangan

            ivPhone.setOnClickListener {
                if(ActivityCompat.checkSelfPermission(this@DetailDisposisiActivity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(this@DetailDisposisiActivity, arrayOf(Manifest.permission.CALL_PHONE),10)
                    return@setOnClickListener
                }else{
                    val uri = "tel:" +data.phone?.trim()
                    val intent = Intent(Intent.ACTION_CALL)
                    intent.data = Uri.parse(uri)
                    startActivity(intent)
                }
            }

            btnProcess.setOnClickListener {
                builder.show()

            }

            ivBack.setOnClickListener {
                finish()
            }
        }
    }

    var resultLocationRequest = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data
            val location = data?.getStringExtra("location")

            binding.tvLokasi.text = location

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == LOCATION_REQUEST_CODE) {
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


    fun formatRupiahh(amount: Double): String {
        val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        return format.format(amount)
    }


    private fun initCustomDialog(id:Int) {
        customDialog = Dialog(this)
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        customDialog.setContentView(R.layout.dialog_tolak)
        customDialog.setCancelable(true)
        txtInputKet = customDialog.findViewById(R.id.ket)
        btnInsertName =customDialog.findViewById(R.id.btnCancelld)
        btnInsertName.setOnClickListener {
            val name: String = txtInputKet.getText().toString()
            updateDisposisi(id,0,name)
            customDialog.dismiss()
        }

    }

    private fun initViewComponents() {
        with(binding){
            if(LEVEL_CHECK < 2){
                btnCancell.visibility = View.GONE
                btnProcess.visibility = View.GONE

            }
            btnCancell.setOnClickListener {
                customDialog.show()
            }
        }

    }

    private fun setImageResource(){
        with(binding){
            image1.setOnClickListener {
                val intent = Intent(this@DetailDisposisiActivity,PhotoBusinessActivity::class.java)
                intent.putExtra("image","/storage/emulated/0/Android/data/com.razitulikhlas.banknagari/files/DCIM/IMG_20230902_123045155.jpg")
                startActivity(intent)
            }
            image1.setImageURI("/storage/emulated/0/Android/data/com.razitulikhlas.banknagari/files/DCIM/IMG_20230902_123045155.jpg".toUri())
            image2.setImageURI("/storage/emulated/0/Android/data/com.razitulikhlas.banknagari/files/DCIM/IMG_20230902_123045155.jpg".toUri())
            image3.setImageURI("/storage/emulated/0/Android/data/com.razitulikhlas.banknagari/files/DCIM/IMG_20230902_123045155.jpg".toUri())
            image4.setImageURI("/storage/emulated/0/Android/data/com.razitulikhlas.banknagari/files/DCIM/IMG_20230902_123045155.jpg".toUri())
            image5.setImageURI("/storage/emulated/0/Android/data/com.razitulikhlas.banknagari/files/DCIM/IMG_20230902_123045155.jpg".toUri())
            image6.setImageURI("/storage/emulated/0/Android/data/com.razitulikhlas.banknagari/files/DCIM/IMG_20230902_123045155.jpg".toUri())
            image7.setImageURI("/storage/emulated/0/Android/data/com.razitulikhlas.banknagari/files/DCIM/IMG_20230902_123045155.jpg".toUri())
            image8.setImageURI("/storage/emulated/0/Android/data/com.razitulikhlas.banknagari/files/DCIM/IMG_20230902_123045155.jpg".toUri())
        }
    }

    private fun updateDisposisi(id:Int,status:Int,informasi:String){
        lifecycleScope.launch {
            viewModel.updateStatusDisposisi(id,status, informasi).observeForever {
                when(it){
                    is ApiResponse.Success->{
//
                        val intent = Intent(this@DetailDisposisiActivity,HomeDisposisiActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                        showToastShort(this@DetailDisposisiActivity,"Sukses update disposisi")
                    }
                    is ApiResponse.Error->{
                        showToastShort(this@DetailDisposisiActivity,"gagal update disposisi")
                    }
                    is ApiResponse.Empty->{

                    }
                }
            }
        }
    }


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

                            val local = Locale("id", "Indonesia")
                            val geocode = Geocoder(this@DetailDisposisiActivity,local)
                            geocode.getAddress(latitude!!,longitude!!){
                                if(it != null){
                                    address = it.getAddressLine(0)
                                    binding.tvLokasi.text = address
                                    val client = ClientEntity(null,data.id!!.toLong(),data.pemohon,null,address,latitude,longitude)
                                    lifecycleScope.launch {
                                        viewModel.save(client)
                                    }
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
                            re.startResolutionForResult(this@DetailDisposisiActivity,  0x1)
                        } catch (sie: IntentSender.SendIntentException) {
                            Log.e("TAG", "Error")
                        }
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        val alertDialog = AlertDialog.Builder(this@DetailDisposisiActivity)
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
}