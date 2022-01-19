package com.lauwba.bmtbima.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lauwba.bmtbima.databinding.ItemAdapterPengumumanBinding

class AdapterPengumuman(private val onItemClick : (PengumumanItem)-> Unit) : RecyclerView.Adapter<AdapterPengumuman.ViewHolder>() {

    private val listPengumuman = mutableListOf<PengumumanItem>()

    inner class ViewHolder(private val itemAdapterPengumumanBinding: ItemAdapterPengumumanBinding) :
        RecyclerView.ViewHolder(itemAdapterPengumumanBinding.root) {

        fun onBindItem(get: PengumumanItem) {
            itemAdapterPengumumanBinding.judulPengumuman.text = get.judul
            itemAdapterPengumumanBinding.lampiran.text = get.lampiran
            itemAdapterPengumumanBinding.iconPengumuman.setImageResource(get.gambar!!)

            itemAdapterPengumumanBinding.root.setOnClickListener {
                onItemClick(get)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAdapterPengumumanBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBindItem(listPengumuman.get(position))
    }

    override fun getItemCount(): Int {
        return listPengumuman.size
    }

    fun addItem(listPengumuman: MutableList<PengumumanItem>) {
        this.listPengumuman.addAll(listPengumuman)
        notifyDataSetChanged()
    }
}