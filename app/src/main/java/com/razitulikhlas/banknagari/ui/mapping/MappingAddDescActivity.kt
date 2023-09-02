package com.razitulikhlas.banknagari.ui.mapping

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.razitulikhlas.banknagari.databinding.ActivityMappingAddBinding
import com.razitulikhlas.banknagari.databinding.ActivityMappingAddDescBinding
import com.razitulikhlas.banknagari.viewmodel.CustomerViewModel
import com.razitulikhlas.core.data.source.local.mapping.CustomerDescMapEntity
import com.razitulikhlas.core.data.source.local.mapping.CustomerEntity
import com.razitulikhlas.core.util.Constant
import com.razitulikhlas.core.util.Constant.ID_MAPPING
import com.razitulikhlas.core.util.getCurrentDate
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MappingAddDescActivity : AppCompatActivity() {
    private val viewModel: CustomerViewModel by viewModel()
    private lateinit var binding: ActivityMappingAddDescBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMappingAddDescBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getLongExtra(ID_MAPPING,0)


            binding.btnSave.setOnClickListener {
                val desc = binding.desc.text

                if(desc.isNullOrBlank()){
                    Toast.makeText(this@MappingAddDescActivity,"Silahkan Tambahkan informasi kunjungan ",Toast.LENGTH_SHORT).show()
                }else{
                    lifecycleScope.launch {
                        val data = CustomerDescMapEntity(null,id, getCurrentDate(),desc.toString())
                        viewModel.saveDesc(data)
                        finish()
                    }
               }

        }

    }
}