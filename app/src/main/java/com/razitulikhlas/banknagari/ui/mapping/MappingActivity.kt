package com.razitulikhlas.banknagari.ui.mapping

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.ContactsContract.Data
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.razitulikhlas.banknagari.R
import com.razitulikhlas.banknagari.adapter.CustomerAdapter
import com.razitulikhlas.banknagari.databinding.ActivityMappingBinding
import com.razitulikhlas.banknagari.viewmodel.CustomerViewModel
import com.razitulikhlas.core.data.source.local.mapping.CustomerEntity
import com.razitulikhlas.core.util.Constant.DATA_MAPPING
import com.razitulikhlas.core.util.Constant.TYPE_CALL_MAPPING
import com.razitulikhlas.core.util.Constant.TYPE_INFO_MAPPING
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class MappingActivity : AppCompatActivity(), CustomerAdapter.CustomerCallback {

    lateinit var binding : ActivityMappingBinding
    private val customerAdapter: CustomerAdapter by inject()
    private val viewModel : CustomerViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityMappingBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        customerAdapter.setListener(this)

        lifecycleScope.launch {
            viewModel.getCustomer().observeForever {
                Log.e("TAG", "onCreate: ${it}")
                customerAdapter.setCustomer(it)
                with(binding.rcv){
                    layoutManager = LinearLayoutManager(this@MappingActivity)
                    setHasFixedSize(true)
                    adapter = customerAdapter
                }
            }
        }

        with(binding){
            add.setOnClickListener {
                openAddActivity()
            }
        }


    }

    private fun openAddActivity() {
        val intent = Intent(this,MappingAddActivity::class.java)
        startActivity(intent)
    }

    override fun onClick(customer: CustomerEntity,type:Int) {
        if(type == TYPE_CALL_MAPPING){
            if(customer.phone != null){
                if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE),10)
                    return
                }else{
                    val uri = "tel:" + customer.phone?.trim()
                    val intent = Intent(Intent.ACTION_CALL)
                    intent.data = Uri.parse(uri)
                    startActivity(intent)
                }
            }
        }else if(type == TYPE_INFO_MAPPING){
            val intent = Intent(this,MappingDetailActivity::class.java)
            intent.putExtra(DATA_MAPPING,customer)
            startActivity(intent)
        }
    }

    override fun onLongClick(customer: CustomerEntity) {
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_layout)
        val delete = bottomSheetDialog.findViewById<LinearLayout>(R.id.delete)
        bottomSheetDialog.show()

        delete!!.setOnClickListener{
            lifecycleScope.launch{
                viewModel.delete(customer)
                Toast.makeText(applicationContext, "Delete is success", Toast.LENGTH_LONG).show()
                val result  = deleteContactNumber(customer)
                Log.e("TAG", "onLongClick: $result", )
                bottomSheetDialog.dismiss()
            }

        }
    }

    fun deleteContactNumber(customer: CustomerEntity):Int{
        val url = Data.CONTENT_URI
        val where = "${Data.MIMETYPE}='${ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE}' AND ${ContactsContract.CommonDataKinds.Phone.NUMBER} = '${customer.phone}'"
        return contentResolver.delete(url,where,null)
    }




}