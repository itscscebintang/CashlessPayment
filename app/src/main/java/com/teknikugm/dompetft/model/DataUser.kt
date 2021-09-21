package com.teknikugm.dompetft.model

import java.io.Serializable

data class DataUser(
    var username: String? = null,
    var email: String? = null,
    var id: String? = null,
    var last_name: String? = null,
    var saldo: String? = null
) : Serializable