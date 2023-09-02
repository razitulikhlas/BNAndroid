package com.razitulikhlas.banknagari.ui.calculator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.razitulikhlas.banknagari.adapter.TableCalculatorAdapter
import com.razitulikhlas.banknagari.databinding.ActivityTableCalculatorBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.NumberFormat
import java.util.*
import kotlin.math.roundToInt

class TableCalculatorActivity : AppCompatActivity() {
    lateinit var binding: ActivityTableCalculatorBinding
    private val viewModel: TableCalculatorViewModel by viewModel()
    private val adapterD: TableCalculatorAdapter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTableCalculatorBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        supportActionBar?.elevation = 0f
        setContentView(binding.root)

        val pf = intent.getDoubleExtra("PF", 0.0)
        val bg = intent.getDoubleExtra("BG", 0.0)
        val bl = intent.getIntExtra("BL", 0)
        val type = intent.getStringExtra("TYPE")

        val data = if(type == "Flat"){
            viewModel.flowerFlat(pf,bg,bl,"TAHUN")
        }else{
            viewModel.flowerAnu(pf,bg,bl)
        }

        adapterD.setData(data)
        binding.btnBack.setOnClickListener {
            finish()
        }
        binding.tvPf.text = rupiah(pf)
            with(binding.rcv) {
                layoutManager = LinearLayoutManager(this@TableCalculatorActivity)
                setHasFixedSize(true)
                adapter = adapterD
            }
    }

    private fun rupiah(number: Double): String{
        val localeID =  Locale("in", "ID")
        val numberFormat = NumberFormat.getCurrencyInstance(localeID)
        return numberFormat.format(number.roundToInt()).toString().replace("Rp","Rp ").dropLast(3)
    }


}