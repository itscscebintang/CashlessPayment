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
import com.teknikugm.dompetft.retrofit.*
import kotlinx.android.synthetic.main.activity_transfer_saldo_scan.*
import retrofit2.Call
import retrofit2.Response

class TransferSaldoScan : AppCompatActivity() {

    private val key= "hasil"
    private var result : String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transfer_saldo_scan)

        val x = intent.extras
        result = x?.getString(key)
        txtuname_transferqr.text = result

        val a = this.getSharedPreferences(Constant.PREFS_NAME, ContextWrapper.MODE_PRIVATE)?.getString(Constant.username, "none")
        nampilinSaldo(a)

        btn_transfer_saldo_payqr.setOnClickListener(){
            if (saldocontoh_payqr.text.toString().toInt() < edit_amount_payqr.text.toString().toInt()){
                Toast.makeText(this, "Saldo Anda tidak cukup", Toast.LENGTH_SHORT).show()
            } else {
                if (edit_amount_payqr.text.toString().toInt() < 5000) {
                    Toast.makeText(this, "Transaksi minimal Rp 5.000", Toast.LENGTH_SHORT).show()
                } else {
                    transaksi()
                }
            }
        }
    }

    fun transaksi(){

        val preferences = this@TransferSaldoScan.getSharedPreferences(Constant.PREFS_NAME, ContextWrapper.MODE_PRIVATE)?.getString(Constant.username, "none")
        val uname_to_transaksi = txtuname_transferqr.text.toString()
        val amount_transaksi = edit_amount_payqr.text.toString()

        class Addtransaction : AsyncTask<Void, Void, String>(){

            lateinit var loading : ProgressDialog

            override fun onPreExecute() {
                super.onPreExecute()
                loading=  ProgressDialog.show(this@TransferSaldoScan, "menambahkan", "tunggu", false, false)
            }

            override fun doInBackground(vararg params: Void?): String {

                val params= HashMap<String, String?>()
                params ["username"]= preferences
                params ["jumlah_payment"]= amount_transaksi
                params ["username_to"] = uname_to_transaksi

                val rh = RequestHandler()
                Log.d("Params", params.entries.toString())
                return rh.sendPostRequest("http://192.168.43.203/Kantin/index.php/Transaksi_saldo/transaksi",params)
            }

            override fun onPostExecute(result: String?) {
                super.onPostExecute(result)
                loading.dismiss()

                AlertDialog.Builder(this@TransferSaldoScan)
                    .setMessage("Transaksi sebesar $amount_transaksi berhasil")
                    .setPositiveButton(android.R.string.ok) { dialog, whichButton ->
                        clearData()
                    }
                    .show()
            }
        }

        val f = Addtransaction()
        f.execute()
    }

    fun clearData(){
        txtuname_transferqr.text = ""
        edit_amount_payqr.text.clear()
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