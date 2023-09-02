package com.razitulikhlas.banknagari.ui.mapping

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.razitulikhlas.banknagari.R
import com.razitulikhlas.banknagari.adapter.MappingDescCustomer
import com.razitulikhlas.banknagari.databinding.ActivityMappingDetailBinding
import com.razitulikhlas.banknagari.viewmodel.CustomerViewModel
import com.razitulikhlas.core.data.source.local.mapping.CustomerEntity
import com.razitulikhlas.core.util.Constant.DATA_MAPPING
import com.razitulikhlas.core.util.Constant.ID_MAPPING
import org.koin.androidx.viewmodel.ext.android.viewModel


class MappingDetailActivity : AppCompatActivity() {

    lateinit var binding : ActivityMappingDetailBinding
    lateinit var adapter:MappingDescCustomer
    private val viewModel : CustomerViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMappingDetailBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()
        supportActionBar?.elevation = 0f
        adapter = MappingDescCustomer()

       val data= if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(DATA_MAPPING, CustomerEntity::class.java)
        } else {
            intent.getParcelableExtra<CustomerEntity>(DATA_MAPPING)
        }

        with(binding){

            tvOwnerName.text = data?.ownerName
            tvPhone.text = data?.phone
            tvNameBusiness.text = data?.businessName
            tvPladfond.text = data?.platFond
            tvBaki.text= data?.bakidebet
            tvResponseCustomer.text = data?.responseStatus
            tvLoanStatus.text = data?.loanStatus
            ivPhoto.setImageURI(data?.photo?.toUri())

            (rcvDesc.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            rcvDesc.layoutManager = LinearLayoutManager(this@MappingDetailActivity)


            viewModel.getCustomerDescMap(data?.id!!).observeForever {
                adapter.setData(it)
                rcvDesc.adapter = adapter
                rcvDesc.isNestedScrollingEnabled = false
                rcvDesc.hasFixedSize()
            }

            ivSetting.setOnClickListener {
                PopupMenu(this@MappingDetailActivity, view).apply {
                    setOnMenuItemClickListener(object: PopupMenu.OnMenuItemClickListener {
                        override fun onMenuItemClick(item: MenuItem?): Boolean {
                            return when (item?.itemId) {
                                R.id.mSetting-> {
                                    val intent = Intent(this@MappingDetailActivity,MappingAddDescActivity::class.java)
                                    intent.putExtra(ID_MAPPING,data.id)
                                    startActivity(intent)
                                    true
                                }
                                else -> false
                            }
                        }

                    })
                    inflate(R.menu.menu_mapping)
                    show()
                }
            }



            btnLocCustomer.setOnClickListener {
                val uri: Uri = Uri.parse("google.navigation:q=${data?.latitude},${data?.longitude}&mode=d")
                val intent = Intent(Intent.ACTION_VIEW, uri)
                intent.setPackage("com.google.android.apps.maps")
                startActivity(intent)
            }

            btnLocCall.setOnClickListener {
                if(data?.phone != null){
                    if(ActivityCompat.checkSelfPermission(this@MappingDetailActivity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(this@MappingDetailActivity, arrayOf(Manifest.permission.CALL_PHONE),10)
                        return@setOnClickListener
                    }else{
                        val uri = "tel:" + data.phone?.trim()
                        val intent = Intent(Intent.ACTION_CALL)
                        intent.data = Uri.parse(uri)
                        startActivity(intent)
                    }
                }
            }
        }

        Log.e("TAG", "onCreate: $data" )
    }
}