package com.teknikugm.dompetft.utama

import android.app.AlertDialog
import android.content.ContextWrapper
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import com.teknikugm.dompetft.R
import com.teknikugm.dompetft.retrofit.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_profile.*

class Profile : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_profile, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        txtusername_profile.text = context?.getSharedPreferences(Constant.PREFS_NAME, ContextWrapper.MODE_PRIVATE)?.getString(Constant.username, "None")
        txtname_profile.text = context?.getSharedPreferences(Constant.PREFS_NAME, ContextWrapper.MODE_PRIVATE)?.getString(Constant.name, "None")
        txtnik_profile.text = context?.getSharedPreferences(Constant.PREFS_NAME, ContextWrapper.MODE_PRIVATE)?.getString(Constant.nik, "None")
        txtemail_profile.text = context?.getSharedPreferences(Constant.PREFS_NAME, ContextWrapper.MODE_PRIVATE)?.getString(Constant.email, "None")

        btn_logout.setOnClickListener(){

            AlertDialog.Builder(context )
                .setMessage("Yakin untuk logout?")
                    .setPositiveButton("Ya") { dialog, whichButton ->
                    context?.getSharedPreferences(Constant.PREFS_NAME, ContextWrapper.MODE_PRIVATE)?.edit{clear()}
                    startActivity(Intent(context, Login::class.java))
                }
                .setNegativeButton("Batal") { dialog, whichButton ->
                }
                .show()

        }

        card_qr.setOnClickListener(){
            startActivity(Intent(context, MyQR::class.java))
        }
    }
}