package com.teknikugm.dompetft.model

import com.google.gson.annotations.SerializedName

data class FilterUser(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("saldo")
	val saldo: Int? = null,

	@field:SerializedName("username")
	val username: String? = null
)
