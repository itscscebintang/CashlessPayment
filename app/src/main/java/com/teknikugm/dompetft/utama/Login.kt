package com.teknikugm.dompetft.utama

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.teknikugm.dompetft.R
import com.teknikugm.dompetft.API.ApiClient
import com.teknikugm.dompetft.API.SessionManager
import com.teknikugm.dompetft.model.LoginRequest
import com.teknikugm.dompetft.model.ResponseLogin
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : AppCompatActivity(){

    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sessionCheck()
        apiClient = ApiClient()
        sessionManager = SessionManager(this)

        btn_login.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)

            val us = editusername_login.text.toString().trim()
            val pas = editpassword_login.text.toString()

            if (us.isEmpty()){
                editusername_login.error = "Mohon diisi"
                editusername_login.requestFocus()
                return@setOnClickListener
            }
            if (pas.isEmpty()){
                editpassword_login.error = "Mohon diisi"
                editpassword_login.requestFocus()
                return@setOnClickListener
            }

            val userlogin = LoginRequest(username = us,password = pas)

            apiClient.getApiService(this).login(userlogin)
                .enqueue(object : Callback<ResponseLogin> {
                    override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                        Toast.makeText(this@Login,"Gagal Login\n"+t.toString(),Toast.LENGTH_LONG).show()
                        Log.d("Coba",t.toString())
                    }

                    override fun onResponse(call: Call<ResponseLogin>, response: Response<ResponseLogin>) {
                        val token = response.body()?.key

                        if (token != null) {
                            sessionManager.saveAuthToken(token)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this@Login,"Username atau password salah",Toast.LENGTH_SHORT).show()
                        }
                    }
                })
        }

        txtsignup.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun sessionCheck(){
        sessionManager = SessionManager(this)
        if (sessionManager.fetchAuthToken() != null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Tekan tombol kembali sekali lagi untuk keluar", Toast.LENGTH_SHORT).show()

        Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
    }
}