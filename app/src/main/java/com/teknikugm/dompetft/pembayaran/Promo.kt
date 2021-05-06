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
import kotlinx.android.synthetic.main.activity_promo_adapter.*
import kotlinx.android.synthetic.main.activity_transaksi_pesanan.*
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class Promo : AppCompatActivity() {

    private var key= "hasil"
    private var result : String?= null

    private val key1 = "hasil1"
    private var result1 : String?=null

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

                            val x = intent.extras // ini untuk ngambil data yang dikasih sama transaksi pesanan, untuk ambil hasil scannya tu
                            result = x?.getString(key) // nilainya ditarok di sini
                            val totalbelanja = result?.toInt() // nilainya dimasukin ke variabel
                            val y = item?.minBelanja.toString().toInt() // ini untuk ngambil minimal belanja di list promo
//
                            val a = intent.extras
                            result1 = a?.getString("hasil1")
                            val kodepromo = result1.toString()

                            status_promo.text= item?.statusPromo.toString()
                            val ss = item?.kodePromo.toString()

                            if(ss == kodepromo || status_promo.text.toString() == "0" ){
                                Toast.makeText(applicationContext, "Promo telah dipakai", Toast.LENGTH_LONG).show()
                            } else {
                                if(totalbelanja != null){
                                    if(totalbelanja < y){
                                        Toast.makeText(applicationContext, "Total belanja Anda masih kurang dari $y", Toast.LENGTH_SHORT).show()
                                    } else {
                                        val ii = Intent()
                                        ii.putExtra("promo", item)
                                        setResult(Activity.RESULT_OK, ii)
                                        finish()
                                    }
                                }
                            }

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

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
//            val dataPromo = data?.getSerializableExtra("minbelanja") as DataItem
//            val minBelanja = dataPromo.minBelanja
//        }
//    }

    companion object {
        const val REQUEST_CODE = 2502
    }
}