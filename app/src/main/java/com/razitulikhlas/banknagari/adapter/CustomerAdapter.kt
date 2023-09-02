package com.razitulikhlas.banknagari.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.razitulikhlas.banknagari.databinding.ItemCustomerBinding
import com.razitulikhlas.core.data.source.local.mapping.CustomerEntity
import com.razitulikhlas.core.util.Constant.TYPE_CALL_MAPPING
import com.razitulikhlas.core.util.Constant.TYPE_INFO_MAPPING

class CustomerAdapter : RecyclerView.Adapter<CustomerAdapter.ViewHolder>() {
    private var listCustomer = ArrayList<CustomerEntity>()
    private lateinit var context : Context
    private lateinit var customerCallback: CustomerCallback

    fun setCustomer(customer: List<CustomerEntity>?) {
        if (customer == null) return
        this.listCustomer.clear()
        this.listCustomer.addAll(customer)
    }

    fun setListener(customerCallback:CustomerCallback){
        this.customerCallback = customerCallback
    }

    class ViewHolder(binding: ItemCustomerBinding) : RecyclerView.ViewHolder(binding.root) {
        val bin = binding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val itemsCustomer = ItemCustomerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemsCustomer)
    }

    override fun getItemCount(): Int  = listCustomer.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val customer = listCustomer[position]
        with(holder.bin){
            tvName.text = customer.ownerName
            tvNameBusiness.text= customer.businessName
            bgKol.setOnClickListener {
                customerCallback.onClick(customer,TYPE_CALL_MAPPING)
            }
            root.setOnClickListener {
                customerCallback.onClick(customer,TYPE_INFO_MAPPING)
            }
            root.setOnLongClickListener {
                customerCallback.onLongClick(customer)
                return@setOnLongClickListener false
            }
        }
    }

    interface CustomerCallback {
        fun onClick(customer: CustomerEntity,type:Int)
        fun onLongClick(customer: CustomerEntity)
    }

}