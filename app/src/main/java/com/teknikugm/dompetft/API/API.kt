package com.teknikugm.dompetft.API

import com.teknikugm.dompetft.model.*
import retrofit2.Call
import retrofit2.http.*

interface API {

    @GET(Constants.USER_URL)
    fun getProfile(): Call<DataUser>

    @POST(Constants.REGISTER_URL)
    fun addUser(@Body userInfo: RegisterRequest
    ): Call<ResponseRegister>

    @POST(Constants.LOGIN_URL)
    fun login(@Body request: LoginRequest
    ): Call<ResponseLogin>

    @GET(Constants.PROMO_URL)
    fun ambil_promo(): Call<List<ResponsePromo>>

    @FormUrlEncoded
    @POST(Constants.TOPUP_URL)
    fun topUpNEW(
        @Field("jumlah_topup") finalTopUp: Int,
        @Field("id_user") iduser: Int?
    ): Call<ResponseTopup>

    //kirim tipe transaksi
    @FormUrlEncoded
    @POST(Constants.TIPE_TRANSAKSI_URL)
    fun transaksi(
        @Field("tipe_transaksi") tipetrans: Int,
    ): Call<ResponseTransaksi>

    //transaksi transfer id == 2
    @FormUrlEncoded
    @POST(Constants.TRANSAKSI_TRANSFER_URL)
    fun transfer(
        @Field("jumlah_transfer") jumlahtransfer: Int,
        @Field("user_tujuan") usertujuan: Int?,
        @Path("id") id: Int,
    ): Call<TransferItem>

    //get data user
    @GET(Constants.FILTER_USER_URL)
    fun filteruser(): Call<List<FilterUser>>

    //transaksi bayar id == 1
    @FormUrlEncoded
    @POST(Constants.TRANSAKSI_BAYAR_URL)
    fun bayar(
        @Field("total_asli") totalasli: Int,
        @Field("total_bayar") totalbayar: Int?,
        @Field("id_penjual") idpenjual: Int?,
        @Field("diskon") diskon: Int?,
        @Path("id") id: Int,
    ): Call<BayarItem>

}