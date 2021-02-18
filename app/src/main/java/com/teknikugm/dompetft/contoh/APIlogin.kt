package com.teknikugm.dompetft.contoh

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface APIlogin {

    @POST("index.php/User/login")
    @FormUrlEncoded
    fun loginUser (@Field("username") username:String,
                   @Field("password") password:String): Call<ResponseLoginContoh>

}