package com.razitulikhlas.banknagari.ui.survey

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.razitulikhlas.banknagari.databinding.ActivityInsertSurveyBinding

class InsertSurveyActivity : AppCompatActivity() {
    private lateinit var binding : ActivityInsertSurveyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInsertSurveyBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}