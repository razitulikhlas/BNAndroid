package com.razitulikhlas.banknagari.ui.login

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.razitulikhlas.banknagari.databinding.ActivityOtpValidationBinding
import com.razitulikhlas.banknagari.ui.dashboard.DashBoardOwnActivity
import com.razitulikhlas.banknagari.ui.disposisi.HomeDisposisiActivity
import com.razitulikhlas.core.data.source.remote.response.Data
import com.razitulikhlas.core.data.storage.DataStoreManager
import com.razitulikhlas.core.util.Constant.IS_ID_USER
import com.razitulikhlas.core.util.Constant.IS_KODE_CABANG
import com.razitulikhlas.core.util.Constant.IS_LEVEL
import com.razitulikhlas.core.util.Constant.IS_LOGIN
import com.razitulikhlas.core.util.Constant.IS_NAME
import com.razitulikhlas.core.util.Constant.IS_PHOTO
import com.razitulikhlas.core.util.showToastShort
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class OtpValidationActivity : AppCompatActivity() {
    private var phoneNumber: String? = null
    lateinit var binding : ActivityOtpValidationBinding
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var countDownTimer: CountDownTimer
    private lateinit var verificationIds: String
    private val valueTimer = 120000L
    private var isAutoVerify: Boolean = false
    private val startTimer = valueTimer



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpValidationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        supportActionBar?.elevation = 0f

        phoneNumber =intent.getStringExtra("hp")
        binding.tvPhone.text = phoneNumber
        startTimer()
        startPhoneNumberVerification()
        try {
            binding.otpView.setOtpCompletionListener {
                if (this::verificationIds.isInitialized) {
                    val phoneAuthCredential: PhoneAuthCredential =
                        PhoneAuthProvider.getCredential(verificationIds, it)
                    signInWithPhoneAuthCredential(phoneAuthCredential)
                } else {
                    showToastShort(this, "Kode yang anda masukan salah")
                }

            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

    private fun startPhoneNumberVerification() {
        verificationCallbacks()
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber!!,
            10,
            TimeUnit.SECONDS,
            this,
            callbacks
        )
    }

    private fun verificationCallbacks() {
        Log.e("TAG", "onVerificationCompletedssss:")

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                signInWithPhoneAuthCredential(credential)
                binding.otpView.setText(credential.smsCode)
                Log.e("TAG", "onVerificationCompleted: run")
            }

            override fun onVerificationFailed(e: FirebaseException) {
                isAutoVerify = false
                Toast.makeText(this@OtpValidationActivity, e.message.toString(), Toast.LENGTH_LONG).show()
                Log.e("TAG", "onfailed:" + e.message.toString())
            }

            override fun onCodeSent(
                verivicationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                super.onCodeSent(verivicationId, token)
                isAutoVerify = true
                verificationIds = verivicationId
                Log.e("TAG", "onCodesent: $verivicationId")
            }
        }
    }

    fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    lifecycleScope.launch{
                        val data = intent.getParcelableExtra<Data>("data")
                        DataStoreManager.saveValue(this@OtpValidationActivity, IS_LOGIN, true)
                        DataStoreManager.saveValue(this@OtpValidationActivity, IS_NAME, data?.name!!)
                        DataStoreManager.saveValue(this@OtpValidationActivity, IS_LEVEL, data.level!!.toInt())
                        if(data.photo != null){
                            DataStoreManager.saveValue(this@OtpValidationActivity, IS_PHOTO, data.photo!!)

                        }
                        DataStoreManager.saveValue(this@OtpValidationActivity, IS_ID_USER, data.nik!!)
                        DataStoreManager.saveValue(this@OtpValidationActivity, IS_KODE_CABANG, data.kodeCabang!!)

                        if(data.level!!.toInt() > 2){
                            val intent = Intent(this@OtpValidationActivity,HomeDisposisiActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()
                        }else{
                            val intent = Intent(this@OtpValidationActivity,DashBoardOwnActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()
                        }


                    }


//                    FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
//                        if (!task.isSuccessful) {
//                            Log.w("TAG", "Fetching FCM registration token failed", task.exception)
//                            return@OnCompleteListener
//                        }
//                        lifecycleScope.launch {
//                            DataStoreManager.saveValue(requireActivity(), FCM, task.result)
//                        }
//                        viewModel.login(phoneNumber!!, task.result, requireActivity())
//                        Log.e("TAG", "signInWithPhoneAuthCredential check1: " )
//                    })
                }
            }
            .addOnFailureListener(this) {
                Log.e("TAG", "signInWithPhoneAuthCredential: ${it.message}")
                Toast.makeText(
                    this,
                    "Kode otp yang anda masukan salah",
                    Toast.LENGTH_LONG
                ).show()
            }
    }

    private fun startTimer() {
        binding.tvResendCode.visibility = View.GONE
        countDownTimer = object : CountDownTimer(startTimer, 1000) {
            override fun onFinish() {
                binding.tvResendCode.visibility = View.VISIBLE
            }

            override fun onTick(millisUntilFinished: Long) {
                binding.tvTime.text = (millisUntilFinished / 1000).toString()
            }
        }.start()
    }
}