package com.razitulikhlas.banknagari.ui.infonaCustomer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.razitulikhlas.banknagari.databinding.ActivityInfoNasabahBinding

class InfoCustomerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val binding= ActivityInfoNasabahBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()
        supportActionBar?.elevation = 0f


    }
}