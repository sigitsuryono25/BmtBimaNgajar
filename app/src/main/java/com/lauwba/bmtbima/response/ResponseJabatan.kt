package com.lauwba.bmtbima.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResponseJabatan(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data_jabatan")
	val dataJabatan: List<DataJabatanItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null
) : Parcelable

@Parcelize
data class DataJabatanItem(

	@field:SerializedName("id_jabatan")
	val idJabatan: String? = null,

	@field:SerializedName("added_by")
	val addedBy: String? = null,

	@field:SerializedName("added_on")
	val addedOn: String? = null,

	@field:SerializedName("nama_jabatan")
	val namaJabatan: String? = null
) : Parcelable
