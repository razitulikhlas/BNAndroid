package com.razitulikhlas.banknagari.ui.permohonan

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import cn.pedant.SweetAlert.SweetAlertDialog
import com.razitulikhlas.banknagari.R
import com.razitulikhlas.banknagari.databinding.ActivityInsertApplicationBinding
import com.razitulikhlas.banknagari.ui.disposisi.HomeDisposisiActivity
import com.razitulikhlas.core.data.source.remote.network.ApiResponse
import com.razitulikhlas.core.data.source.remote.response.DataItemSkim
import com.razitulikhlas.core.util.Constant.NIK_CHECK
import com.razitulikhlas.core.util.format.RupiahFormat
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class InsertApplicationActivity : AppCompatActivity() {
    lateinit var binding : ActivityInsertApplicationBinding
    private val viewModel : OfficerViewModel by viewModel()
    private lateinit var pDialog: SweetAlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInsertApplicationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        pDialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        pDialog.progressHelper.barColor = Color.parseColor("#A5DC86")
        pDialog.titleText = "Loading"
        pDialog.setCancelable(false)
//        binding.toolbar.title = "Data Permohonan"
//        setSupportActionBar(binding.toolbar)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val skim = arrayOf("KPUM","KUR","KMK-MG","KRK","Makan Randang")

        val adapter = ArrayAdapter(
            this,
            R.layout.dropdown_menu_popup_item,
            skim
        )

        with(binding){
           edSkim.setAdapter(adapter)
           edPlatfond.addTextChangedListener(RupiahFormat(edPlatfond))
            btnSave.setOnClickListener {
                saveData()
            }
            ivBack.setOnClickListener {
                finish()
            }
        }

    }


    private fun saveData() {
        with(binding){
            if(edDebitur.text.isNullOrBlank()){
                showMessage("nama usaha tidak boleh kosong")
            }else if(edktpdebitur.text.isNullOrBlank()){
                showMessage("nama pemilik usaha tidak boleh kosong")
            }else if(edPenjamin.text.isNullOrBlank()){
                showMessage("status pinjaman tidak boleh kosong")
            }else if(edktpPenjamin.text.isNullOrBlank()){
                showMessage("response nasabah tidak boleh kosong")
            }else if(edPhone.text.isNullOrBlank()){
                showMessage("nama pemilik usaha tidak boleh kosong")
            }else if(edSkim.text.isNullOrBlank()){
                showMessage("silahkan masukan data baki debet")
            }else if(edSektorUsaha.text.isNullOrBlank()){
                showMessage("nama pemilik usaha tidak boleh kosong")
            }else if(edPlatfond.text.isNullOrBlank()){
                showMessage("silahkan masukan data platfond")
            }else if(edTimeLoan.text.isNullOrBlank()){
                showMessage("silahkan masukan data jangka waktu")
            }else{

                val plafond = edPlatfond.text.toString().replace("Rp ","").replace(".","")
                val disposisi = DataItemSkim(null,edktpPenjamin.text.toString(),edSkim.text.toString(),
                    plafond, null,NIK_CHECK.toInt(),null,edPenjamin.text.toString(),edSektorUsaha.text.toString(),null,
                    edPhone.text.toString(),edDebitur.text.toString(),edTimeLoan.text.toString().toInt(),null,edktpdebitur.text.toString(),null)
                lifecycleScope.launch {
//                    saveContact()
                    Log.e("TAG", "saveData: $plafond", )
                   viewModel.insert(disposisi).observeForever {
                       when(it){
                           is ApiResponse.Success->{
                               val intent = Intent(this@InsertApplicationActivity,
                                   HomeDisposisiActivity::class.java)
                               intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                               startActivity(intent)
                               finish()
                               showMessage("Sukses")
                           }
                           is ApiResponse.Error->{
                               Log.e("TAG", "saveData:eror ${it.errorMessage}", )
                               showMessage(it.errorMessage)
                           }
                           is ApiResponse.Empty->{
                               showMessage(it.toString())
                           }
                       }
                   }
                    delay(1000)
                    pDialog.dismiss()
                }

            }
        }
    }

    private fun showMessage(message:String){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }
}