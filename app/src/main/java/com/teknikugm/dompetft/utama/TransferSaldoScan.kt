package com.teknikugm.dompetft.utama

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.teknikugm.dompetft.API.ApiClient
import com.teknikugm.dompetft.API.SessionManager
import com.teknikugm.dompetft.R
import com.teknikugm.dompetft.model.FilterUser
import com.teknikugm.dompetft.model.ResponseTransaksi
import com.teknikugm.dompetft.model.TransferItem
import kotlinx.android.synthetic.main.activity_top_up.*
import kotlinx.android.synthetic.main.activity_transfer_saldo.*
import kotlinx.android.synthetic.main.activity_transfer_saldo_scan.*
import retrofit2.Call
import retrofit2.Response

class TransferSaldoScan : AppCompatActivity() {

    private var result : String?=null
    private var listUser = mutableListOf<FilterUser>()
    private lateinit var sessionManager : SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transfer_saldo_scan)

//         untuk edit titik di edit text
        edit_amount_payqr.setSpacing(false)
        edit_amount_payqr.setDecimals(false)
        edit_amount_payqr.setDelimiter(false)
        edit_amount_payqr.setSeparator(".")

        getSaldo()

        panah_tfscan.setOnClickListener(){
            clearData()
            finish()
            startActivity(Intent(this, MainActivity::class.java))
        }

        // ngambil username
        val x = intent.extras
        result = x?.getString("SCAN")
        txtuname_transferqr.text = result

        tampiluser()

        btn_transfer_saldo_payqr.setOnClickListener(){
            val transfer = edit_amount_payqr.text.toString().replace(".","")

            if(edit_amount_payqr.text.toString().isEmpty()){
                edit_amount_payqr.error = "Mohon diisi"
                edit_amount_payqr.requestFocus()
                return@setOnClickListener
            } else if(transfer.toInt() < 5000){
                Toast.makeText(this, "Transfer saldo minimal Rp5.000", Toast.LENGTH_SHORT).show()
            } else if(usernamescan.text == txtuname_transferqr.text){
                AlertDialog.Builder(this@TransferSaldoScan)
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
            } else if (transfer.toInt() > saldo.text.toString().toInt()){
                AlertDialog.Builder(this@TransferSaldoScan)
                    .setTitle("Transfer Saldo")
                    .setMessage("Saldo Anda tidak cukup, silakan isi ulang saldo")
                    .setPositiveButton("Top Up") { dialog, whichButton ->
                        startActivity(Intent(this, TopUp::class.java))
                    }
                    .setNegativeButton("Kembali") { dialog, whichButton ->
                        finish()
                    }
                    .show()
            }else {
                val username = result
                val filter = listUser.filter{
                    it.username == username }
                val tipeTransaksi =2

                if(filter.isNullOrEmpty()){

                    Toast.makeText(this, "Username tidak ditemukan", Toast.LENGTH_SHORT).show()

                }else{
                    val id= filter[0].id
                    transfer(tipeTransaksi, id)
                }
            }
        }

        btn_cancel_tfscan.setOnClickListener(){
            clearData()
            finish()
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    fun transfer(tipetransaksi : Int, id : Int?){
        ApiClient().getApiService(this).transaksi(tipetransaksi)
            .enqueue(object : retrofit2.Callback<ResponseTransaksi>{
                override fun onResponse(
                    call: Call<ResponseTransaksi>,
                    response: Response<ResponseTransaksi>
                ) {

                    val c = response.body()?.id
                    val transfer = edit_amount_payqr.text.toString().replace(".","").toInt()
                    val totalTransfer = edit_amount_payqr.text.toString()

                    if(response.isSuccessful){
                        ApiClient().getApiService(this@TransferSaldoScan).transfer(transfer, id, c!!)
                            .enqueue(object : retrofit2.Callback<TransferItem>{

                                override fun onResponse(call: Call<TransferItem>, response: Response<TransferItem>){

                                    AlertDialog.Builder(this@TransferSaldoScan)
                                        .setTitle("Transfer Saldo")
                                        .setMessage("Transfer saldo sebesar Rp$totalTransfer berhasil")
                                        .setPositiveButton("Ok") { dialog, whichButton ->
                                            startActivity(Intent(this@TransferSaldoScan, MainActivity::class.java))
                                            finish()
                                        }
                                        .setNegativeButton("Tutup") { dialog, whichButton ->
                                            startActivity(Intent(this@TransferSaldoScan, MainActivity::class.java))
                                            finish()
                                        }
                                        .show()
                                }

                                override fun onFailure(call: Call<TransferItem>, t: Throwable) {
                                    Toast.makeText(applicationContext, response.message(), Toast.LENGTH_SHORT).show()
                                }
                            })

                    } else {

                        AlertDialog.Builder(this@TransferSaldoScan)
                            .setTitle("Transfer Saldo")
                            .setMessage("Transfer saldo sebesar Rp$totalTransfer gagal")
                            .setPositiveButton("Ok") { dialog, whichButton ->
                                finish()
                                startActivity(Intent(this@TransferSaldoScan, MainActivity::class.java))
                            }
                            .setNegativeButton("Tutup") { dialog, whichButton ->
                                finish()
                                startActivity(Intent(this@TransferSaldoScan, MainActivity::class.java))
                            }
                            .show()

                    }
                    print(response.body()!!)
                }

                override fun onFailure(call: Call<ResponseTransaksi>, t: Throwable) {
                    t.printStackTrace()
                }

            })
    }

    fun tampiluser(){
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
        if (sessionManager.fetchAuthToken() == null) {
        }
        else {
            val detailProfile = sessionManager.getProfile()
            saldo.text = detailProfile.saldo
            usernamescan.text = detailProfile.username
        }
    }

    fun clearData(){
        txtuname_transferqr.text = ""
        edit_amount_payqr.setText("")
    }
}