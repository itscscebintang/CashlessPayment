package com.teknikugm.dompetft.utama

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import com.google.gson.Gson
import com.teknikugm.dompetft.API.ApiClient
import com.teknikugm.dompetft.R
import com.teknikugm.dompetft.API.Currency
import com.teknikugm.dompetft.API.SessionManager
import com.teknikugm.dompetft.model.BayarItem
import com.teknikugm.dompetft.model.ResponsePromo
import com.teknikugm.dompetft.model.ResponseScan
import com.teknikugm.dompetft.model.ResponseTransaksi
import kotlinx.android.synthetic.main.activity_transaksi_pesanan.*
import retrofit2.Call
import retrofit2.Response

class TransaksiPesanan : AppCompatActivity() {

    private var result : String? = null
    private var idPromo : Int? = null
    private var persendiskon : Int? = null
    private var potongandiskon : Int? = null
    private var totalBayar : Int? = null
    private var saldo : String? = null
    private var totaltext : String? = null
    private var kodePromo : String?= null
    private val key = "hasil"
    private lateinit var sessionManager : SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaksi_pesanan)

        val x = intent.extras
        result = x?.getString(key)
        val isi_pesanan = result

        // untuk ngambil saldo
        sessionManager = SessionManager(this)
        if (sessionManager.fetchAuthToken() == null){ }
        else {
            val dataUser = sessionManager.getProfile()
            saldo = dataUser.saldo
        }

        // untuk print data ke log (untuk tau respon qr code tu apa)
//        Log.d("TAG", "$isi_pesanan")
//        print(result)

        val gson = Gson()
        val responScan1 = gson.fromJson(result, ResponseScan::class.java)
//        isipesanan.text = result

        totaltext = responScan1.jumlahPesanan.toString()
        totalPembeli.text = Currency.toRupiahFormat2(totaltext!!.toInt()).replace("$", "Rp").replace(",",".")
        hasil_scan.text = Currency.toRupiahFormat2(totaltext!!.toInt()).replace("$", "Rp").replace(",",".")

        // untuk dapatin list daftar menu
        var resMenu = responScan1.daftarMenu
        var array = mutableListOf<String?>()
        resMenu?.forEach{
            array.add(it?.daftarMenu)
        }

        val adapter = ArrayAdapter(this,
            R.layout.menu, array)
        val listView:ListView = findViewById(R.id.listmenu)
        listView.setAdapter(adapter)

        btn_pay_pesanan.setOnClickListener(){
            if (saldo!!.toInt() < responScan1.jumlahPesanan!!.toInt()){
                AlertDialog.Builder(this)
                    .setTitle("Saldo Anda tidak cukup")
                    .setMessage("Isi ulang saldo Anda sekarang")
                    .setPositiveButton("Top Up") { dialog, whichButton ->
                        startActivity(Intent(this, TopUp::class.java))
                    }
                    .setNegativeButton("Tutup") { dialog, whichButton ->
                        startActivity(Intent(this, MainActivity::class.java))
                    }
                    .show()
            } else {
                val tipeTransaksi = 1
                bayarKantin(tipeTransaksi)
            }
        }

        relativeoren.setOnClickListener(){
            val i = Intent(this, Promo::class.java)
            startActivityForResult(i, REQUEST_CODE)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
            val dataPromo = data?.getSerializableExtra(Promo.DATA_PROMO) as ResponsePromo
            idPromo = dataPromo.id
            persendiskon = dataPromo.persentasePromo
            kodePromo = dataPromo.kodePromo

            test_kode_promo.text = kodePromo.toString()
            test_persentase.text = persendiskon.toString()

            val persenDiskon = 0.01 * persendiskon.toString().toInt()
            val hasilAkhir = totaltext.toString().toInt() * persenDiskon
            jumlahPromo.text = Currency.toRupiahFormat2(hasilAkhir.toInt()).replace("$", "-Rp").replace(",",".")
            val b = totaltext.toString().toInt() - hasilAkhir.toString().replace(".0", "").toInt()
            totalPembeli.text = Currency.toRupiahFormat2(b).replace("$", "Rp").replace(",",".")
        }
    }

    fun bayarKantin(tipetransaksi : Int){
        ApiClient().getApiService(this).transaksi(tipetransaksi)
            .enqueue(object : retrofit2.Callback<ResponseTransaksi>{
                override fun onResponse(call: Call<ResponseTransaksi>, response: Response<ResponseTransaksi>) {
                    val respon = response.body()
                    if (response.isSuccessful){
                        val gsonDua = Gson()
                        val responScanDua = gsonDua.fromJson(result, ResponseScan::class.java)

                        val totalAsli = responScanDua.jumlahPesanan!!.toInt()
                        val idPenjual = responScanDua.penjual
                        val diskon = idPromo
                        val idTransaksi = respon!!.id!!.toInt()

                        if(idPromo != null){
                            potongandiskon = persendiskon
                            val besarDiskon = 0.01 * potongandiskon!!
                            val potonganBayar = besarDiskon * totalAsli
                            totalBayar = totalAsli - potonganBayar.toInt()
                        } else {
                            potongandiskon = 0
                            totalBayar = totalAsli - potongandiskon!!
                        }

                        val saldoTerkini = saldo?.toInt()!! - totalBayar!!
                        jumlahPromo.text = potongandiskon.toString()

                        val sadoSekarang = Currency.toRupiahFormat2(saldoTerkini).replace("$","").replace(",",".")
                        val totalbayar = Currency.toRupiahFormat2(totalBayar!!).replace("$","").replace(",",".")

                        AlertDialog.Builder(this@TransaksiPesanan)
                            .setTitle("Transaksi pesanan sebesar Rp$totalbayar berhasil")
                            .setMessage("Saldo Anda sekarang Rp$sadoSekarang")
                            .setPositiveButton("Ok") { dialog, whichButton ->
                                startActivity(Intent(this@TransaksiPesanan, MainActivity::class.java))
                            }
                            .setNegativeButton("Tutup") { dialog, whichButton ->
                                startActivity(Intent(this@TransaksiPesanan, MainActivity::class.java))
                            }
                            .show()

                        ApiClient().getApiService(this@TransaksiPesanan).bayar(totalAsli, totalBayar, idPenjual, diskon, idTransaksi)
                            .enqueue(object : retrofit2.Callback<BayarItem>{
                                override fun onResponse(
                                    call: Call<BayarItem>,
                                    response: Response<BayarItem>
                                ) {
                                    ApiClient().getApiService(this@TransaksiPesanan)
                                    Toast.makeText(applicationContext, response.message(), Toast.LENGTH_SHORT).show()
                                }

                                override fun onFailure(call: Call<BayarItem>, t: Throwable) {
                                    Toast.makeText(applicationContext, response.message(), Toast.LENGTH_SHORT).show()
                                }
                            })
                        Toast.makeText(applicationContext, response.message(), Toast.LENGTH_SHORT).show()

                    } else {
                        Toast.makeText(applicationContext, response.message(), Toast.LENGTH_SHORT).show()
                    }
                    print(response.body())
                }

                override fun onFailure(call: Call<ResponseTransaksi>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()
                    t.printStackTrace()
                }

            })
    }

    companion object {
        const val REQUEST_CODE = 2502
    }
}