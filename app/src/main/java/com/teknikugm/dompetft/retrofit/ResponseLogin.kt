package com.teknikugm.dompetft.retrofit

import com.google.gson.annotations.SerializedName

data class ResponseLogin(

    @field:SerializedName("code")
    val code: Int? = null,

    @field:SerializedName("data")
    val data: Data? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class Data(

    @field:SerializedName("username")
    val kodeUser: String? = null,

    @field:SerializedName("password")
    val passwordUser: String? = null,

    @field:SerializedName("name")
    val namaUser: String? = null,

//    @field:SerializedName("email")
//    val email: String? = null,
//
//    @field:SerializedName("nik")
//    val nik: String? = null,

    @field:SerializedName("saldo")
    val saldo: String? = null
)