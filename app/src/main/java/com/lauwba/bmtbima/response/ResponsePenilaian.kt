package com.lauwba.bmtbima.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResponsePenilaian(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("data_penilaian")
	val dataPenilaian: List<DataPenilaianItem?>? = null
) : Parcelable

@Parcelize
data class PenilaianItem(

	@field:SerializedName("idBidang")
	val idBidang: String? = null,

	@field:SerializedName("bobotPoint")
	val bobotPoint: String? = null,

	@field:SerializedName("nilaiAkhirPerPoint")
	val nilaiAkhirPerPoint: String? = null,

	@field:SerializedName("nilaiDanPoint")
	val nilaiDanPoint: List<NilaiDanPointItem?>? = null
) : Parcelable

@Parcelize
data class DataPenilaianItem(

	@field:SerializedName("dinilai_pada")
	val dinilaiPada: String? = null,

	@field:SerializedName("penilaian")
	val penilaian: List<PenilaianItem?>? = null,

	@field:SerializedName("id_kar")
	val idKar: String? = null,

	@field:SerializedName("nama_kar")
	val namaKar: String? = null,

	@field:SerializedName("jabatan_kar")
	val jabatanKar: String? = null,

	@field:SerializedName("id_penilai")
	val idPenilai: String? = null,

	@field:SerializedName("nama_penilai")
	val namaPenilai: String? = null
) : Parcelable

@Parcelize
data class NilaiDanPointItem(

	@field:SerializedName("nilai")
	val nilai: Double? = null,

	@field:SerializedName("idPoint")
	val idPoint: String? = null,

	@field:SerializedName("giveScore")
	val giveScore: Double? = null
) : Parcelable
