package com.razitulikhlas.banknagari.ui.disposisi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.razitulikhlas.banknagari.databinding.ActivityPhotoBusinessBinding
import com.razitulikhlas.core.BuildConfig

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
                Glide
                    .with(this@PhotoBusinessActivity)
                    .load(BuildConfig.BASE_URL_IMAGE +data)
                    .into(binding.pvImage);
            }
        }
    }
}