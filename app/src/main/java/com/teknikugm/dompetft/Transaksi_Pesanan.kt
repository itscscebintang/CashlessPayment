package com.teknikugm.dompetft

import android.app.Activity
import android.content.ContextWrapper
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.CaptureActivity
import com.teknikugm.dompetft.pembayaran.DataItem
import com.teknikugm.dompetft.pembayaran.Promo
import com.teknikugm.dompetft.retrofit.Constant
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_pay__canteen__q_r.*
import kotlinx.android.synthetic.main.activity_transaksi__pesanan.*

class Transaksi_Pesanan : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_transaksi__pesanan, container, false)
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_promo.setOnClickListener(){
            startActivity(Intent(context, Promo::class.java))
        }

        pesanan.setOnClickListener(){
            Scan_QR_Code()
        }

    }

    fun Scan_QR_Code(){

        val integrator = IntentIntegrator.forSupportFragment(this).apply {
            captureActivity = CaptureActivity::class.java
            setOrientationLocked(false)
            setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
            setPrompt("")
        }
        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK){
            val dataPromo = data?.getSerializableExtra("promo") as DataItem
            Toast.makeText(context, dataPromo.jumlahPromo, Toast.LENGTH_SHORT).show()
            total_promo.text= dataPromo.jumlahPromo
        }

//        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
//        if (result != null) {
//            if (result.contents == null) Toast.makeText(context, "Cancelled", Toast.LENGTH_LONG).show()
//            else total_promo.text=result.contents
//        } else {
//            super.onActivityResult(requestCode, resultCode, data)
//        }
    }

}