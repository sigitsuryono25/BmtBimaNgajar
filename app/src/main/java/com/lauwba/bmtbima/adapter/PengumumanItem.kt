package com.lauwba.bmtbima.adapter

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PengumumanItem(
    var judul: String? = null,
    var lampiran: String? = null,
    var gambar: Int? = null
) : Parcelable
