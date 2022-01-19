package com.lauwba.bmtbima.pkp

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.lauwba.bmtbima.R
import com.lauwba.bmtbima.databinding.FragmentLevelBinding
import com.lauwba.bmtbima.network.NetworkModule
import com.lauwba.bmtbima.response.DataKaryawanItem
import com.lauwba.bmtbima.response.ResponseJabatan
import com.lauwba.bmtbima.response.ResponseKaryawan
import kotlinx.coroutines.*

class LevelFragment : Fragment(R.layout.fragment_level) {
    private lateinit var binding: FragmentLevelBinding
    private var dataKaryawanItem: DataKaryawanItem?= null
    private var idJabatan: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLevelBinding.bind(view)

        getJabatan()

        binding.next.setOnClickListener {
            if(dataKaryawanItem != null && idJabatan?.isNotEmpty() == true){
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.container, KaryawanFragment.newInstance(dataKaryawanItem, idJabatan.toString()))
                    .commit()
            }else{
                Toast.makeText(requireActivity(), "Pilih Level dan karyawan terlebih dahulu", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getJabatan() {
        //kita buat request ke server
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.IO) {
                try {
                    //ketika request berhasil dibuat dan menerima hasil response
                    val response = NetworkModule.getService().getJabatan()

                    //set ke UI nya
                    MainScope().launch {
                        setJabatan(response)
                    }
                } catch (e: Throwable) {
                    //tangkap error saat melakukan request (http code non 200/300)
                    e.printStackTrace()
                }
            }
        }
    }

    private fun setJabatan(response: ResponseJabatan) {
        val jabatanList = mutableListOf<String?>()
        val data = response.dataJabatan

        //looping untuk masing-masing item jabatan
        data?.forEach { jabatan ->
            //masukkan nama jabatan ke jabatanList
            //item pada jabatanList yang akan muncul ke dalam spinner/dropdownnya
            jabatanList.add(jabatan?.namaJabatan)
        }

        //set adapter to spinner
        val adapterSpinner =
            ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, jabatanList)
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_list_item_1)
        binding.spinnerLevel.adapter = adapterSpinner

        binding.spinnerLevel.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                //ambil data yang nama jabatanya sama persis dengan yang telah dipilih di spinner
                val filteredData = data?.filter {
                    it?.namaJabatan.toString() == binding.spinnerLevel.selectedItem.toString()
                }

//                Toast.makeText(
//                    requireActivity(),
//                    filteredData?.iterator()?.next()?.idJabatan,
//                    Toast.LENGTH_SHORT
//                ).show()
                idJabatan = filteredData?.iterator()?.next()?.idJabatan
                getKaryawan(filteredData?.iterator()?.next()?.idJabatan)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
    }

    private fun getKaryawan(idJabatan: String?) {
        //buat request ke server
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.IO){
                try{
                    val response = NetworkModule.getService().getKaryawan(idJabatan)
                    MainScope().launch {
                        setToAdapterKaryawan(response)
                    }
                }catch(e: Throwable){
                    e.printStackTrace()
                }
            }
        }
    }

    private fun setToAdapterKaryawan(response: ResponseKaryawan) {
        val karyawanList = mutableListOf<String?>()
        val data = response.dataKaryawan

        //looping data karyawan dari server
        data?.forEach {karyawan->
            karyawanList.add(karyawan?.nama)
        }

        val adapterKaryawan = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, karyawanList)
        adapterKaryawan.setDropDownViewResource(android.R.layout.simple_list_item_1)
        binding.spinnerKaryawan.adapter = adapterKaryawan


        binding.spinnerKaryawan.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val filteredData = data?.filter {
                    it?.nama.toString() == binding.spinnerKaryawan.selectedItem.toString()
                }
                dataKaryawanItem = filteredData?.iterator()?.next()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
    }
}