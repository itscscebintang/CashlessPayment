package com.teknikugm.dompetft.contoh

import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface APIsignup {

    @POST("appkantin/register.php")
    @FormUrlEncoded
    fun registeruser (@Field("username") username:String,
                      @Field("name") name:String,
                      @Field("password") password:String): Observable<String>
}