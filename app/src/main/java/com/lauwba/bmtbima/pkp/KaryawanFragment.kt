package com.lauwba.bmtbima.pkp

import android.app.AlertDialog
import android.app.ProgressDialog
import android.graphics.Typeface
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.lauwba.bmtbima.R
import com.lauwba.bmtbima.databinding.FragmentKaryawanBinding
import com.lauwba.bmtbima.model.Nilai
import com.lauwba.bmtbima.model.Penilaian
import com.lauwba.bmtbima.model.PenilaianSent
import com.lauwba.bmtbima.network.NetworkModule
import com.lauwba.bmtbima.response.*
import com.lauwba.bmtbima.utils.Constant
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.coroutines.*
import retrofit2.HttpException

private const val ARG_PARAM1 = "dataKaryawan"
private const val ARG_PARAM2 = "idJabatan"


class KaryawanFragment : Fragment(R.layout.fragment_karyawan) {
    private var param1: DataKaryawanItem? = null
    private var param2: String? = null
    private lateinit var binding: FragmentKaryawanBinding
    private var initIndex = 0
    private var bobotList = mutableListOf<String?>()
    private var idPoint = mutableListOf<String?>()
    private var bobotBidang: String? = null
    private var idBidang: String? = null
    private var namaBidang: String? = null
    private var subPoint: List<SubpointItem?>? = listOf()
    private var dataParam: List<DataParamItem?>? = null
    private var pointItem: List<PointItem?>? = listOf()
    private var penilaianList = mutableListOf<Penilaian>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getParcelable(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentKaryawanBinding.bind(view)


        getParams(param2)

        binding.lanjut.setOnClickListener {
            getNextValues()
        }
    }

