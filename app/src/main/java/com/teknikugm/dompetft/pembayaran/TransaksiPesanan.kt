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

        val x = intent.extras // ini untuk ambil nilai dri promo
        result = x?.getString(key)
        hasil_scan.text = result
        total_order.text = result

        btn_promo.setOnClickListener(){
            val a = hasil_scan.text.toString() // ini untuk ambil total order biar bisa dibawa ke list promo, untuk dicek min belanjanya tu
            val i = Intent(this, Promo::class.java) // ini dia nge intent ke kelas promo
            i.putExtra(key, a) // ini untuk bawa nilainya, kek key tu extrasnya, jdi kayak nyambungin yg nilai dibawa ni nnti di activity tujuannya taroknya dimana
            startActivityForResult((i), REQUEST_CODE) // tu ini startactivity kalo bawa nilai, jdi startactivityforresult
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            val dataPromo = data?.getSerializableExtra("promo") as DataItem
            val promo = dataPromo.jumlahPromo
            val kodepromo = dataPromo.kodePromo
            total_promo.setText("Promo Anda Rp $promo")

            val a = promo.toString().toInt()
            val b = total_order.text.toString().toInt()
            val c = b-a

            total_order.text = c.toString()
            test_kode_promo.text = kodepromo.toString()
        }
    }

    companion object {
        const val REQUEST_CODE = 2502
    }
}