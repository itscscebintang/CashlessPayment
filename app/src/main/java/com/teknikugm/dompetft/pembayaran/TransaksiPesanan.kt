package com.teknikugm.dompetft.pembayaran

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.teknikugm.dompetft.R
import kotlinx.android.synthetic.main.activity_transaksi_pesanan.*

class TransaksiPesanan : AppCompatActivity() {

    private val key= "hasil"
    private var result : String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaksi_pesanan)

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
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            val dataPromo = data?.getSerializableExtra("promo") as DataItem
            val jumlahpromo = dataPromo.jumlahPromo
            result = dataPromo.kodePromo

            val x = jumlahpromo.toString().toInt()
            val y = hasil_scan.text.toString().toInt()

            total_promo.setText("Promo Anda Rp $jumlahpromo")
            if( x > y){
                total_order.setText("0")
            } else {
                val a = jumlahpromo.toString().toInt()
                val b = hasil_scan.text.toString().toInt()
                val c = b-a

                total_order.text = c.toString()
                test_kode_promo.text = result
            }
        }
    }

    companion object {
        const val REQUEST_CODE = 2502
    }
}