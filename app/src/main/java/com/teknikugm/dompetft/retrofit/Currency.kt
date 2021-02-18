package com.teknikugm.dompetft.retrofit

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.*

object Currency {


    fun toRupiahFormat2(nominal: Int): String {
        val format = NumberFormat.getCurrencyInstance()
        format.maximumFractionDigits = 0
//        format.currency = Currency.getInstance(Locale.getDefault())

        return format.format(nominal)
    }


}