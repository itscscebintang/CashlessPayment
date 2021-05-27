package com.teknikugm.dompetft.utama

import android.content.ContextWrapper
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.teknikugm.dompetft.R
import com.teknikugm.dompetft.retrofit.API
import com.teknikugm.dompetft.retrofit.Constant
import com.teknikugm.dompetft.retrofit.Response_Topup
import com.teknikugm.dompetft.retrofit.RetrofitClient
import kotlinx.android.synthetic.main.activity_top_up.*
import kotlinx.android.synthetic.main.activity_transfer_saldo_scan.*
import retrofit2.Call
import retrofit2.Response
import kotlin.random.Random

class TopUp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top_up)

        btn_send_topup.setOnClickListener(){
            if(topup_saldo.text.toString().isEmpty()){
                Toast.makeText(this, "Masukkan jumlah saldo Top Up", Toast.LENGTH_SHORT).show()
            }else{
                doTopUp(topup_saldo.text.toString())
                clearData()
            }
        }

        btn_cancel_topup.setOnClickListener(){
            clearData()
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun doTopUp(jumlahTopUp:String){
        val retrofit = RetrofitClient.instance
        val api = retrofit.create(API::class.java)

        val randomNumber = Random.nextInt(100,999)
        val username = getSharedPreferences(Constant.PREFS_NAME, ContextWrapper.MODE_PRIVATE)?.getString((Constant.username), "none")

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
                            Toast.makeText(this@TopUp, message, Toast.LENGTH_SHORT).show()
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