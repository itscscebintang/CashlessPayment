package com.teknikugm.dompetft.pembayaran

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class DataItem(

    @field:SerializedName("kode_promo")
    val kodePromo: String? = null,

    @field:SerializedName("id_promo")
    val idPromo: String? = null,

    @field:SerializedName("jumlah_promo")
    val jumlahPromo: String? = null,

    @field:SerializedName("min_belanja")
    val minBelanja: String? = null,

//    @field:SerializedName("status_promo")
//    val statusPromo: String? = null

    @field:SerializedName("persentase_promo")
    val persentasePromo: String? = null

) : Serializable