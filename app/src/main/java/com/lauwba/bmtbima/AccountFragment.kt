package com.lauwba.bmtbima

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lauwba.bmtbima.databinding.FragmentAccountBinding
import com.lauwba.bmtbima.utils.Constant
import com.pixplicity.easyprefs.library.Prefs

class AccountFragment : Fragment(R.layout.fragment_account){
    private lateinit var binding: FragmentAccountBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentAccountBinding.bind(view)

        binding.namauser.text = Prefs.getString(Constant.NAMA)
        binding.userid.text = Prefs.getString(Constant.USERID)

        binding.logout.setOnClickListener {
            Prefs.clear()
            Intent(requireActivity(), LoginActivity::class.java).apply {
                startActivity(this)
            }
            requireActivity().finishAffinity()
        }
    }

}