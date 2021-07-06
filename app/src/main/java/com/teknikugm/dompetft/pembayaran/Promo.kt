package com.teknikugm.dompetft.pembayaran

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.teknikugm.dompetft.R
import com.teknikugm.dompetft.retrofit.API
import com.teknikugm.dompetft.retrofit.RetrofitClient
import com.teknikugm.dompetft.utama.MainActivity
import kotlinx.android.synthetic.main.activity_promo.*
import kotlinx.android.synthetic.main.activity_promo_adapter.*
import kotlinx.android.synthetic.main.activity_transaksi_pesanan.*
import kotlinx.android.synthetic.main.activity_transfer_saldo.*
import okhttp3.internal.checkOffsetAndCount
import retrofit2.Call
import retrofit2.Response

class Promo : AppCompatActivity() {

    private var result : String?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_promo)

        var selectedPromo = intent.getStringExtra("kode_promo")
        list_promo(selectedPromo)

        panah_promo.setOnClickListener(){
            startActivity(Intent(this, MainActivity::class.java))
        }

        refresh_promo.setOnRefreshListener {
            list_promo(selectedPromo)
        }
    }

    fun list_promo(selectedPromo: String?) {

        refresh_promo.isRefreshing=true

        RetrofitClient.instance.create(API::class.java).ambil_promo()
            .enqueue(object : retrofit2.Callback<ResponsePromo>{

                @SuppressLint("SetTextI18n")
                override fun onResponse(
                    call: Call<ResponsePromo>,
                    response: Response<ResponsePromo>
                ) {
                    if (response.isSuccessful){
                        val dataItem = response.body()?.data
                        if(response.body()?.status == 1){
                            icon.setImageResource(R.drawable.icon_pricetag)
                            ops.setText("Oops!")
                            penjelasan.setText("Looks like you don't have any voucher.")
                            ava_promos.setText("")
                        }else {
                            val adapter = PromoAdapter(dataItem, this@Promo) { item ->
                                val x = intent.extras // ini untuk ngambil data yang dikasih sama transaksi pesanan, untuk ambil hasil scannya tu
                                result = x?.getString("hasil") // nilainya ditarok di sini
                                val totalbelanja = result?.toInt() // nilainya dimasukin ke variabel
                                val y = item?.minBelanja.toString().toInt() // ini untuk ngambil minimal belanja di list promo
//                                val row = item?.jumlah_barispromo.toString()
//                                banyakpromo.text = row

                                if (totalbelanja != null) {
                                    if (totalbelanja < y) {
                                        Toast.makeText(applicationContext, "Total belanja Anda masih kurang dari $y", Toast.LENGTH_SHORT).show()
                                    } else if (item?.kodePromo == selectedPromo) {
                                        Toast.makeText(applicationContext, "Promo $selectedPromo sudah Anda pakai", Toast.LENGTH_SHORT).show()
                                    } else {
                                        val ii = Intent()
                                        ii.putExtra("promo", item)
                                        setResult(Activity.RESULT_OK, ii)
                                        finish()
                                    }
                                }
                            }
                            rv.layoutManager = LinearLayoutManager(this@Promo)
                            rv.adapter = adapter
                        }
                    }
                }

                override fun onFailure(call: Call<ResponsePromo>, t: Throwable) {
                    Toast.makeText(applicationContext, "Connection failed", Toast.LENGTH_SHORT).show()
                }
            })

        refresh_promo.isRefreshing=false
    }

    companion object {
        const val REQUEST_CODE = 2502
    }
}