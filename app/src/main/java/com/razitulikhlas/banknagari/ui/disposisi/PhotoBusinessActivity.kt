package com.razitulikhlas.banknagari.ui.disposisi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.razitulikhlas.banknagari.databinding.ActivityPhotoBusinessBinding

class PhotoBusinessActivity : AppCompatActivity() {
    lateinit var binding : ActivityPhotoBusinessBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityPhotoBusinessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = intent.getStringExtra("image")

        with(binding){
            ivBack.setOnClickListener {
                finish()
            }
            if(data != null){
                pvImage.setImageURI(data.toUri())
            }
        }
    }
}