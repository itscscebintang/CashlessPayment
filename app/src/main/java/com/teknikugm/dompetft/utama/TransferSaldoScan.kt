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

    private val key= "hasil"
    private var result : String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transfer_saldo_scan)

//         untuk edit titik di edit text
        edit_amount_payqr.setCurrency(CurrencySymbols.INDONESIA)
        edit_amount_payqr.setSpacing(true)
        edit_amount_payqr.setDecimals(false)
        edit_amount_payqr.setDelimiter(true)
        edit_amount_payqr.setSeparator(".")

        val x = intent.extras
        result = x?.getString(key)
        txtuname_transferqr.text = result

        val a = this.getSharedPreferences(Constant.PREFS_NAME, ContextWrapper.MODE_PRIVATE)?.getString(Constant.username, "none")
        nampilinSaldo(a)

        btn_transfer_saldo_payqr.setOnClickListener(){
            val x = edit_amount_payqr.text.toString().replace("Rp. ","").replace(".","")

            if (saldocontoh_payqr.text.toString().toInt() < x.toInt()){
                Toast.makeText(this, "Saldo Anda tidak cukup", Toast.LENGTH_SHORT).show()
            }else if(a.toString() == txtuname_transferqr.text.toString()) {
                Toast.makeText(this, "Transaksi tidak dapat dilakukan", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
            }else {
                if (x.toInt() < 5000) {
                    Toast.makeText(this, "Transaksi minimal Rp 5.000", Toast.LENGTH_SHORT).show()
                } else {
//                    transaksi()
                    val username = getSharedPreferences(Constant.PREFS_NAME, ContextWrapper.MODE_PRIVATE)?.getString((Constant.username), "none")
                    transfer(username.toString(), x, txtuname_transferqr.text.toString() )
                }
            }
        }
    }

    fun transaksi(){

        val preferences = this@TransferSaldoScan.getSharedPreferences(Constant.PREFS_NAME, ContextWrapper.MODE_PRIVATE)?.getString(Constant.username, "none")
        val uname_to_transaksi = txtuname_transferqr.text.toString()
        val amount_transaksi = edit_amount_payqr.text.toString()
        val amount= edit_amount_payqr.text.toString().replace("Rp. ","").replace(".","")

        class Addtransaction : AsyncTask<Void, Void, String>(){

            lateinit var loading : ProgressDialog

            override fun onPreExecute() {
                super.onPreExecute()
                loading=  ProgressDialog.show(this@TransferSaldoScan, "menambahkan", "tunggu", false, false)
            }

            override fun doInBackground(vararg params: Void?): String {


                val params= HashMap<String, String?>()
                params ["username"]= preferences
                params ["jumlah_payment"]= amount
                params ["username_to"] = uname_to_transaksi

                val rh = RequestHandler()
                Log.d("Params", params.entries.toString())
                return rh.sendPostRequest("http://192.168.18.158/Kantin/index.php/Transaksi_saldo/transaksi",params)
            }

            override fun onPostExecute(result: String?) {
                super.onPostExecute(result)
                loading.dismiss()

                AlertDialog.Builder(this@TransferSaldoScan)
                    .setMessage("Transaksi sebesar $amount_transaksi berhasil")
                    .setPositiveButton(android.R.string.ok) { dialog, whichButton ->
                        clearData()
                        startActivity(Intent(applicationContext, MainActivity::class.java))

                    }
                    .show()
            }
        }

        val f = Addtransaction()
        f.execute()
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
                                .setMessage("Transaksi sebesar $amount_transaksi berhasil")
                                .setPositiveButton(android.R.string.ok) { dialog, whichButton ->
                                    clearData()
                                    startActivity(Intent(applicationContext, MainActivity::class.java))

                                }
                                .show()
                            Toast.makeText(this@TransferSaldoScan, message, Toast.LENGTH_SHORT).show()
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