    private fun getNextValues() {
        initIndex++ //ini untuk ambil value dari index selanjutnya.

        if (initIndex < this.dataParam?.size!!) {
            val nilaiSementara = mutableListOf<Nilai>()
            val layoutPenilaian = binding.containerPenilaian
            val nilaiPerbidang = mutableListOf<Double>()
            val givenScore = mutableListOf<Double>()
            val editTextPenilaian = mutableListOf<EditText>()
            val nilaiDanPoint = hashMapOf<String?, Double>()

            //kita looping untuk edittext nya
            for (i in 0 until layoutPenilaian.childCount) {
                // check dulu apakah elemen yang ada di dalam layout penilaian itu edittext atau bukan
                if (layoutPenilaian.getChildAt(i) is EditText) {
                    //jika edittext, maka tambahkan ke dalam variable edittextPenilaian
                    editTextPenilaian.add(layoutPenilaian.getChildAt(i) as EditText)
                }
            }

            var jumlah = 0.0

            //kita looping edittext tadi untuk ambil value yang sudah di inputkan
            editTextPenilaian.forEachIndexed { index, editText ->
                //check dulu, apakah edittext itu ada nilainya atau tidak
                if(editText.text.isEmpty()){
                    //kalo belum diinput, tampilkan pesan toast dibawah ini
//                    Toast.makeText(requireActivity(), "isi semua kolom nilai sebelum lanjut", Toast.LENGTH_SHORT).show()
                    Toast.makeText(requireActivity(), "isi semua kolom nilai sebelum lanjut", Toast.LENGTH_SHORT).show()

                    //return dibawah ini berfungsi agar kode selanjutnya tidak dieksekusi dan looping berhenti
                    return
                }

                val nilai = editText.text.toString().toDouble() // ambil nilai dari masing-masing edittext dan convert ke double
                val bobot = bobotList[index].toString().toDouble() //ambil nilai bobot berdasarkan point penilaian dan convert ke double
                val nilaiAkhir = nilai.times(bobot) //kalikan nilai dengan bobot untuk dapatkan nilai perbidang
                nilaiPerbidang.add(nilaiAkhir) //tambahkan nilai akhir tadi ke mutablelist nilai perbidang
                givenScore.add(editText.text.toString().toDouble()) //tambahkan juga value dari edittext tadi ke mutablelist given score

                jumlah += nilaiAkhir
            }

            idPoint.forEachIndexed { index, s ->
                nilaiDanPoint[s] = nilaiPerbidang.get(index)
                val nilai = Nilai()
                nilai.idPoint = s
                nilai.nilai = nilaiPerbidang[index]
                nilai.giveScore = givenScore[index]

                nilaiSementara.add(nilai)
            }

            //semuanya disimpan kedalam class Penilaian yang mana setiap data yang telah diinputkan
            //akan simpan lagi kedalam list sehingga kita dapat mengirimkannya sekaligus ke server
            val penilaian = Penilaian()
            penilaian.nilaiDanPoint = nilaiSementara
            penilaian.bobotPoint = bobotBidang

            //string format digunakan untuk memformat string sesuai dengan keinginan
            //dibawah ini, string dalam hal ini jumlah, akan di format menjadi desimal dengan 2 angka
            //dibelakang koma
            penilaian.nilaiAkhirPerPoint = String.format("%.2f", jumlah.times(bobotBidang.toString().toDouble()))
            penilaian.idBidang = idBidang

            penilaianList.add(penilaian)

            //ambil/tampilkan parameter penilaian selanjutnya
            getPoint(initIndex)

        } else {
            val nilaiSementara = mutableListOf<Nilai>()
            val layoutPenilaian = binding.containerPenilaian
            val nilaiPerbidang = mutableListOf<Double>()
            val edittextPenilaian = mutableListOf<EditText>()
            val givenScore = mutableListOf<Double>()
            val nilaiDanPoint = hashMapOf<String?, Double>()

            for (i in 0 until layoutPenilaian.childCount){
                if(layoutPenilaian.getChildAt(i) is EditText)
                    edittextPenilaian.add(layoutPenilaian.getChildAt(i) as EditText)
            }

            var jumlah = 0.0
            edittextPenilaian.forEachIndexed { index, editText ->
                if(editText.text.isEmpty()){
                    Toast.makeText(requireActivity(), "isi semua kolom sebelum lanjut", Toast.LENGTH_SHORT).show()
                    return
                }

                val nilai = editText.text.toString().toDouble()
                val bobot = bobotList[index].toString().toDouble()
                val nilaiAkhir = nilai.times(bobot)
                nilaiPerbidang.add(nilaiAkhir)
                givenScore.add(editText.text.toString().toDouble())

                jumlah += nilaiAkhir
            }

            idPoint.forEachIndexed { index, s ->
                nilaiDanPoint[s] = nilaiPerbidang[index]
                val nilai = Nilai()
                nilai.idPoint = s
                nilai.nilai = nilaiPerbidang[index]
                nilai.giveScore = givenScore[index]

                nilaiSementara.add(nilai)
            }

            val penilaian = Penilaian()
            penilaian.nilaiDanPoint = nilaiSementara
            penilaian.bobotPoint = bobotBidang
            penilaian.nilaiAkhirPerPoint = String.format("%.2f", jumlah.times(bobotBidang.toString().toDouble()))
            penilaian.idBidang = idBidang
            penilaianList.add(penilaian)


            //prepare data to send into server
            val penilaianSent = PenilaianSent()
            penilaianSent.userid = param1?.userid
            penilaianSent.penilaian = penilaianList
            penilaianSent.dinilaiOleh = Prefs.getString(Constant.USERID)

            sendPenilaian(penilaianSent)
        }
    }

