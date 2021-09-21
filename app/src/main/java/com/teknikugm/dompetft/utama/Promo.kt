package com.teknikugm.dompetft.utama

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.teknikugm.dompetft.R
import com.teknikugm.dompetft.API.ApiClient
import com.teknikugm.dompetft.API.SessionManager
import com.teknikugm.dompetft.adapter.PromoAdapter
import com.teknikugm.dompetft.model.ResponsePromo
import kotlinx.android.synthetic.main.activity_promo.*
import kotlinx.android.synthetic.main.activity_promo_adapter.*
import kotlinx.android.synthetic.main.activity_transaksi_pesanan.*
import kotlinx.android.synthetic.main.activity_transfer_saldo.*
import retrofit2.Call
import retrofit2.Response

class Promo : AppCompatActivity() {

    private lateinit var progressBar : ProgressBar

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_promo)

        val selectedPromo = intent.getStringExtra("kode_promo")
        progressBar = findViewById(R.id.pbpromo)

        listPromo(selectedPromo)

        panah_promo.setOnClickListener(){
            startActivity(Intent(this, MainActivity::class.java))
        }

    }

    fun listPromo (selectedPromo : String?){
        ApiClient().getApiService(this).ambil_promo()
            .enqueue(object : retrofit2.Callback<List<ResponsePromo>>{
                override fun onResponse(
                    call: Call<List<ResponsePromo>>,
                    response: Response<List<ResponsePromo>>
                ) {
                    refresh_promo.isRefreshing = true
                    val dataItem : List<ResponsePromo?>? = response.body()

                    if(response.isSuccessful){
                        showLoading()
                        if(dataItem?.size!! > 0){
                            ava_promos.setText("Available Promos")
                            val adapter = PromoAdapter(dataItem, this@Promo) { item ->
                                val backIntent = Intent()
                                backIntent.putExtra(DATA_PROMO, item)
                                setResult(Activity.RESULT_OK, backIntent)
                                finish()
                            }
                            rv.layoutManager = LinearLayoutManager(this@Promo)
                            rv.adapter = adapter
                            hideLoading()

                        } else {
                            gambarpromo.setImageResource(R.drawable.icon_pricetag)
                            Toast.makeText(this@Promo, "Promo tidak tersedia", Toast.LENGTH_SHORT).show()
                        }

                    } else {
                        Toast.makeText(this@Promo, "Tidak ada promo", Toast.LENGTH_SHORT).show()
                    }

                    refresh_promo.isRefreshing = false
                }

                @SuppressLint("SetTextI18n")
                override fun onFailure(call: Call<List<ResponsePromo>>, t: Throwable) {
//                    Toast.makeText(this@Promo, "Connection Failed", Toast.LENGTH_SHORT).show()
                    gambarpromo.setImageResource(R.drawable.icon_pricetag)
                    ops.setText("Oops!")
                    penjelasan.setText("Looks like you don't have any voucher.")
                    ava_promos.setText("")
                    hideLoading()
                }

            })
    }

    fun showLoading(){
        progressBar.visibility
    }

    fun hideLoading(){
        progressBar.setVisibility(View.GONE)
    }
    companion object {
        const val REQUEST_CODE = 2502
        const val DATA_PROMO = "data promo"
    }
}