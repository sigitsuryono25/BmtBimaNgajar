package com.lauwba.bmtbima.monitoring.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lauwba.bmtbima.databinding.ItemAdapterDaftarPenilaianBinding
import com.lauwba.bmtbima.response.DataPenilaianItem
import com.lauwba.bmtbima.utils.Constant
import com.pixplicity.easyprefs.library.Prefs

class AdapterDaftarPenilaian(private val onClick: (DataPenilaianItem?) -> Unit) :
    RecyclerView.Adapter<AdapterDaftarPenilaian.ViewHolder>() {
    private val listPenilaian = mutableListOf<DataPenilaianItem?>()

    inner class ViewHolder(val binding: ItemAdapterDaftarPenilaianBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBindItem(dataPenilaianItem: DataPenilaianItem?) {
            var total = 0.0
            val penilaianItem = dataPenilaianItem?.penilaian
            penilaianItem?.forEach {
                total = total.plus(it?.nilaiAkhirPerPoint?.replace(",", ".")?.toDouble()!!)
            }

            if (Prefs.getString(Constant.JABATAN).equals("7", true)) {
                binding.namaKaryawan.text =
                    "Dinilai oleh: " + dataPenilaianItem?.namaPenilai?.trim()
                binding.nilaiAkhir.text = String.format("Nilai akhir: %.2f", total)
                binding.dinilaiPada.text = dataPenilaianItem?.dinilaiPada?.trim()

                //trim() adalah fungsi untuk menghilangkan spasi diawal dan diakhir kata/karakter

            } else {
                binding.nilaiAkhir.text = String.format("Nilai akhir: %.2f", total)
                binding.namaKaryawan.text = dataPenilaianItem?.namaKar?.trim()
                binding.dinilaiPada.text = dataPenilaianItem?.dinilaiPada?.trim()
            }

            binding.root.setOnClickListener {
                onClick(dataPenilaianItem)
            }
        }
    }

    fun addData(list: List<DataPenilaianItem?>, clearAll: Boolean = false) {
        if (clearAll)
            listPenilaian.removeAll(listPenilaian)

        listPenilaian.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemAdapterDaftarPenilaianBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBindItem(listPenilaian.get(position))
    }

    override fun getItemCount(): Int {
        return listPenilaian.size
    }
}