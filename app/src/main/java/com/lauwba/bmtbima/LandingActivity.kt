package com.lauwba.bmtbima

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lauwba.bmtbima.databinding.ActivityLandingBinding

    class LandingActivity : AppCompatActivity() {
        private lateinit var binding : ActivityLandingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLandingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .replace(R.id.container_fragment, HomeFragment()).commit()


        binding.bottomnav.setOnItemSelectedListener {

            when(it.itemId){
                R.id.menuhome ->{
//                    saat menu home diklkik, action -> fragmenthome\
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container_fragment, HomeFragment()).commit()
                    return@setOnItemSelectedListener true
                }
                R.id.menuakun ->{
//                    ngapain setelah diklik mwnuakun
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container_fragment, AccountFragment()).commit()
                    return@setOnItemSelectedListener true
                }
            }

            return@setOnItemSelectedListener false
        }

    }
}