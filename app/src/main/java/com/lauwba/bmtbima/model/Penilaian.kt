package com.lauwba.bmtbima.model

data class Penilaian(
    var nilaiDanPoint: MutableList<Nilai>? = null,
    var bobotPoint: String? = null,
    var nilaiAkhirPerPoint: String? = null,
    var idBidang: String? = null
)

data class Nilai(
    var idPoint: String? = null,
    var nilai: Double? = null,
    var giveScore: Double? = null
)

class PenilaianSent {
    var userid: String? = null
    var penilaian: List<Penilaian>? = null
    var dinilaiOleh: String? = null
}
