package com.teknikugm.dompetft

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.teknikugm.dompetft.pembayaran.DataItem
import com.teknikugm.dompetft.pembayaran.Promo
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
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK){
            val dataPromo = data?.getSerializableExtra("promo") as DataItem
            Toast.makeText(context, dataPromo.jumlahPromo, Toast.LENGTH_SHORT).show()
//            total_promo.text= dataPromo.jumlahPromo
        }
    }
}