package com.lauwba.bmtbima.monitoring

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.text.InputType
import android.util.Log
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import com.lauwba.bmtbima.R
import com.lauwba.bmtbima.databinding.ActivityDetailPenilaianBinding
import com.lauwba.bmtbima.model.Nilai
import com.lauwba.bmtbima.model.Penilaian
import com.lauwba.bmtbima.model.PenilaianSent
import com.lauwba.bmtbima.network.NetworkModule
import com.lauwba.bmtbima.response.*
import com.lauwba.bmtbima.utils.Constant
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.coroutines.*

class DetailPenilaianActivity : AppCompatActivity() {
    private var dataPenilaianItem: List<PenilaianItem?>? = null
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
    private lateinit var binding: ActivityDetailPenilaianBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPenilaianBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val incomingData = intent.getParcelableExtra<DataPenilaianItem?>("data_penilaian")

        supportActionBar?.apply {
            if(Prefs.getString(Constant.JABATAN).equals("7", true)){
                title = "Detail Hasil Penilaian Anda"
            }else{
                title = "Detail Penilaian Karyawan"
            }
            setDisplayHomeAsUpEnabled(true)
            subtitle = incomingData?.namaKar?.trim()
        }

        dataPenilaianItem = incomingData?.penilaian

        getParams(incomingData?.jabatanKar)

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
//                    Toast.makeText(this@DetailPenilaianActivity, "isi semua kolom nilai sebelum lanjut", Toast.LENGTH_SHORT).show()
                    Toast.makeText(this@DetailPenilaianActivity, "isi semua kolom nilai sebelum lanjut", Toast.LENGTH_SHORT).show()

                    //return dibawah ini berfungsi agar kode selanjutnya tidak dieksekusi dan looping berhenti
                    return
                }


            }

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
                    Toast.makeText(this@DetailPenilaianActivity, "isi semua kolom sebelum lanjut", Toast.LENGTH_SHORT).show()
                    return
                }
            }

            finish()
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
            val tv = TextView(this@DetailPenilaianActivity)
            val l1 = LinearLayout(this@DetailPenilaianActivity)
            val l2 = LinearLayout(this@DetailPenilaianActivity)
            val bobotTextView = TextView(this@DetailPenilaianActivity)

            val editText = EditText(this@DetailPenilaianActivity)
            editText.layoutParams = lParams //untuk set widht dan height nya
            editText.setBackgroundColor(
                ActivityCompat.getColor(
                    this@DetailPenilaianActivity,
                    android.R.color.transparent
                )
            ) //untuk setbackground edittext
            editText.hint = "Tulis Score Disini" //untuk set hint atau place holder
            editText.inputType =
                InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL // untuk type keyboard yang muncul

            val filterBidang = dataPenilaianItem?.filter {
                it?.idBidang == dataParam?.get(initIndex)?.idBidang
            }

            val nilai = filterBidang?.get(0)?.nilaiDanPoint?.filter {
                it?.idPoint == pointItem?.idPoint
            }

            val nilaiFinal = nilai?.get(0)?.nilai
            editText.typeface = Typeface.DEFAULT_BOLD
            editText.gravity = GravityCompat.END
            editText.setText(String.format("Score yang diberikan: %.2f",
                nilaiFinal?.div(pointItem?.bobot?.toDouble()!!)))
            editText.isEnabled = false

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
                    this@DetailPenilaianActivity,
                    android.R.color.holo_red_light
                )
            ) //untuk set warna text
            bobotTextView.textSize = 16f //untuk ukuran font
            bobotTextView.typeface = Typeface.DEFAULT_BOLD //text style nya, Bold
            bobotTextView.gravity = GravityCompat.END //untuk gravity nya. END/RIGHT

            subPoint = pointItem?.subpoint

            pointItem?.subpoint?.forEachIndexed { index2, subpointItem ->
                val subPoint = TextView(this@DetailPenilaianActivity)
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
}