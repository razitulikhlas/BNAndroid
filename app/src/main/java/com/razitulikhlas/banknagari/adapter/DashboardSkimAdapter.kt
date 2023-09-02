package com.razitulikhlas.banknagari.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.razitulikhlas.banknagari.databinding.ItemSkimBinding
import com.razitulikhlas.core.data.source.remote.response.DataItem
import com.razitulikhlas.core.util.formatRupiah


class DashboardSkimAdapter: RecyclerView.Adapter<DashboardSkimAdapter.ViewHolder>()  {
    private var listCustomer = ArrayList<DataItem>()
    private lateinit var dashboardCallback: DashboardSkimCallback

    val color = arrayOf("#DD1E5F","#8E24AA","#E64A19","#C2185B","#4808BA","#BF0606"
    )
    class ViewHolder(binding : ItemSkimBinding) : RecyclerView.ViewHolder(binding.root) {
        val bin=binding
    }

    fun setListener(dashboardCallback: DashboardSkimCallback){
        this.dashboardCallback = dashboardCallback
    }

    fun setData(listCustomer: List<DataItem>) {
        this.listCustomer.clear()
        this.listCustomer.addAll(listCustomer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val items = ItemSkimBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(items)
    }

    override fun getItemCount(): Int = listCustomer.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listCustomer[position]
        with(holder.bin){
            root.setOnClickListener {
                dashboardCallback.onClick(item.skimKredit!!)
            }
            tvSkim.text = item.skimKredit
            tvPlafond.text= formatRupiah(item.totalPlafond!!.toDouble())
            tvJumlah.text= "${item.jumlah} pemohon"
            holder.bin.parent.backgroundTintList= ColorStateList.valueOf(Color.parseColor(color[position]))
        }
    }


    interface DashboardSkimCallback {
        fun onClick(skim:String)
    }


}