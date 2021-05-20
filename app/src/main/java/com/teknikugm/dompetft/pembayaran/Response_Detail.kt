package com.teknikugm.dompetft.pembayaran

import com.google.gson.annotations.SerializedName

data class Response_Detail (

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("success")
    val success: Boolean? = null

)
