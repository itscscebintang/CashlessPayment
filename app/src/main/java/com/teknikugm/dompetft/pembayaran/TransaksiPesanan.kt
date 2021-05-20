package com.teknikugm.dompetft.pembayaran

import android.app.Activity
import android.app.AlertDialog
import android.content.ContextWrapper
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.edit
import com.teknikugm.dompetft.R
import com.teknikugm.dompetft.retrofit.*
import com.teknikugm.dompetft.utama.Login
import com.teknikugm.dompetft.utama.MainActivity
import kotlinx.android.synthetic.main.activity_promo_adapter.*
import kotlinx.android.synthetic.main.activity_transaksi_pesanan.*
import kotlinx.android.synthetic.main.activity_transfer_saldo.*
import retrofit2.Call
import retrofit2.Response
import kotlin.random.Random

class TransaksiPesanan : AppCompatActivity() {

    private val key= "hasil"
    private var result : String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaksi_pesanan)

        val tt = this.getSharedPreferences(Constant.PREFS_NAME, ContextWrapper.MODE_PRIVATE)?.getString(
            Constant.username, "noen")
        nampilinSaldo(tt)

        val totalOrder = intent.getStringExtra("hasil")
        hasil_scan.text = totalOrder
        total_order.text = totalOrder

        btn_promo.setOnClickListener(){
            val a = hasil_scan.text.toString() // ini untuk ambil total order biar bisa dibawa ke list promo, untuk dicek min belanjanya tu
            val i = Intent(this, Promo::class.java) // ini dia nge intent ke kelas promo
            i.putExtra(key, a) // ini untuk bawa nilainya, kek key tu extrasnya, jdi kayak nyambungin yg nilai dibawa ni nnti di activity tujuannya taroknya dimana
            i.putExtra("kode_promo", result)
            startActivityForResult((i), REQUEST_CODE) // tu ini startactivity kalo bawa nilai, jdi startactivityforresult
        }


        btn_pay_pesanan.setOnClickListener(){


            val  promo4 = 90
            val a = test_promo.text.toString()
            val promoo = Currency.toRupiahFormat2(a.toInt()).replace(",", "").replace(".", "").replace("Rp", "")

            if(total_promo.text.toString().isEmpty()){
                if(saldo_anda.text.toString().toInt() < total_order.text.toString().toInt()){
                    Toast.makeText(this, "Saldo anda tidak cukup", Toast.LENGTH_SHORT).show()
                } else {

                    detailTransaksi(total_order.text.toString(), hasil_scan.text.toString(), a)
                }
            }else{
                detailTransaksi(total_order.text.toString(), hasil_scan.text.toString(),a)
            }
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

            if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
                val dataPromo = data?.getSerializableExtra("promo") as DataItem
                val jumlahpromo = dataPromo.jumlahPromo
                result = dataPromo.kodePromo
                val persentasePromo = dataPromo.persentasePromo

                test_persentase.text = persentasePromo.toString()

                val hasilScan = hasil_scan.text.toString().toInt()
                val promo1 = persentasePromo.toString().toInt()
                val promo2 = promo1*0.01
                val promo = promo2*hasilScan
                total_promo.setText("Promo Anda Rp $promo")
                test_promo.text = Currency.toRupiahFormat2(promo.toInt()).replace("$", "").replace(".", "").replace(",", "")

                if(promo > hasilScan){
                    total_order.setText("0")
                } else {
                    val hasil = hasilScan - promo
                    total_order.text =  Currency.toRupiahFormat2(hasil.toInt()).replace("$", "").replace(".", "").replace(",", "")
                }

                test_kode_promo.text = result





//            if( x > y){
//                total_order.setText("0")
//            } else {
//                val b = hasil_scan.text.toString().toInt()
//                val promo = (persentasePromo.toString().toInt() / 100) * b
//
//                val a = jumlahpromo.toString().toInt()
//
//                val c = b-promo
//
//                total_order.text = c.toString()
//                test_kode_promo.text = result
//            }
        }
    }

    companion object {
        const val REQUEST_CODE = 2502
    }

    fun nampilinSaldo(key : String?){
        lateinit var myAPI: API
        val retrofit = RetrofitClient.instance
        myAPI = retrofit.create(API::class.java)

        myAPI.getsaldo(key).enqueue(object : retrofit2.Callback<ResponseSaldo>{

            override fun onFailure(call: Call<ResponseSaldo>, t: Throwable) {
                Toast.makeText(this@TransaksiPesanan, "Gagal", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<ResponseSaldo>, response: Response<ResponseSaldo>) {
                val a = response.body()?.balance
                saldo_anda.text = a.toString()
            }
        })
    }

    fun detailTransaksi (totalBayar : String, totalasli:String, diskon:String){
        val retrofit = RetrofitClient.instance
        val api = retrofit.create(API::class.java)

        val username = getSharedPreferences(Constant.PREFS_NAME, ContextWrapper.MODE_PRIVATE)?.getString((Constant.username), "none")

        api.detailTransaksi(username, totalBayar.toInt(), totalasli.toInt(), diskon.toInt()).enqueue(

            object : retrofit2.Callback<Response_Detail>{
                override fun onFailure(call: Call<Response_Detail>, t: Throwable) {
                    Toast.makeText(this@TransaksiPesanan, t.message, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<Response_Detail>,
                    response: Response<Response_Detail>
                ) {
                    if(response.isSuccessful){
                        val message = response.body()?.message
                        if (response.isSuccessful){
                            val message = response.body()?.message

                            val a = total_order.text.toString().toInt()
                            val b = saldo_anda.text.toString().toInt()
                            val c = b - a

                            val d = Currency.toRupiahFormat2(a).replace("$", "Rp").replace(",", ".")
                            val e = Currency.toRupiahFormat2(c).replace("$", "Rp").replace(",", ".")

                            AlertDialog.Builder(this@TransaksiPesanan)
                                .setTitle("Transaksi Pembayaran senilai $d berhasil ")
                                .setMessage("Saldo Anda sekarang : $e")
                                .setPositiveButton("OK") { dialog, whichButton ->
                                    startActivity(Intent(this@TransaksiPesanan, MainActivity::class.java))
                                }
                                .setNegativeButton("CLOSE") { dialog, whichButton ->
                                    startActivity(Intent(this@TransaksiPesanan, MainActivity::class.java))
                                }
                                .show()

                            Toast.makeText(this@TransaksiPesanan, message, Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(this@TransaksiPesanan, message, Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        Toast.makeText(this@TransaksiPesanan, response.message(), Toast.LENGTH_SHORT).show()
                    }
                }

            }

        )

    }
}