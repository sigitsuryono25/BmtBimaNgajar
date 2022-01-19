package com.lauwba.bmtbima.network

import com.lauwba.bmtbima.model.PenilaianSent
import com.lauwba.bmtbima.model.User
import com.lauwba.bmtbima.response.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST("api/user/auth")
    suspend fun auth(@Body mUser: User): ResponseUser

    @GET("api/pengumuman/list")
    fun apipengumuman(): Call<ResponsePengumuman>

    @GET("api/user/jabatan")
    suspend fun getJabatan(): ResponseJabatan

    @GET("api/user/list")
    suspend fun getKaryawan(@Query("id_jabatan") idJabatan: String?): ResponseKaryawan

    @GET("api/penilaian/param")
    suspend fun getParam(@Query("id_jabatan") idJabatan: String?): ResponseParams

    @POST("api/penilaian/received")
    suspend fun sendPenilaian(@Body penilaianSent: PenilaianSent): GeneralResponse

    @GET("api/penilaian/get-penilaian")
    suspend fun getPenilaian(@Query("dinilai_oleh") dinilaiOleh: String?): ResponsePenilaian

    @GET("api/penilaian/get-penilaian-by-user")
    suspend fun getPenilaianByUser(@Query("id_karyawan") idKaryawan: String?) : ResponsePenilaian
}
