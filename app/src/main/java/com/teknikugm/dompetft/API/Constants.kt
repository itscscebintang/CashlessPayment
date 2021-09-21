package com.teknikugm.dompetft.API

import com.teknikugm.dompetft.utama.MainActivity

object Constants {
    const val BASE_URL = "https://api.dompetft.xyz/api/"
    const val LOGIN_URL = "rest-auth/login/"
    const val REGISTER_URL = "rest-auth/registration"
    const val USER_URL = "rest-auth/user/"
    const val PROMO_URL = "aktif/?format=json"
    const val TOPUP_URL = "saldo/?format=json"
    const val TIPE_TRANSAKSI_URL = "transaksi/"
    const val TRANSAKSI_TRANSFER_URL = "transaksi/{id}/transfer"
    const val TRANSAKSI_BAYAR_URL = "transaksi/{id}/bayar"
    const val FILTER_USER_URL = "filtesuser/?format=json"


    val PREFS_NAME = MainActivity::class.java.`package`?.toString()
}