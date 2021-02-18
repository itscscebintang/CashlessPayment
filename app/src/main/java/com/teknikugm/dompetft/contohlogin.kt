package com.teknikugm.dompetft

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.edit
import com.teknikugm.dompetft.contoh.APIlogin
import com.teknikugm.dompetft.contoh.ConstantLogin
import com.teknikugm.dompetft.contoh.ResponseLoginContoh
import com.teknikugm.dompetft.retrofit.Constant
import com.teknikugm.dompetft.retrofit.ResponseLogin
import com.teknikugm.dompetft.retrofit.RetrofitClient
import kotlinx.android.synthetic.main.activity_contohlogin.*
import kotlinx.android.synthetic.main.activity_contohsignup.*
import retrofit2.Response

class contohlogin : AppCompatActivity() {

    lateinit var APIL : APIlogin
    lateinit var preferences : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contohlogin)

        val retrofit = RetrofitClient.instance
        APIL = retrofit.create(APIlogin::class.java)
        preferences = getSharedPreferences(ConstantLogin.PREFS_NAME, Context.MODE_PRIVATE)

        btn_logcontoh.setOnClickListener(){
           logincontoh(edituname_logcontoh.text.toString(), editpass_logcontoh.text.toString())
        }

        txtsignup_logcontoh.setOnClickListener(){
            startActivity(Intent(this, contohsignup::class.java))
        }
    }

    fun logincontoh(username : String, password : String){
        APIL.loginUser(username, password).enqueue(object : retrofit2.Callback<ResponseLoginContoh> {

            override fun onFailure(call: retrofit2.Call<ResponseLoginContoh>, t: Throwable) {
                Toast.makeText(
                    this@contohlogin,
                    "Username dan Password salah !",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onResponse(
                call: retrofit2.Call<ResponseLoginContoh>,
                response: Response<ResponseLoginContoh>)
            {
                if (response.isSuccessful) {
                    val data = response.body()?.data
                    preferences.edit(true) {
                        putString(ConstantLogin.name, data?.namaUser)
                        putString(ConstantLogin.password, data?.passwordUser)
                        putString(ConstantLogin.username, data?.kodeUser)
                        putString(ConstantLogin.balance, data?.saldo)
                    }
                    val main = Intent(this@contohlogin, MainActivity::class.java)
                    startActivity(main)
                } else {
                    Toast.makeText(this@contohlogin, response.message(), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })
    }
}