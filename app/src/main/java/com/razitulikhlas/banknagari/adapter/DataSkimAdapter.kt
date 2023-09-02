package com.razitulikhlas.banknagari.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.razitulikhlas.banknagari.databinding.ItemSubSkimBinding
import com.razitulikhlas.core.data.source.remote.response.DataItemSkim
import com.razitulikhlas.core.util.formatRupiah

class DataSkimAdapter : RecyclerView.Adapter<DataSkimAdapter.ViewHolder>() {

    private var listData = ArrayList<DataItemSkim>()
    private lateinit var dataCallback: DataSkimCallback

    fun setData(listData: List<DataItemSkim>) {
        this.listData.clear()
        this.listData.addAll(listData)
    }

    class ViewHolder(binding : ItemSubSkimBinding) : RecyclerView.ViewHolder(binding.root) {
        val bin = binding
    }

    fun setListener(dataCallback: DataSkimCallback){
        this.dataCallback = dataCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val items = ItemSubSkimBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(items)
    }

    override fun getItemCount(): Int = listData.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = listData[position]
            with(holder.bin){
                root.setOnClickListener {
                    dataCallback.onClick(item)
                }
                tvSkim.text = item.skimKredit
                tvDebitur.text = item.pemohon
                tvPlafond.text= formatRupiah(item.plafond!!.toDouble())
                tvTimeLoan.text= "${item.jangkaWaktu} bulan"
            }
    }


    interface DataSkimCallback {
        fun onClick(item:DataItemSkim)
    }
}