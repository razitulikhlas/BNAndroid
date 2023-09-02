package com.razitulikhlas.banknagari.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.razitulikhlas.banknagari.databinding.ItemDescBinding
import com.razitulikhlas.core.data.source.local.mapping.CustomerDescMapEntity

class MappingDescCustomer : RecyclerView.Adapter<MappingDescCustomer.ViewHolder>() {

    private var listDescCustomer = ArrayList<CustomerDescMapEntity>()

    fun setData(listDescCustomer: List<CustomerDescMapEntity>){
        this.listDescCustomer.clear()
        this.listDescCustomer.addAll(listDescCustomer)
    }

    class ViewHolder(private val bind: ItemDescBinding) : RecyclerView.ViewHolder(bind.root) {
        fun bind(customer: CustomerDescMapEntity) {
            val expanded: Boolean = customer.expanded
            with(bind){
                subItem.visibility = if (expanded) View.VISIBLE else View.GONE
                itemDate.text = customer.date
                subItemGenre.text = customer.desc
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemDescBinding = ItemDescBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemDescBinding)
    }

    override fun getItemCount(): Int = listDescCustomer.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val customer: CustomerDescMapEntity = listDescCustomer.get(position)
        holder.bind(customer)
        holder.itemView.setOnClickListener { v ->
            val expanded: Boolean = customer.expanded
            customer.expanded= !expanded
            notifyItemChanged(position)
        }
    }
}