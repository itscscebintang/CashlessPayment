package com.teknikugm.dompetft.model

import com.google.gson.annotations.SerializedName

data class ResponseTopup(

    @field:SerializedName("confirm")
    val confirm: Boolean? = null,

    @field:SerializedName("jumlah_topup")
    val jumlahTopup: Int? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("tanggal")
    val tanggal: String? = null,

    @field:SerializedName("id_user")
    val idUser: Int? = null,

    @field:SerializedName("random_topup")
    val randomTopup: String? = null
)