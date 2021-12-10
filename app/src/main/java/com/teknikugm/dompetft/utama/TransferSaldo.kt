package com.teknikugm.dompetft.utama

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.teknikugm.dompetft.API.ApiClient
import com.teknikugm.dompetft.API.SessionManager
import com.teknikugm.dompetft.R
import com.teknikugm.dompetft.model.FilterUser
import com.teknikugm.dompetft.model.ResponseTransaksi
import com.teknikugm.dompetft.model.TransferItem
import kotlinx.android.synthetic.main.activity_transfer_saldo.*
import retrofit2.Call
import retrofit2.Response

class TransferSaldo : AppCompatActivity() {

    private var listUser = mutableListOf<FilterUser>()
    private lateinit var sessionManager : SessionManager
    private var saldo : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transfer_saldo)

        editbalance_transfer.setSpacing(false)
        editbalance_transfer.setDecimals(false)
        editbalance_transfer.setDelimiter(false)
        editbalance_transfer.setSeparator(".")

        tampilUser()
        getSaldo()

        panah_tfman.setOnClickListener(){
            startActivity(Intent(this, MainActivity::class.java))
        }

        Log.d("username", username.text.toString())
        print(username.text)

        btn_send_transfer.setOnClickListener(){
            val transfer = editbalance_transfer.text.toString().replace(".","")

            if(editusername_transfer.text.toString().isEmpty()){
                editusername_transfer.error = "Mohon diisi"
                editusername_transfer.requestFocus()
                return@setOnClickListener
            } else if (editbalance_transfer.text.toString().isEmpty()){
                editbalance_transfer.error = "Mohon diisi"
                editbalance_transfer.requestFocus()
                return@setOnClickListener
            } else if(transfer.toInt() < 5000){
                Toast.makeText(this, "Transfer saldo minimal Rp5.000", Toast.LENGTH_SHORT).show()
            } else if(editusername_transfer.text.toString() == username.text.toString()){
                AlertDialog.Builder(this@TransferSaldo)
                    .setTitle("Transfer Saldo")
                    .setMessage("Transaksi tidak dapat dilakukan")
                    .setPositiveButton("Ok") { dialog, whichButton ->
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                    .setNegativeButton("Kembali") { dialog, whichButton ->
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                    .show()
            } else {
                if(transfer.toInt() > saldo?.toInt()!!){
                    AlertDialog.Builder(this@TransferSaldo)
                        .setTitle("Transfer Saldo")
                        .setMessage("Saldo Anda tidak cukup, silakan isi ulang saldo")
                        .setPositiveButton("Top Up") { dialog, whichButton ->
                            startActivity(Intent(this, TopUp::class.java))
                        }
                        .setNegativeButton("Kembali") { dialog, whichButton ->
                            finish()
                        }
                        .show()
                    Toast.makeText(this, "Saldo Anda tidak cukup", Toast.LENGTH_SHORT).show()
                } else {
                    val username = editusername_transfer.text.toString()
                    val filter = listUser.filter {
                        it.username == username
                    }

                    val tipeTransaksi =2

                    if(filter.isNullOrEmpty()){
                        Toast.makeText(this, "Username tidak ditemukan", Toast.LENGTH_SHORT).show()

                    }else{
                        val id= filter[0].id
                        transfer(tipeTransaksi, id)
                    }

                }
            }

        }

        btn_cancel_transfer.setOnClickListener(){
            clearData()
            startActivity(Intent(this, MainActivity::class.java))
        }

    }

    private fun transfer(tipetransaksi : Int, id : Int?){
        ApiClient().getApiService(this).transaksi(tipetransaksi)
            .enqueue(object : retrofit2.Callback<ResponseTransaksi>{
                override fun onResponse(
                    call: Call<ResponseTransaksi>,
                    response: Response<ResponseTransaksi>
                ) {

                    val resp = response.body()
                    val transfer = editbalance_transfer.text.toString()
                    val totalTransfer = editbalance_transfer.text.toString().replace(".","").toInt()

                    if(response.isSuccessful){

                        val a = resp?.id

                            ApiClient().getApiService(this@TransferSaldo).transfer(totalTransfer, id, a!!)
                                .enqueue(object : retrofit2.Callback<TransferItem>{
                                    override fun onResponse(
                                        call: Call<TransferItem>,
                                        response: Response<TransferItem>
                                    ) {
                                        AlertDialog.Builder(this@TransferSaldo)
                                            .setTitle("Transfer Saldo")
                                            .setMessage("Transfer saldo sebesar Rp$transfer berhasil")
                                            .setPositiveButton("Ok") { dialog, whichButton ->
                                                startActivity(Intent(applicationContext, MainActivity::class.java))
                                                finish()
                                            }
                                            .setNegativeButton("Tutup") { dialog, whichButton ->
                                                startActivity(Intent(applicationContext, MainActivity::class.java))
                                                finish()
                                            }
                                            .show()
                                    }
                                    override fun onFailure(call: Call<TransferItem>, t: Throwable) {
                                        Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()
                                    }
                                })
                    } else {
                        Toast.makeText(applicationContext, "Transfer saldo sebesar Rp$transfer gagal", Toast.LENGTH_SHORT).show()
                    }
                    print(response.body())
                }

                override fun onFailure(call: Call<ResponseTransaksi>, t: Throwable) {
                    t.printStackTrace()
                }
            }
            )
    }

    private fun tampilUser(){
        ApiClient().getApiService(this).filteruser()
            .enqueue(object : retrofit2.Callback<List<FilterUser>>{
                override fun onResponse(
                    call: Call<List<FilterUser>>,
                    response: Response<List<FilterUser>>
                ) {
                   if(response.isSuccessful){
                        listUser.addAll(response.body()!!)
                   }
                }

                override fun onFailure(call: Call<List<FilterUser>>, t: Throwable) {
                }
            })
    }

    fun getSaldo(){
        sessionManager = SessionManager(this)
        if(sessionManager.fetchAuthToken() == null){

        } else {
            val dataUser = sessionManager.getProfile()
            saldo = dataUser.saldo
            username.text = dataUser.username
        }
    }

    fun clearData(){
        editusername_transfer.text.clear()
        editbalance_transfer.setText("")
    }
}