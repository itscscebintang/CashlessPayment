package com.teknikugm.dompetft.retrofit

import com.google.gson.annotations.SerializedName

data class ResponseSaldo(
    @field:SerializedName("saldo")
    val balance: Int? = null,

)

