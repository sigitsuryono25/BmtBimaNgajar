package com.lauwba.bmtbima

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lauwba.bmtbima.databinding.ActivityLoginBinding
import com.lauwba.bmtbima.model.User
import com.lauwba.bmtbima.network.NetworkModule
import com.lauwba.bmtbima.response.ResponseUser
import com.lauwba.bmtbima.utils.Constant
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    // ini digunakan untuk mengakses id-id yang ada didalam activity_login.xml
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (checkedLogin()) {
            Intent(this, LandingActivity::class.java).apply {
                startActivity(this)
            }
            finish()
        }

        //memasukkan activity_login.xml ke dalam kotlin nya
        binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)

        //kasih event klik ke tombol masuk
        binding.btnMasuk.setOnClickListener {

            val userid = binding.userId.text.toString() //ambil nilai dari edittext userid
            val password = binding.password.text.toString() //ambil nilai dari edittext password


            if (userid.isEmpty() || password.isEmpty()) {
                //action kalo userid dan password kosong
                Toast.makeText(this, "User ID dan password harus diisi", Toast.LENGTH_SHORT).show()
            } else {
                //action kalo userid dan password berisi
//                Intent untuk memindahkan dari 1 activity ke activity lain
//                Prefs.putString(Constant.USERID, userid)
//                Prefs.putString(Constant.PASSWORD, password)
//                startActivity(Intent(this, LandingActivity::class.java))

                val user = User()
                user.userid = userid
                user.password = password
                sendLoginData(user)

            }
        }
    }

    private fun checkedLogin(): Boolean {
        return Prefs.contains(Constant.USERID)
    }

    private fun sendLoginData(mUser: User) {
        CoroutineScope(Dispatchers.IO).launch {
            try{
                val res = NetworkModule.getService().auth(mUser)
                MainScope().launch {
                    updateUI(res)
                }
            }catch (t: Throwable){

            }

        }
    }

    private fun updateUI(res: ResponseUser) {
        if(res.code == 200){
            // user ditemukan diserver
            // kalo ditemukan, set session, lalu pindah ke halaman landingActivity

            Prefs.putString(Constant.USERID, res.dataUser?.userid)
            Prefs.putString(Constant.NAMA, res.dataUser?.nama)
            Prefs.putString(Constant.PASSWORD, res.dataUser?.password)
            Prefs.putString(Constant.JABATAN, res.dataUser?.jabatan)
            Prefs.putString(Constant.NAMA_JABATAN, res.dataUser?.namaJabatan)
            Prefs.putString(Constant.TERDAFTAR_PADA, res.dataUser?.createdAt)

            //pindah ke halaman landingActivity pakai Intent
            Intent(this, LandingActivity::class.java).apply {
                startActivity(this)
            }
            finishAffinity()
        }else{
            // user tiddak ditemukan
            // kalo tidak ditemukan, munculkan pesan pop up user tidak terdaftar
            // tidak ditemukan
            Toast.makeText(this, res.message, Toast.LENGTH_SHORT).show()
        }
    }
}