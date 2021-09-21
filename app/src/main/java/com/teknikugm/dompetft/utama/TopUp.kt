package com.teknikugm.dompetft.utama

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.teknikugm.dompetft.R
import com.teknikugm.dompetft.API.ApiClient
import com.teknikugm.dompetft.API.Currency
import com.teknikugm.dompetft.API.SessionManager
import com.teknikugm.dompetft.model.DataUser
import com.teknikugm.dompetft.model.ResponseTopup
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_top_up.*
import kotlinx.android.synthetic.main.activity_transfer_saldo.*
import kotlinx.android.synthetic.main.activity_transfer_saldo_scan.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TopUp : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top_up)

        topup_saldo.setSpacing(false)
        topup_saldo.setDecimals(false)
        topup_saldo.setDelimiter(false)
        topup_saldo.setSeparator(".")

        sessionManager = SessionManager(this)
        if (sessionManager.fetchAuthToken() == null) {
        }
        else {
            val detailProfile = sessionManager.getProfile()
            txtid.text = detailProfile.id
        }

        btn_send_topup.setOnClickListener(){
            val topupsaldo = topup_saldo.text.toString().replace(".","")

            if(topup_saldo.text.toString().isEmpty()){
                topup_saldo.error = "Mohon diisi"
                topup_saldo.requestFocus()
                return@setOnClickListener
            } else if (topupsaldo.toInt() < 5000){
                Toast.makeText(this, "Top up saldo minimal Rp5.000", Toast.LENGTH_SHORT).show()
            } else {
                topUp(topupsaldo.toInt(),txtid.text.toString().toInt())
            }

        }

        panah_topup.setOnClickListener(){
            clearData()
            finish()
        }

        btn_cancel_topup.setOnClickListener(){
            clearData()
            finish()
        }
    }

    private fun topUp(jumlahtopup :Int, iduser : Int){

//        val jumlahTopup = Currency.toRupiahFormat2(topup_saldo.text.toString().toInt()).replace("$","Rp").replace(",", ".")
////        Currency.toRupiahFormat2(editbalance_transfer.text.toString().toInt()).replace("$","Rp").replace(",", ".")

        ApiClient().getApiService(this).topUpNEW(jumlahtopup,iduser)
            .enqueue(object : Callback<ResponseTopup>{
                override fun onResponse(call: Call<ResponseTopup>, response: Response<ResponseTopup>) {
                    if(response.isSuccessful){
                        if (response.isSuccessful){
//                            Toast.makeText(this@TopUp, "Berhasil", Toast.LENGTH_SHORT).show()
                            val jumlahTopup = topup_saldo.text.toString()
                            AlertDialog.Builder(this@TopUp)
                                .setTitle("Top Up")
                                .setMessage("Permintaan top up sebesar Rp$jumlahTopup berhasil")
                                .setPositiveButton("Ok") { dialog, whichButton ->
                                    finish()
                                }
                                .setNegativeButton("Kembali") { dialog, whichButton ->
                                    finish()
                                }
                                .show()
                        }else{
                            Toast.makeText(this@TopUp, "Gagal", Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        Toast.makeText(this@TopUp, "Gagal", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponseTopup>, t: Throwable) {
                    Toast.makeText(this@TopUp, t.message, Toast.LENGTH_SHORT).show()

                }
            })
    }

    fun clearData(){
        topup_saldo.setText("")
    }
}