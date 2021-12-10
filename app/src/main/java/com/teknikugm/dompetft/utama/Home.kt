package com.teknikugm.dompetft.utama

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.teknikugm.dompetft.API.Currency
import com.teknikugm.dompetft.API.SessionManager
import com.teknikugm.dompetft.R
import kotlinx.android.synthetic.main.activity_home.*

class Home : Fragment() {

    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_home, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            sessionManager = SessionManager(this.context!!)
            if (sessionManager.fetchAuthToken() == null) {
            }
            else {
                val activity: MainActivity = activity as MainActivity
                val profile = activity.getProfile()
                val saldonew = profile?.saldo
                txtsaldo_home.text = Currency.toRupiahFormat2(saldonew!!.toInt()).replace(
                    "$",
                    ""
                ).replace(",", ".").replace("Rp", "")
            }
        } catch (e: NullPointerException) {
            println("NullPointerException thrown!")
        }

        Log.d("pb", progress_Bar.toString())
        print(progress_Bar.toString())

        btn_topup_home.setOnClickListener(){
            startActivity(Intent(context, TopUp::class.java))
        }

        card_transfer.setOnClickListener(){
            startActivity(Intent(context, Scanner_Transfer::class.java))
        }

        card_promo.setOnClickListener(){
            startActivity(Intent(context, Promo::class.java))
        }

        swipe_refresh.setOnRefreshListener {
            sessionManager = SessionManager(this.context!!)
            if (sessionManager.fetchAuthToken() == null) {
            }
            else {
                val activity: MainActivity = activity as MainActivity
                val profile = activity.getProfile()
                txtsaldo_home.text= Currency.toRupiahFormat2(profile?.saldo!!.toInt()).replace(
                    "$",
                    ""
                ).replace(",", ".").replace("Rp", "")
            }
            swipe_refresh.isRefreshing= false
        }
    }
}