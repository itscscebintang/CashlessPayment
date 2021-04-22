package com.teknikugm.dompetft.utama

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.ContextWrapper
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.CaptureActivity
import com.teknikugm.dompetft.R
import com.teknikugm.dompetft.retrofit.*
import kotlinx.android.synthetic.main.activity_pay__canteen__q_r.*
import retrofit2.Call
import retrofit2.Response

class Pay_Canteen_QR : Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_pay__canteen__q_r, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Unit_UI()

        val a = context?.getSharedPreferences(Constant.PREFS_NAME, ContextWrapper.MODE_PRIVATE)?.getString(Constant.username, "noen")
        nampilinSaldo(a)

        btn_transfer_saldo.setOnClickListener(){

            if (saldocontoh_qr.text.toString().toInt() < edit_amount.text.toString().toInt()){
                Toast.makeText(context, "Saldo Anda tidak cukup", Toast.LENGTH_SHORT).show()
            } else{
                if (edit_amount.text.toString().toInt() < 5000){
                    Toast.makeText(context, "Transaksi minimal Rp 5000", Toast.LENGTH_SHORT).show()
                } else {
                    transaksi()
                }
            }
        }
    }

    fun Unit_UI (){
        Scan_QR_Code()
    }

    fun Scan_QR_Code(){

        val integrator = IntentIntegrator.forSupportFragment(this).apply {
            captureActivity = CaptureActivity::class.java
            setOrientationLocked(true)
            setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
            setPrompt("Scan Payment User ID")
        }
        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) Toast.makeText(context, "Cancelled", Toast.LENGTH_LONG).show()
            else txtuname_payqr.text=result.contents
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun transaksi(){

        val preferences = context?.getSharedPreferences(Constant.PREFS_NAME, ContextWrapper.MODE_PRIVATE)?.getString(Constant.username, "noen")
        val uname_to_transaksi = txtuname_payqr.text.toString()
        val amount_transaksi = edit_amount.text.toString()

        class Addtransaction : AsyncTask<Void, Void, String>(){

            lateinit var loading : ProgressDialog

            override fun onPreExecute() {
                super.onPreExecute()
                loading=  ProgressDialog.show(context, "menambahkan", "tunggu", false, false)
            }

            override fun doInBackground(vararg params: Void?): String {

                val params= HashMap<String, String?>()
                params ["username"]= preferences
                params ["jumlah_payment"]= amount_transaksi
                params ["username_to"] = uname_to_transaksi

                val rh = RequestHandler()
                Log.d("Params", params.entries.toString())
                return rh.sendPostRequest("http://192.168.18.158/Kantin/index.php/Transaksi_saldo/transaksi",params)
            }

            override fun onPostExecute(result: String?) {
                super.onPostExecute(result)
                loading.dismiss()


                AlertDialog.Builder(context)
                    .setMessage("Transaksi sebesar $amount_transaksi berhasil")
                    .setPositiveButton(android.R.string.ok) { dialog, whichButton ->
                        clearData()
                    }
                    .show()
            }
        }

        val f = Addtransaction()
        f.execute()

    }

    fun clearData(){
        txtuname_payqr.text = ""
        edit_amount.text.clear()
    }

    fun nampilinSaldo(key : String?){
        lateinit var myAPI: API
        val retrofit = RetrofitClient.instance
        myAPI = retrofit.create(API::class.java)

        myAPI.getsaldo(key).enqueue(object : retrofit2.Callback<ResponseSaldo>{

            override fun onFailure(call: Call<ResponseSaldo>, t: Throwable) {
                Toast.makeText(context, "Gagal", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<ResponseSaldo>, response: Response<ResponseSaldo>) {
                val a = response.body()?.balance
                saldocontoh_qr.text = a.toString()

            }
        })
    }
}