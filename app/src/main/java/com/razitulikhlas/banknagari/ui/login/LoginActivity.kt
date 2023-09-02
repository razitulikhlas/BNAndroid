package com.razitulikhlas.banknagari.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.razitulikhlas.banknagari.databinding.ActivityLoginBinding
import com.razitulikhlas.banknagari.ui.dashboard.DashboardActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activityLoginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(activityLoginBinding.root)
        supportActionBar?.hide()
        supportActionBar?.elevation = 0f

        activityLoginBinding.btnLogin.setOnClickListener {
            startActivity(Intent(this,DashboardActivity::class.java))
        }
    }


}