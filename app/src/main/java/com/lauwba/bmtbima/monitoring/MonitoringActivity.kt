package com.lauwba.bmtbima.monitoring

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.lauwba.bmtbima.R
import com.lauwba.bmtbima.utils.Constant
import com.pixplicity.easyprefs.library.Prefs

class MonitoringActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monitoring)

        if (Prefs.getString(Constant.JABATAN).equals("7", true)) {
            //ini login sebagai karyawan
            changeFragment(MonitoringFragment(), "Hasil Penilaian Anda")
        } else {
            //ini login sebagai selain karyawan
            changeFragment(MonitoringFragment(), "Monitoring Hasil Penilaian")
        }
    }

    private fun changeFragment(fragment: Fragment, titleBar: String?) {
        //untuk mengganti halaman sekarang dengan fragment sesuai dengan permintaan
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()

        //change title
        supportActionBar?.apply {
            //untuk mengubah judul action/status bar
            title = titleBar
            //untuk mengaktifkan tombol back di bagian pojok kiri atas
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            //memberikan action untuk tombol back yang ada di pojok kiri atas
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}