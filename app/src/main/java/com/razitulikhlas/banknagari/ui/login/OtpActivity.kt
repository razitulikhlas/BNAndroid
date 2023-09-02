package com.razitulikhlas.banknagari.ui.login

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import cn.pedant.SweetAlert.SweetAlertDialog
import com.razitulikhlas.banknagari.databinding.ActivityOtpBinding
import com.razitulikhlas.banknagari.ui.dashboard.DashBoardOwnActivity
import com.razitulikhlas.banknagari.ui.disposisi.HomeDisposisiActivity
import com.razitulikhlas.core.data.source.remote.network.ApiResponse
import com.razitulikhlas.core.data.storage.DataStoreManager
import com.razitulikhlas.core.util.Constant.IS_ID_USER
import com.razitulikhlas.core.util.Constant.IS_LEVEL
import com.razitulikhlas.core.util.Constant.IS_LOGIN
import com.razitulikhlas.core.util.Constant.LEVEL_CHECK
import com.razitulikhlas.core.util.Constant.NIK_CHECK
import com.razitulikhlas.core.util.checkFocus
import com.razitulikhlas.core.util.showToastShort
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class OtpActivity : AppCompatActivity() {
    private lateinit var pDialog: SweetAlertDialog
    private var phone: String = ""
    private val viewModel : LoginViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenStarted {
            val login = DataStoreManager.getBooleanValue(
                this@OtpActivity,
                IS_LOGIN, false
            )
            if (login) {
                val level = DataStoreManager.getIntValue(
                    this@OtpActivity,
                    IS_LEVEL, 0
                )
                val nik = DataStoreManager.getStringValue(
                    this@OtpActivity,
                    IS_ID_USER, ""
                )
                LEVEL_CHECK = level
                NIK_CHECK = nik
                if(level > 2){
                    val intent = Intent(this@OtpActivity, HomeDisposisiActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }else{
                    val intent = Intent(this@OtpActivity, DashBoardOwnActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }

            }else{
                val binding = ActivityOtpBinding.inflate(layoutInflater)
                setContentView(binding.root)

                pDialog = SweetAlertDialog(this@OtpActivity, SweetAlertDialog.PROGRESS_TYPE)
                pDialog.progressHelper.barColor = Color.parseColor("#A5DC86")
                pDialog.titleText = "Loading"
                pDialog.setCancelable(false)



                checkFocus(binding.ipHp, binding.edHp)
                binding.btnSign.setOnClickListener {
                    phone = binding.edHp.text.toString()
                    if (phone.isNotEmpty()) {
                        if (phone.startsWith("0")) {
                            phone = "+62${phone.substring(1, phone.length)}"
                        } else if (phone.startsWith("8")) {
                            phone = "+628${phone.substring(1, phone.length)}"
                        }
//
                        pDialog.show()
                        lifecycleScope.launch {
                            viewModel.checkPhone(phone).asLiveData().observeForever {
                                when(it){
                                    is ApiResponse.Success->{
                                        Log.e("Succes", "onCreate: ${it.data}" )
                                        val intent = Intent(this@OtpActivity,OtpValidationActivity::class.java)
                                        intent.putExtra("hp",phone)
                                        intent.putExtra("data",it.data.data)
                                        startActivity(intent)
                                        showToastShort(this@OtpActivity,"sukses")
                                    }
                                    is ApiResponse.Error->{
                                        showToastShort(this@OtpActivity,it.errorMessage)
                                    }
                                    is ApiResponse.Empty->{
                                        showToastShort(this@OtpActivity,"Nomor Hanphone anda belum terdaftar ke sistem")
                                    }
                                    else -> {

                                    }
                                }
                            }
                            delay(1000)
                            pDialog.dismiss()
                        }


                    } else {
                        Toast.makeText(
                            this@OtpActivity,
                            "tolong masukan nomor handphone",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }








    }
}