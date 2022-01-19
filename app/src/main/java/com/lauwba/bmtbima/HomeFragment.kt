package com.lauwba.bmtbima

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.lauwba.bmtbima.adapter.AdapterPengumuman
import com.lauwba.bmtbima.adapter.AdapterPengumumanAll
import com.lauwba.bmtbima.adapter.PengumumanItem
import com.lauwba.bmtbima.databinding.FragmentHomeBinding
import com.lauwba.bmtbima.monitoring.MonitoringActivity
import com.lauwba.bmtbima.network.NetworkModule
import com.lauwba.bmtbima.pengumuman.AllPengumuman
import com.lauwba.bmtbima.pkp.PkpActivity
import com.lauwba.bmtbima.response.DataPengumumanItem
import com.lauwba.bmtbima.response.ResponsePengumuman
import retrofit2.Call
import retrofit2.Response

class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var adapterPengumuman: AdapterPengumuman
    lateinit var adapterpeng : AdapterPengumumanAll
    private lateinit var binding: FragmentHomeBinding

    //ini dipanggil ketika layout itu sudah tampil
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentHomeBinding.bind(view)



//        val listPengumuman = mutableListOf<PengumumanItem>()
//        val pengumumanItem1 = PengumumanItem()
//        pengumumanItem1.gambar = R.drawable.ic_home
//        pengumumanItem1.judul = "Contoh Judul Pengumuman"
//        pengumumanItem1.lampiran = "Contoh Lampiran Pengumuman"
//
//        listPengumuman.add(pengumumanItem1)
//
//        val pengumumanItem2 = PengumumanItem()
//        pengumumanItem2.gambar = R.drawable.ic_home
//        pengumumanItem2.judul = "Pengumuman 2"
//        pengumumanItem2.lampiran = "Contoh Pengumuman 2"
//
//        listPengumuman.add(pengumumanItem2)
//
//        adapterPengumuman = AdapterPengumuman(onItemClick = {
//            Intent(requireActivity(), DetailPengumuman::class.java).apply {
//                putExtra("item_pengumuman", it)
//                startActivity(this)
//            }
//        })
//        binding.rvPengumuman.adapter = adapterPengumuman
//        binding.rvPengumuman.layoutManager = LinearLayoutManager(requireActivity())
//
//        adapterPengumuman.addItem(listPengumuman)

        binding.pkp.setOnClickListener {
            Intent(requireActivity(), PkpActivity::class.java).apply {
                startActivity(this)
            }
        }

        binding.monitoring.setOnClickListener {
            Intent(requireActivity(), MonitoringActivity::class.java).apply {
                startActivity(this)
            }
        }
    }


}