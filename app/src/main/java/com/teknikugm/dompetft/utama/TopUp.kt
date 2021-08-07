package com.teknikugm.dompetft.utama

import android.app.AlertDialog
import android.content.ContextWrapper
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.teknikugm.dompetft.R
import com.teknikugm.dompetft.retrofit.*
import kotlinx.android.synthetic.main.activity_top_up.*
import kotlinx.android.synthetic.main.activity_transfer_saldo_scan.*
import retrofit2.Call
import retrofit2.Response
import kotlin.random.Random

class TopUp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top_up)

        topup_saldo.setSpacing(false)
        topup_saldo.setDecimals(false)
        topup_saldo.setDelimiter(false)
        topup_saldo.setSeparator(".")

        btn_send_topup.setOnClickListener(){
            val x = topup_saldo.text.toString().replace("Rp","").replace(".","")

            if(x.isEmpty()){
                Toast.makeText(this, "Masukkan jumlah saldo Top Up", Toast.LENGTH_SHORT).show()
            } else if(x.toInt() < 5000 ){
                Toast.makeText(this, "Top up minimal Rp 5.000", Toast.LENGTH_SHORT).show()
            } else{
                doTopUp(x)
                clearData()
            }
        }

        btn_cancel_topup.setOnClickListener(){
            clearData()
            finish()
        }

        panah_topup.setOnClickListener(){
            finish()
        }

    }

    private fun doTopUp(jumlahTopUp:String){
        val retrofit = RetrofitClient.instance
        val api = retrofit.create(API::class.java)

        val randomNumber = Random.nextInt(100,999)
        val username = getSharedPreferences(Constant.PREFS_NAME, ContextWrapper.MODE_PRIVATE)?.getString((Constant.username), "none")
        val amount_transaksi = topup_saldo.text.toString()

        api.topUpSaldo(jumlahTopUp.toInt(), username, randomNumber).enqueue(

            object : retrofit2.Callback<Response_Topup>{
                override fun onFailure(call: Call<Response_Topup>, t: Throwable) {
                    Toast.makeText(this@TopUp, t.message, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<Response_Topup>,
                    response: Response<Response_Topup>
                ) {
                    if(response.isSuccessful){
                        val message = response.body()?.message
                        if (response.isSuccessful){
                            val message = response.body()?.message

                            AlertDialog.Builder(this@TopUp)
                                .setMessage("Permintaan top up sebesar Rp$amount_transaksi berhasil")
                                .setPositiveButton(android.R.string.ok) { dialog, whichButton ->
                                    clearData()
                                    startActivity(Intent(applicationContext, MainActivity::class.java))
                                }
                                .show()
                        }else{
                            Toast.makeText(this@TopUp, message, Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        Toast.makeText(this@TopUp, response.message(), Toast.LENGTH_SHORT).show()
                    }
                }

            }

        )
    }

    fun clearData(){
        topup_saldo.setText("")
    }
}