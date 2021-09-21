package com.teknikugm.dompetft.API

import java.text.NumberFormat

object Currency {

    fun toRupiahFormat2(nominal: Int): String {
        val format = NumberFormat.getCurrencyInstance()
        format.maximumFractionDigits = 0
//        format.currency = Currency.getInstance(Locale.getDefault())

        return format.format(nominal)
    }


}