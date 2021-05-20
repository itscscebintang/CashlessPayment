package com.teknikugm.dompetft.retrofit

import com.teknikugm.dompetft.pembayaran.ResponsePromo
import com.teknikugm.dompetft.pembayaran.Response_Detail
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.*

interface API {

    @POST("index.php/User/login")
    @FormUrlEncoded
    fun loginUser (
        @Field("username") username:String,
        @Field("password") password:String
    ): Call<ResponseLogin>

    @POST("appkantin/register.php")
    @FormUrlEncoded
    fun registeruser (@Field("username") username:String,
                      @Field("name") name:String,
                      @Field("password") password:String,
                      @Field("email") email:String,
                      @Field("nik") nik:String): Observable<String>

    @POST("index.php/User/saldo_c/{key}")
    fun getsaldo(
        @Path("key") key: String?): retrofit2.Call<ResponseSaldo>

    @FormUrlEncoded
    @POST("index.php/Transaksi_topup/send_topup")
    fun topUpSaldo(

        @Field("jumlah_topup") finalTopUp: Int,
        @Field("username") username: String?,
        @Field("random_topup") randomNumber: Int

    ) : Call<Response_Topup>

    @GET("index.php/Transaksi_promo/api_list_promo")
    fun ambil_promo() : Call<ResponsePromo>

    @FormUrlEncoded
    @POST("index.php/Transaksi_kantin/detail_transaksi")
    fun detailTransaksi(

        @Field("user") user: String?,
        @Field("total_bayar") totalBayar: Int?,
        @Field("total_asli") totalAsli: Int?,
        @Field("diskon") diskonDetail: Int?

    ) : Call<Response_Detail>
}