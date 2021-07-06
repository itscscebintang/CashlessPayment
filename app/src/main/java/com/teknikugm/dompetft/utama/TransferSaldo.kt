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
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_transfer_saldo.*
import kotlinx.android.synthetic.main.activity_transfer_saldo_scan.*
import retrofit2.Call
import retrofit2.Response

class TransferSaldo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transfer_saldo)

        editbalance_transfer.setSpacing(false)
        editbalance_transfer.setDecimals(false)
        editbalance_transfer.setDelimiter(false)
        editbalance_transfer.setSeparator(".")

        val a = this.getSharedPreferences(Constant.PREFS_NAME, ContextWrapper.MODE_PRIVATE)?.getString(Constant.username, "noen")
        nampilinSaldo(a)

        panah_tfman.setOnClickListener(){
            finish()
        }

        btn_send_transfer.setOnClickListener(){
            val x = editbalance_transfer.text.toString().replace("Rp","").replace(".","")

            if (saldo_contoh.text.toString().toInt() < x.toInt()){
                // cek kalo saldo kurang

                AlertDialog.Builder(this@TransferSaldo)
                    .setMessage("Saldo Anda tidak cukup")
                    .setPositiveButton(android.R.string.ok) { dialog, whichButton ->
                        clearData()
                    }
                    .show()

            } else if(a == editidnumber_transfer.text.toString()){
                // cek kalo transfer ke diri sndiri
                AlertDialog.Builder(this@TransferSaldo)
                    .setMessage("Transaksi tidak dapat dilakukan")
                    .setPositiveButton(android.R.string.ok) { dialog, whichButton ->
                        clearData()
                        startActivity(Intent(applicationContext, MainActivity::class.java))
                    }
                    .show()

            }
            else {
                if (x.toInt() < 5000){
                    //cek kalo dak memenuhi minimal transaksi
                    Toast.makeText(this, "Transaksi minimal Rp 5000", Toast.LENGTH_SHORT).show()
                } else {
                    val username = getSharedPreferences(Constant.PREFS_NAME, ContextWrapper.MODE_PRIVATE)?.getString((Constant.username), "none")
                    transfer(username.toString(), x, editidnumber_transfer.text.toString())
                }
            }
        }

        btn_cancel_transfer.setOnClickListener(){
            clearData()
            finish()
        }

    }

    fun transfer(username:String, jumlahTransfer:String, username_to:String){
        val retrofit = RetrofitClient.instance
        val api = retrofit.create(API::class.java)
        val amount_transaksi = editbalance_transfer.text.toString()

        api.transaksi(username, jumlahTransfer.toInt(), username_to).enqueue(

            object : retrofit2.Callback<Response_Detail>{
                override fun onFailure(call: Call<Response_Detail>, t: Throwable) {
                    Toast.makeText(this@TransferSaldo, t.message, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<Response_Detail>,
                    response: Response<Response_Detail>
                ) {
                    if (response.isSuccessful) {
                        val message = response.body()?.message
                        if (response.isSuccessful) {
                            val message = response.body()?.message

                            AlertDialog.Builder(this@TransferSaldo)
                                .setMessage("Transaksi sebesar Rp$amount_transaksi berhasil")
                                .setPositiveButton(android.R.string.ok) { dialog, whichButton ->
                                    clearData()
                                    startActivity(Intent(applicationContext, MainActivity::class.java))
                                }
                                .show()

                        } else{
                            Toast.makeText(this@TransferSaldo, message , Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@TransferSaldo, response.message() , Toast.LENGTH_SHORT).show()
                    }
                }

            }

        )

    }

    fun clearData(){
        editidnumber_transfer.text.clear()
        editbalance_transfer.text?.clear()
    }

    fun nampilinSaldo(key : String?){
        lateinit var myAPI: API
        val retrofit = RetrofitClient.instance
        myAPI = retrofit.create(API::class.java)

        myAPI.getsaldo(key).enqueue(object : retrofit2.Callback<ResponseSaldo>{

            override fun onFailure(call: Call<ResponseSaldo>, t: Throwable) {
                Toast.makeText(this@TransferSaldo, "Gagal", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<ResponseSaldo>, response: Response<ResponseSaldo>) {
                val a = response.body()?.balance
                saldo_contoh.text = a.toString()
            }
        })
    }
}