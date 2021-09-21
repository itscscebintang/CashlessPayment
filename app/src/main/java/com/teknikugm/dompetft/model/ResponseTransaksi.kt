package com.teknikugm.dompetft.model

import com.google.gson.annotations.SerializedName

data class ResponseTransaksi(

	@field:SerializedName("transfer")
	val transfer: List<Any?>? = null,

	@field:SerializedName("bayar")
	val bayar: List<BayarItem?>? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("tanggal")
	val tanggal: String? = null,

	@field:SerializedName("user")
	val user: String? = null,

	@field:SerializedName("tipe_transaksi")
	val tipeTransaksi: Int? = null
)

data class TransferItem(

	@field:SerializedName("user_tujuan")
	val userTujuan: Int? = null,

	@field:SerializedName("jumlah_transfer")
	val jumlahTransfer: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null
)

data class BayarItem(

	@field:SerializedName("total_asli")
	val totalAsli: Int? = null,

	@field:SerializedName("id_penjual")
	val idPenjual: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("tanggal")
	val tanggal: String? = null,

	@field:SerializedName("total_bayar")
	val totalBayar: Int? = null,

	@field:SerializedName("diskon")
	val diskon: Any? = null,

	@field:SerializedName("makanan")
	val makanan: List<Any?>? = null
)
