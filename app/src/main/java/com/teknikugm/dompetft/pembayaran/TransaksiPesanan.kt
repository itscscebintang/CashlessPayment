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

    private var result : String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaksi_pesanan)

        val uname = this.getSharedPreferences(Constant.PREFS_NAME, ContextWrapper.MODE_PRIVATE)?.getString(
            Constant.username, "noen")
        nampilinSaldo(uname)

        val totalOrder = intent.getStringExtra("hasil")
        // hasil_scan ni untuk nampilin pake delimeter tu
        hasil_scan.text = Currency.toRupiahFormat2(totalOrder.toString().toInt()).replace("$", "Rp").replace(",", ".")
        //cheat_hasilScan ni untuk perhitungan
        cheat_hasilScan.text = totalOrder
        cheat_tanpaPromo.text = totalOrder
        totalPembeli.text = Currency.toRupiahFormat2(totalOrder.toString().toInt()).replace("$", "Rp").replace(",", ".")

        relativeoren.setOnClickListener(){
            val a = cheat_hasilScan.text.toString() // ini untuk ambil total order biar bisa dibawa ke list promo, untuk dicek min belanjanya tu
            val i = Intent(this, Promo::class.java) // ini dia nge intent ke kelas promo
            i.putExtra("hasil", a) // ini untuk bawa nilainya, kek key tu extrasnya, jdi kayak nyambungin yg nilai dibawa ni nnti di activity tujuannya taroknya dimana
            i.putExtra("kode_promo", result)
            startActivityForResult((i), REQUEST_CODE) // tu ini startactivity kalo bawa nilai, jdi startactivityforresult
        }

        btn_pay_pesanan.setOnClickListener(){
            val a = cheat_hasilPromo.text.toString()
            val totalScan = cheat_hasilScan.text.toString().toInt()
            val persenanPenjual = totalScan*0.85
            val totalPenjual = Currency.toRupiahFormat2(persenanPenjual.toInt()).replace("$", "").replace(".", "").replace(",", "")
            val persenanFT = (totalScan*0.15).toInt()

            if(saldo_anda.text.toString().toInt() < cheat_tanpaPromo.text.toString().toInt()){
                Toast.makeText(this, "Saldo anda tidak cukup", Toast.LENGTH_SHORT).show()
            }else if(cheat_hasilPromo.text.toString( ).isEmpty()){
                detailTransaksi(cheat_tanpaPromo.text.toString(), totalPenjual, persenanFT.toString())
            } else {
                val totalFT = (persenanFT - a.toInt()).toString()
                detailTransaksi(cheat_totalPembeli.text.toString(), totalPenjual, totalFT)
            }
        }

        panah_transaksi.setOnClickListener(){
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

            if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
                val dataPromo = data?.getSerializableExtra("promo") as DataItem
                result = dataPromo.kodePromo
                val persentasePromo = dataPromo.persentasePromo

                test_persentase.text = persentasePromo.toString()

                val hasilScan = cheat_hasilScan.text.toString().toInt()
                val promo1 = test_persentase.text.toString().toInt()
                val promo2 = promo1*0.01
                val promo = promo2*hasilScan

                val hasildiskon = (promo1*0.01)*hasilScan

                cheat_hasilPromo.text = Currency.toRupiahFormat2(hasildiskon.toInt()).replace("$", "").replace(".", "").replace(",", "")
                jumlahPromo.text = Currency.toRupiahFormat2(hasildiskon.toInt()).replace("$", "-Rp").replace(",", ".")

                if(promo > hasilScan){
                    cheat_totalPembeli.setText("0")
                } else {
                    val hasil = hasilScan - promo
                    cheat_totalPembeli.text = Currency.toRupiahFormat2(hasil.toInt()).replace("$", "").replace(".", "").replace(",", "")
                    totalPembeli.text =  Currency.toRupiahFormat2(hasil.toInt()).replace("$", "Rp").replace(",", ".")
                }

                test_kode_promo.text = result
        }
    }

    companion object {
        const val   REQUEST_CODE = 2502
    }

    fun nampilinSaldo(key : String?){
        lateinit var myAPI: API
        val retrofit = RetrofitClient.instance
        myAPI = retrofit.create(API::class.java)

        myAPI.getsaldo(key).enqueue(object : retrofit2.Callback<ResponseSaldo>{

            override fun onFailure(call: Call<ResponseSaldo>, t: Throwable) {
                Toast.makeText(this@TransaksiPesanan, "Gagal memuat saldo", Toast.LENGTH_SHORT).show()
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

                            val a = cheat_tanpaPromo.text.toString().toInt()
                            val b = saldo_anda.text.toString().toInt()
                            val sisaSaldo = b - a

                            val d = Currency.toRupiahFormat2(a).replace("$", "Rp").replace(",", ".")
                            val e = Currency.toRupiahFormat2(sisaSaldo).replace("$", "Rp").replace(",", ".")

                            AlertDialog.Builder(this@TransaksiPesanan)
                                .setTitle("Transaksi pembayaran senilai $d berhasil ")
                                .setMessage("Saldo Anda sekarang : $e")
                                .setPositiveButton("Ok") { dialog, whichButton ->
                                    startActivity(Intent(this@TransaksiPesanan, MainActivity::class.java))
                                }
                                .setNegativeButton("Tutup") { dialog, whichButton ->
                                    startActivity(Intent(this@TransaksiPesanan, MainActivity::class.java))
                                }
                                .show()
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