package com.lauwba.bmtbima.pengumuman

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.lauwba.bmtbima.DetailPengumuman
import com.lauwba.bmtbima.R
import com.lauwba.bmtbima.adapter.AdapterPengumumanAll
import com.lauwba.bmtbima.databinding.ActivityAllPengumumanBinding
import com.lauwba.bmtbima.network.NetworkModule
import com.lauwba.bmtbima.response.DataPengumumanItem
import com.lauwba.bmtbima.response.ResponsePengumuman
import retrofit2.Call
import retrofit2.Response

class AllPengumuman : AppCompatActivity() {

    private lateinit var binding: ActivityAllPengumumanBinding
    lateinit var adapterpengumuman :AdapterPengumumanAll

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAllPengumumanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showDataPengumuman()

    }

    private  fun showDataPengumuman(){
        NetworkModule.getService().apipengumuman()
            .enqueue(object : retrofit2.Callback<ResponsePengumuman>{
                override fun onResponse(
                    call: Call<ResponsePengumuman>,
                    response: Response<ResponsePengumuman>
                ) {
                    if (response.isSuccessful){
                        val dataItem = response.body()?.dataPengumuman
                        adapterpengumuman = AdapterPengumumanAll(dataItem as List<DataPengumumanItem>, this@AllPengumuman){


                        }
                        binding.rvpengumumanall.layoutManager = LinearLayoutManager(this@AllPengumuman)
                        binding.rvpengumumanall.adapter = adapterpengumuman
                    }else{
                        Toast.makeText(applicationContext, response.message(),Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponsePengumuman>, t: Throwable) {
                    Toast.makeText(applicationContext,t.message,Toast.LENGTH_SHORT).show()
                }

            })
    }
}