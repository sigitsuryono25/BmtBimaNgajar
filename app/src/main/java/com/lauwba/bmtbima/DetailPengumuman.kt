package com.lauwba.bmtbima

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lauwba.bmtbima.adapter.PengumumanItem
import com.lauwba.bmtbima.databinding.ActivityDetailPengumumanBinding
import com.lauwba.bmtbima.response.DataPengumumanItem

class DetailPengumuman : AppCompatActivity() {

    private lateinit var binding: ActivityDetailPengumumanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailPengumumanBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }
}