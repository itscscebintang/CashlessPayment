package com.teknikugm.dompetft.pembayaran

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.teknikugm.dompetft.R
import com.teknikugm.dompetft.retrofit.API
import com.teknikugm.dompetft.retrofit.RetrofitClient
import kotlinx.android.synthetic.main.activity_promo.*
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class Promo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_promo)

        list_promo()
    }

    fun list_promo(){

        RetrofitClient.instance.create(API::class.java).ambil_promo()
            .enqueue(object : retrofit2.Callback<ResponsePromo>{

                override fun onResponse(
                    call: Call<ResponsePromo>,
                    response: Response<ResponsePromo>
                ) {
                    if (response.isSuccessful){
                        val dataItem = response.body()?.data
                        val adapter = PromoAdapter(dataItem, this@Promo){ item->
                            val ii = Intent()
                            ii.putExtra("promo", item)
                            setResult(Activity.RESULT_OK, ii)
                            finish()
                        }

                        rv.layoutManager = LinearLayoutManager(this@Promo)
                        rv.adapter = adapter
                    }
                }

                override fun onFailure(call: Call<ResponsePromo>, t: Throwable) {
                    Toast.makeText(applicationContext, "Connection failed", Toast.LENGTH_SHORT).show()
                }
            })
    }
}