package com.teknikugm.dompetft.model

import com.google.gson.annotations.SerializedName

data class ResponseScan(

    @field:SerializedName("jumlah_pesanan")
    val jumlahPesanan: Int? = null,

    @field:SerializedName("daftar_menu")
    val daftarMenu: List<DaftarMenuItem?>? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("tanggal")
    val tanggal: String? = null,

    @field:SerializedName("penjual")
    val penjual: Int? = null
)

data class DaftarMenuItem(

    @field:SerializedName("daftar_menu")
    val daftarMenu: String? = null,

    @field:SerializedName("id")
    val id: Int? = null
)