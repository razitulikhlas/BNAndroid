package com.razitulikhlas.banknagari.ui.liquid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.razitulikhlas.banknagari.R
import com.razitulikhlas.banknagari.databinding.ActivityCalculatorBinding
import com.razitulikhlas.banknagari.databinding.ActivityLiquidInpBinding

class LiquidInpActivity : AppCompatActivity() {
    lateinit var binding : ActivityLiquidInpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLiquidInpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val jenis_agunan = arrayOf("Kendaraan Bermotor","Tanah","Bangunan","Rumah Susun/Apartemen/Flat/Unit Strata Title","Alat Berat","Mesin-mesin","Kapal","Pesawat Udara",
            "Peralatan Kerja","Tanaman Keras","Deposito diblokir","Tabungan diblokir","Giro diblokir","Emas","Term Proyek","Hak Pakai/Sewa Toko Milik Pemerintah","Hak atas sewa",
            "Piutan Dagang","Surat Berharga"
            )

        val adapter_jenis_agunan = ArrayAdapter(
            this,
            R.layout.dropdown_menu_popup_item,
            jenis_agunan
        )

        with(binding){
            edKriteriaAgunan.setAdapter(adapter_jenis_agunan)
        }
    }
}