package com.teknikugm.dompetft.utama

import android.content.ContextWrapper
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.teknikugm.dompetft.*
import com.teknikugm.dompetft.pembayaran.Promo
import com.teknikugm.dompetft.retrofit.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_home.btn_topup_home
import retrofit2.Call
import retrofit2.Response

class Home : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_home, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        btn_pay_home.setOnClickListener(){
//            startActivity(Intent(context, Transaksi_Pesanan::class.java))
//        }

        btn_topup_home.setOnClickListener(){
            startActivity(Intent(context, TopUp::class.java))
        }

        card_qr.setOnClickListener(){
            startActivity(Intent(context, MyQR::class.java))
        }

        card_transfer.setOnClickListener(){
            startActivity(Intent(context, TransferSaldo::class.java))
        }

        card_promo.setOnClickListener(){
            startActivity(Intent(context, Promo::class.java))
        }

        val b = context?.getSharedPreferences(Constant.PREFS_NAME, ContextWrapper.MODE_PRIVATE)?.getString(Constant.username, "none")
        nampilinSaldo(b)

        swipe_refresh.setOnRefreshListener {
            nampilinSaldo(b)
            swipe_refresh.isRefreshing= false
        }
    }

    fun nampilinSaldo(key : String?){
        lateinit var myAPI: API
        val retrofit = RetrofitClient.instance
        myAPI = retrofit.create(API::class.java)

        myAPI.getsaldo(key).enqueue(object : retrofit2.Callback<ResponseSaldo>{

            override fun onFailure(call: Call<ResponseSaldo>, t: Throwable) {
                Toast.makeText(context, "Tidak bisa memuat saldo", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<ResponseSaldo>, response: Response<ResponseSaldo>) {
                val a = response.body()?.balance.toString().toInt()
                txtsaldo_home.text = Currency.toRupiahFormat2(a).replace("$", "").replace(",", ".")
            }
        })
    }
}