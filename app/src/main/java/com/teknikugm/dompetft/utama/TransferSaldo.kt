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

        val a = this.getSharedPreferences(Constant.PREFS_NAME, ContextWrapper.MODE_PRIVATE)?.getString(Constant.username, "noen")
        nampilinSaldo(a)

        btn_send_transfer.setOnClickListener(){

            if (saldo_contoh.text.toString().toInt() < editbalance_transfer.text.toString().toInt()){
                Toast.makeText(this, "Saldo Anda tidak cukup", Toast.LENGTH_SHORT).show()
            } else if(a == editidnumber_transfer.text.toString()){
               Toast.makeText(this, "Transaksi tidak dapat dilakukan", Toast.LENGTH_SHORT).show()
            }
            else {
                if (editbalance_transfer.text.toString().toInt() < 5000){
                    Toast.makeText(this, "Transaksi minimal Rp 5000", Toast.LENGTH_SHORT).show()
                } else {
//                    transaksi()
                    val username = getSharedPreferences(Constant.PREFS_NAME, ContextWrapper.MODE_PRIVATE)?.getString((Constant.username), "none")
                    transfer(username.toString(), editbalance_transfer.text.toString(), editidnumber_transfer.text.toString())
                }
            }
        }

        btn_cancel_transfer.setOnClickListener(){
            clearData()
            finish()
        }
    }

    fun transaksi(){

        val preferences = this.getSharedPreferences(Constant.PREFS_NAME, ContextWrapper.MODE_PRIVATE)?.getString(Constant.username, "noen")
        val uname_to_transaksi = editidnumber_transfer.text.toString()
        val amount_transaksi = editbalance_transfer.text.toString()

        class Addtransaction : AsyncTask<Void, Void, String>(){

            lateinit var loading : ProgressDialog

            override fun onPreExecute() {
                super.onPreExecute()
                loading=  ProgressDialog.show(this@TransferSaldo, "menambahkan", "tunggu", false, false)
            }

            override fun doInBackground(vararg params: Void?): String {

                val params= HashMap<String, String?>()
                params ["username"]= preferences
                params ["jumlah_payment"]= amount_transaksi
                params ["username_to"] = uname_to_transaksi

                val rh = RequestHandler()
                Log.d("Params", params.entries.toString())
                return rh.sendPostRequest("http://192.168.18.158/Kantin/index.php/Transaksi_saldo/transaksi",params)
            }

            override fun onPostExecute(result: String?) {
                super.onPostExecute(result)
                loading.dismiss()


                AlertDialog.Builder(this@TransferSaldo)
                    .setMessage("Transaksi sebesar $amount_transaksi berhasil")
                    .setPositiveButton(android.R.string.ok) { dialog, whichButton ->
                        clearData()
                        finish()
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
                                .setMessage("Transaksi sebesar $amount_transaksi berhasil")
                                .setPositiveButton(android.R.string.ok) { dialog, whichButton ->
                                    clearData()
                                    startActivity(Intent(applicationContext, MainActivity::class.java))
                                }
                                .show()
                            Toast.makeText(this@TransferSaldo, message, Toast.LENGTH_SHORT).show()
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
        editbalance_transfer.text.clear()
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