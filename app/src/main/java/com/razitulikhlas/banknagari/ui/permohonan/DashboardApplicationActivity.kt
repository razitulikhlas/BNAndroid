package com.razitulikhlas.banknagari.ui.permohonan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.razitulikhlas.banknagari.databinding.ActivityDasboardPermohonanBinding

class DashboardApplicationActivity : AppCompatActivity() {
    lateinit var binding : ActivityDasboardPermohonanBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDasboardPermohonanBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}