package com.teknikugm.dompetft.pembayaran

import android.app.Activity
import android.content.ContextWrapper
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.teknikugm.dompetft.R
import com.teknikugm.dompetft.retrofit.API
import com.teknikugm.dompetft.retrofit.Constant
import com.teknikugm.dompetft.retrofit.ResponseSaldo
import com.teknikugm.dompetft.retrofit.RetrofitClient
import kotlinx.android.synthetic.main.activity_transaksi_pesanan.*
import kotlinx.android.synthetic.main.activity_transfer_saldo.*
import retrofit2.Call
import retrofit2.Response

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

            if(total_promo.text.toString().isEmpty()){
                if(saldo_anda.text.toString().toInt() < total_order.text.toString().toInt()){
                    Toast.makeText(this, "Saldo anda tidak cukup", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Saldo anda cukup", Toast.LENGTH_SHORT).show()
                }
            }else{
//                startActivityForResult()

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

//            val x = jumlahpromo.toString().toInt()
                val hasilScan = hasil_scan.text.toString().toInt()
                val promo1 = persentasePromo.toString().toInt()
                val promo2 = promo1*0.01
                val promo = promo2*hasilScan
                total_promo.setText("Promo Anda Rp $promo")

                if(promo > hasilScan){
                    total_order.setText("0")
                } else {
                    val hasil = hasilScan - promo
                    total_order.text = hasil.toString()
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
}