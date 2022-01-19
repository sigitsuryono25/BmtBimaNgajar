package com.lauwba.bmtbima.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ResponsePengumuman(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("data_pengumuman")
	val dataPengumuman: List<DataPengumumanItem?>? = null
)

data class DataPengumumanItem(

	@field:SerializedName("cover")
	val cover: String? = null,

	@field:SerializedName("keterangan")
	val keterangan: String? = null,

	@field:SerializedName("file")
	val file: List<String?>? = null,

	@field:SerializedName("judul")
	val judul: String? = null,

	@field:SerializedName("added_on")
	val addedOn: String? = null
):Serializable
