package com.teknikugm.dompetft.retrofit

import com.google.gson.annotations.SerializedName

data class Response_Topup (
    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("success")
    val success: Boolean? = null

)