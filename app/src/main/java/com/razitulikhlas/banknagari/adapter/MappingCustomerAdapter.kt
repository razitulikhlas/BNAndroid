package com.razitulikhlas.banknagari.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.razitulikhlas.banknagari.databinding.ItemMapsBinding
import com.razitulikhlas.core.domain.mapping.model.DataMapCustomer

class MappingCustomerAdapter : RecyclerView.Adapter<MappingCustomerAdapter.ViewHolder>() {

    private lateinit var mappingCallback: MappingCallback

    fun setListener(mappingCallback: MappingCallback){
        this.mappingCallback = mappingCallback
    }

    fun setData(listCustomer: List<DataMapCustomer>){
        this.listCustomer.clear()
        this.listCustomer.addAll(listCustomer)
    }

    class ViewHolder(bind: ItemMapsBinding) :RecyclerView.ViewHolder(bind.root){
        val bin = bind

    }

    private var listCustomer = ArrayList<DataMapCustomer>()
    private lateinit var context : Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemMaps = ItemMapsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemMaps)
    }

    override fun getItemCount(): Int = listCustomer.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val customer: DataMapCustomer = listCustomer[position]
        with(holder.bin){
            tvName.text = customer.name
            tvPlatfond.text = customer.pladfond
            btnNav.setOnClickListener {
                mappingCallback.onClick(customer)
            }
        }
    }

    interface MappingCallback {
        fun onClick(customer: DataMapCustomer)
    }
}