    private fun sendPenilaian(penilaianSent: PenilaianSent) {
        val progressDialog = ProgressDialog.show(requireActivity(), "", "mengirimkan data..", true, true)
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.IO){
                try {
                    val response = NetworkModule.getService().sendPenilaian(penilaianSent)
                    if(response.code == 200){
                        //pengiriman data penilaian berhasil
                        MainScope().launch {

                            //hilangkan progress dialog
                            progressDialog.dismiss()

                            //tampilkan informasi/response dari server bahwa pengiriman data berhasil
                            //dilakukan
                            AlertDialog.Builder(requireActivity())
                                .setMessage(response.message)
                                .setTitle("Info")
                                .setPositiveButton("Tutup"){d,i->
                                    requireActivity().finish()
                                }
                                .create().show()
                        }
                    }
                }catch (e: Throwable){
                    e.printStackTrace()
                    //pengiriman data penilaian gagal
                    var mesg : String? = ""
                    when(e){
                        is HttpException->{
                            if(e.code() == 404){
                                mesg = e.message()
                            }else if(e.code() == 500){
                                mesg = e.message()
                            }
                        }
                        else-> mesg = e.message
                    }

                    MainScope().launch {
                        Toast.makeText(requireActivity(), mesg.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun getParams(param2: String?) {
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.IO) {
                try {
                    val response = NetworkModule.getService().getParam(param2)
                    MainScope().launch {
                        updateUI(response)
                    }
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun updateUI(response: ResponseParams) {
        dataParam = response.dataParam
        getPoint(initIndex)
    }

    private fun getPoint(index: Int) {
        bobotList = mutableListOf()
        idPoint = mutableListOf()
        subPoint = listOf()
        pointItem = listOf()

        val judul = dataParam?.get(index)?.nama
        bobotBidang = dataParam?.get(index)?.bobotBidang
        idBidang = dataParam?.get(index)?.idBidang
        namaBidang = judul
        binding.bidang.text = judul
        binding.bobotBidang.text = "Bobot bidang: $bobotBidang"
        val point = dataParam?.get(index)?.point
        val lParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        val charA = 96 //kode ASCII untuk huruf a kecil

        binding.containerPenilaian.removeAllViews()
        point?.forEachIndexed { index, pointItem ->
            val urutan = charA.plus(index.plus(1)).toChar()
            val tv = TextView(requireActivity())
            val l1 = LinearLayout(requireActivity())
            val l2 = LinearLayout(requireActivity())
            val bobotTextView = TextView(requireActivity())

            val editText = EditText(requireActivity())
            editText.layoutParams = lParams //untuk set widht dan height nya
            editText.setBackgroundColor(
                ActivityCompat.getColor(
                    requireActivity(),
                    android.R.color.transparent
                )
            ) //untuk setbackground edittext
            editText.hint = "Tulis Score Disini" //untuk set hint atau place holder
            editText.inputType =
                InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL // untuk type keyboard yang muncul

            l1.orientation = LinearLayout.VERTICAL
            l2.orientation = LinearLayout.VERTICAL

            tv.text = "$urutan. ${pointItem?.isiPoint}"
            tv.layoutParams = lParams
            tv.setPadding(8, 8, 8, 8)

            bobotList.add(pointItem?.bobot)
            idPoint.add(pointItem?.idPoint)

            bobotTextView.text = "Bobot: ${pointItem?.bobot}" //untuk set text nya
            bobotTextView.setTextColor(
                ActivityCompat.getColor(
                    requireActivity(),
                    android.R.color.holo_red_light
                )
            ) //untuk set warna text
            bobotTextView.textSize = 16f //untuk ukuran font
            bobotTextView.typeface = Typeface.DEFAULT_BOLD //text style nya, Bold
            bobotTextView.gravity = GravityCompat.END //untuk gravity nya. END/RIGHT

            subPoint = pointItem?.subpoint

            pointItem?.subpoint?.forEachIndexed { index2, subpointItem ->
                val subPoint = TextView(requireActivity())
                subPoint.setPadding(50, 0, 0, 0)
                subPoint.text = "$urutan.${index2.plus(1)}. ${subpointItem?.isiSubPoint}"
                subPoint.layoutParams = lParams
                l2.layoutParams = lParams
                l2.addView(subPoint, -1)
            }

            l1.addView(tv, -1)
            l1.addView(l2, -1)

            val containerParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            containerParams.setMargins(0, 20, 0, 0)
            l1.layoutParams = containerParams
            binding.containerPenilaian.addView(
                l1,
                -1
            ) //tambahan linear layout l1 ke container penilaian
            binding.containerPenilaian.addView(
                editText,
                -1
            ) //tambahkan edittext nilai ke container penilaiann
            binding.containerPenilaian.addView(
                bobotTextView,
                -1
            ) //tambahkan bobot textview ke container penilaian

        }
    }


    companion object {
        @JvmStatic

        //untuk menerima data pada saat halaman fragment ini dipanggil
        fun newInstance(dataKaryawan: DataKaryawanItem?, idJabatan: String) =
            KaryawanFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM1, dataKaryawan)
                    putString(ARG_PARAM2, idJabatan)
                }
            }
    }
}