package com.razitulikhlas.banknagari.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.razitulikhlas.banknagari.data.model.TableInstallment
import com.razitulikhlas.banknagari.databinding.ItemAnsuranBinding
import com.razitulikhlas.core.util.Constant.rupiah

class TableCalculatorAdapter:RecyclerView.Adapter<TableCalculatorAdapter.ViewHolder>() {

    private var list = ArrayList<TableInstallment>()

    fun setData(listDescCustomer: List<TableInstallment>){
        this.list.clear()
        this.list.addAll(listDescCustomer)
    }

    class ViewHolder(binding: ItemAnsuranBinding) : RecyclerView.ViewHolder(binding.root) {
        val bind = binding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val item= ItemAnsuranBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(item)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        with(holder.bind){
            tvNumber.text = "${position+1}"
            tvAp.text= rupiah(data.installmentPrimary)
            tvFlower.text = rupiah(data.bankInterest)
            tvJA.text=rupiah(data.installmentAmount)
            tvLoan.text=rupiah(data.remainingLoan)
        }
    }
}