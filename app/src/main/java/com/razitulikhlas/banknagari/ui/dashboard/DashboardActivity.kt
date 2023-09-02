package com.razitulikhlas.banknagari.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.razitulikhlas.banknagari.adapter.CustomerAdapter
import com.razitulikhlas.banknagari.databinding.ActivityDashboardBinding
import com.razitulikhlas.banknagari.ui.calculator.CalculatorActivity
import com.razitulikhlas.banknagari.ui.mapping.MappingActivity
import com.razitulikhlas.banknagari.ui.permohonan.InsertApplicationActivity
import com.razitulikhlas.banknagari.ui.petaCustomer.MapsActivity
import com.razitulikhlas.core.data.source.local.mapping.CustomerEntity
import com.razitulikhlas.core.data.source.remote.network.ApiResponse
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class DashboardActivity : AppCompatActivity(), CustomerAdapter.CustomerCallback {
    private val viewModel : DashBoardViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding= ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        supportActionBar?.elevation = 0f

        with(binding){
            ivMapping.setOnClickListener {
                openMapping()
            }

            ivPetaCustomer.setOnClickListener {
                startActivity(Intent(this@DashboardActivity,MapsActivity::class.java))
            }
            ivCalculator.setOnClickListener {
                startActivity(Intent(this@DashboardActivity,CalculatorActivity::class.java))
            }

            ivPermohonan.setOnClickListener {
                startActivity(Intent(this@DashboardActivity,InsertApplicationActivity::class.java))
            }




        }

        lifecycleScope.launch {
            viewModel.getData().asLiveData().observeForever {
                when(it){
                    is ApiResponse.Success->{
                        Log.e("TAG", "onCreate: ${it.data.data?.get(0)?.jumlah}" )
                    }
                    is ApiResponse.Error->{

                    }
                    else -> {}
                }
            }
        }

        val customerAdapter = CustomerAdapter()
        val nasabahPotensialAdapter = CustomerAdapter()


//        with(binding.rcvNb) {
//            nasabahAdapter.setNasabah(DataNasabah.generateDummyNasabah(),"nb")
//            nasabahAdapter.setListener(this@DashboardActivity)
//            layoutManager = LinearLayoutManager(this@DashboardActivity)
//            setHasFixedSize(true)
//            adapter = nasabahAdapter
//        }
//
//        with(binding.rcvNbPotensial) {
//            nasabahPotensialAdapter.setNasabah(DataNasabah.generateDummyNasabahPotensial(),"nb")
//            nasabahPotensialAdapter.setListener(this@DashboardActivity)
//            layoutManager = LinearLayoutManager(this@DashboardActivity)
//            setHasFixedSize(true)
//            adapter = nasabahAdapter
//        }

    }

    private fun openMapping() {
        val intent = Intent(this,MappingActivity::class.java)
        startActivity(intent)
    }

    override fun onClick(customer: CustomerEntity, type: Int) {
        TODO("Not yet implemented")
    }

    override fun onLongClick(customer: CustomerEntity) {
        TODO("Not yet implemented")
    }


}