package com.lauwba.bmtbima.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResponseParams(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("data_param")
	val dataParam: List<DataParamItem?>? = null
) : Parcelable

@Parcelize
data class DataParamItem(

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("id_bidang")
	val idBidang: String? = null,

	@field:SerializedName("bobot_bidang")
	val bobotBidang: String? = null,

	@field:SerializedName("point")
	val point: List<PointItem?>? = null
) : Parcelable

@Parcelize
data class SubpointItem(

	@field:SerializedName("added_by")
	val addedBy: String? = null,

	@field:SerializedName("id_sub")
	val idSub: String? = null,

	@field:SerializedName("id_point")
	val idPoint: String? = null,

	@field:SerializedName("added_on")
	val addedOn: String? = null,

	@field:SerializedName("isi_sub_point")
	val isiSubPoint: String? = null
) : Parcelable

@Parcelize
data class PointItem(

	@field:SerializedName("isi_point")
	val isiPoint: String? = null,

	@field:SerializedName("bobot")
	val bobot: String? = null,

	@field:SerializedName("id_point")
	val idPoint: String? = null,

	@field:SerializedName("subpoint")
	val subpoint: List<SubpointItem?>? = null
) : Parcelable
