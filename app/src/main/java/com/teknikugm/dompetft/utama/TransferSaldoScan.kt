package com.teknikugm.dompetft.utama

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.ContextWrapper
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.teknikugm.dompetft.R
import com.teknikugm.dompetft.pembayaran.Response_Detail
import com.teknikugm.dompetft.retrofit.*
import kotlinx.android.synthetic.main.activity_transfer_saldo.*
import kotlinx.android.synthetic.main.activity_transfer_saldo_scan.*
import me.abhinay.input.CurrencySymbols
import retrofit2.Call
import retrofit2.Response

class TransferSaldoScan : AppCompatActivity() {

    private var result : String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transfer_saldo_scan)

//         untuk edit titik di edit text
        edit_amount_payqr.setSpacing(false)
        edit_amount_payqr.setDecimals(false)
        edit_amount_payqr.setDelimiter(false)
        edit_amount_payqr.setSeparator(".")

        panah_tfscan.setOnClickListener(){
            startActivity(Intent(this, MainActivity::class.java))
        }

        val x = intent.extras
        result = x?.getString("SCAN")
        txtuname_transferqr.text = result

        val a = this.getSharedPreferences(Constant.PREFS_NAME, ContextWrapper.MODE_PRIVATE)?.getString(Constant.username, "none")
        nampilinSaldo(a)

        btn_transfer_saldo_payqr.setOnClickListener(){
            val x = edit_amount_payqr.text.toString().replace("Rp","").replace(".","")

            if (edit_amount_payqr.text.toString().isEmpty()){
                Toast.makeText(this, "Masukkan jumlah saldo yang akan ditransfer", Toast.LENGTH_SHORT).show()
            }
            else if(saldocontoh_payqr.text.toString().toInt() < x.toInt()){
                AlertDialog.Builder(this@TransferSaldoScan)
                    .setMessage("Saldo Anda tidak cukup")
                    .setPositiveButton(android.R.string.ok) { dialog, whichButton ->
                        clearData()
                    }
                    .show()
            }
            else if(a.toString() == txtuname_transferqr.text.toString()) {
                AlertDialog.Builder(this@TransferSaldoScan)
                    .setMessage("Transaksi tidak dapat dilakukan")
                    .setPositiveButton(android.R.string.ok) { dialog, whichButton ->
                        clearData()
                        startActivity(Intent(applicationContext, MainActivity::class.java))
                    }
                    .show()
            } else {
                if (x.toInt() < 5000) {
                    Toast.makeText(this, "Transaksi minimal Rp 5.000", Toast.LENGTH_SHORT).show()
                } else {
                    val username = getSharedPreferences(Constant.PREFS_NAME, ContextWrapper.MODE_PRIVATE)?.getString((Constant.username), "none")
                    transfer(username.toString(), x, txtuname_transferqr.text.toString() )
                }
            }
        }

        btn_cancel_tfscan.setOnClickListener(){
            startActivity(Intent(this, MainActivity::class.java))
            clearData()
        }

    }

    fun transfer(username:String, jumlahTransfer:String, username_to:String){

        val retrofit = RetrofitClient.instance
        val api = retrofit.create(API::class.java)
        val amount_transaksi = edit_amount_payqr.text.toString()

        api.transaksi(username, jumlahTransfer.toInt(), username_to).enqueue(

            object : retrofit2.Callback<Response_Detail> {
                override fun onFailure(call: Call<Response_Detail>, t: Throwable) {
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    Toast.makeText(this@TransferSaldoScan, t.message, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<Response_Detail>,
                    response: Response<Response_Detail>
                ) {
                    if (response.isSuccessful) {
                        val message = response.body()?.message
                        if (response.isSuccessful) {
                            val message = response.body()?.message

                            AlertDialog.Builder(this@TransferSaldoScan)
                                .setMessage("Transaksi sebesar Rp$amount_transaksi berhasil")
                                .setPositiveButton(android.R.string.ok) { dialog, whichButton ->
                                    clearData()
                                    startActivity(Intent(applicationContext, MainActivity::class.java))
                                }
                                .show()

                        } else{
                            Toast.makeText(this@TransferSaldoScan, message , Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@TransferSaldoScan, response.message() , Toast.LENGTH_SHORT).show()
                    }
                }
            }
        )
    }

    fun clearData(){
        txtuname_transferqr.text = ""
        edit_amount_payqr.text?.clear()
    }

    fun nampilinSaldo(key : String?){
        lateinit var myAPI: API
        val retrofit = RetrofitClient.instance
        myAPI = retrofit.create(API::class.java)

        myAPI.getsaldo(key).enqueue(object : retrofit2.Callback<ResponseSaldo>{

            override fun onFailure(call: Call<ResponseSaldo>, t: Throwable) {
                Toast.makeText(this@TransferSaldoScan, "Gagal", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<ResponseSaldo>, response: Response<ResponseSaldo>) {
                val a = response.body()?.balance
                saldocontoh_payqr.text = a.toString()
            }
        })
    }
}