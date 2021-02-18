package com.teknikugm.dompetft

import android.content.ContextWrapper
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.teknikugm.dompetft.retrofit.Constant
import com.teknikugm.dompetft.retrofit.Currency
import kotlinx.android.synthetic.main.activity_home.*

class Home : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_home, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var saldo_home = context?.getSharedPreferences(Constant.PREFS_NAME, ContextWrapper.MODE_PRIVATE)?.getString(Constant.balance, "None")
        val a = saldo_home.toString().toInt()

        txtsaldo_home.text = Currency.toRupiahFormat2(a).replace("$", "").replace(",", ".")

        card_qr.setOnClickListener(){
            startActivity(Intent(context, MyQR::class.java))
        }

        card_transfer.setOnClickListener(){
            startActivity(Intent(context, TransferSaldo::class.java))
        }
    }
}