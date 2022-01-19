package com.lauwba.bmtbima.monitoring

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.lauwba.bmtbima.R
import com.lauwba.bmtbima.databinding.FragmentMonitoringBinding
import com.lauwba.bmtbima.monitoring.adapter.AdapterDaftarPenilaian
import com.lauwba.bmtbima.network.NetworkModule
import com.lauwba.bmtbima.response.ResponsePenilaian
import com.lauwba.bmtbima.utils.Constant
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.coroutines.*

class MonitoringFragment : Fragment(R.layout.fragment_monitoring) {
    private lateinit var binding: FragmentMonitoringBinding
    private lateinit var adapterDaftarPenilaian: AdapterDaftarPenilaian


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentMonitoringBinding.bind(view)

        //cek level siapa yang lagi login
        if (Prefs.getString(Constant.JABATAN).equals("7", true)) {
            //yang sedang login adalah karyawan
            getPenilaianByUser(Prefs.getString(Constant.USERID))
        } else {
            //yang sedang login selain karyawan
            getPenilaian(Prefs.getString(Constant.USERID))

        }

        adapterDaftarPenilaian = AdapterDaftarPenilaian {
            Intent(requireActivity(), DetailPenilaianActivity::class.java).apply {
                putExtra("data_penilaian", it)

                startActivity(this)
            }
        }
        binding.daftarPenilaian.apply {
            adapter = adapterDaftarPenilaian
            layoutManager = LinearLayoutManager(requireActivity())
        }
    }

    private fun getPenilaian(userid: String?) {
        //function ini digunakan bila yang sedang login itu adalah selain karyawan
        //function ini akan menampilkan semua daftar karyawan yang pernah dinilai oleh user yang
        //sedang login
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.IO) {
                try {
                    val res = NetworkModule.getService().getPenilaian(userid)
                    MainScope().launch {
                        updateUI(res)
                    }
                } catch (t: Throwable) {
                    t.printStackTrace()
                }
            }
        }
    }

    private fun updateUI(res: ResponsePenilaian) {
        res.dataPenilaian?.let {
            adapterDaftarPenilaian.addData(it, true)
        }
    }

    private fun getPenilaianByUser(userid: String?) {
        //function ini digunakan bila yang sedang login itu ada karyawan
        //function ini akan menampilkan history penilaian
    }
}