package com.lauwba.bmtbima.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lauwba.bmtbima.R
import com.lauwba.bmtbima.databinding.ItemAdapterPengumumanBinding
import com.lauwba.bmtbima.response.DataPengumumanItem

class AdapterPengumumanAll(private var data : List<DataPengumumanItem>,private var context : Context,private var onclick :(DataPengumumanItem)->Unit)
    : RecyclerView.Adapter<AdapterPengumumanAll.ViewHolder>(){

    private var cont : Context? = null

    class ViewHolder(view :ItemAdapterPengumumanBinding):RecyclerView.ViewHolder(view.root) {
        val judul = view.judulPengumuman
        val img = view.iconPengumuman
        val lamp = view.lampiran
        val tanggal = view.tanggal
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterPengumumanAll.ViewHolder {

        val binding = ItemAdapterPengumumanBinding.inflate(
            LayoutInflater.from(parent.context)
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdapterPengumumanAll.ViewHolder, position: Int) {
        val dataItem = data.get(position)
        holder.judul.text = dataItem.judul
        cont.let {
            Glide.with(context).load(dataItem.cover).into(holder.img)
        }
        holder.tanggal.text = dataItem.addedOn

    }

    override fun getItemCount(): Int {
        return data.size
    }
}