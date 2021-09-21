package com.teknikugm.dompetft.utama

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.teknikugm.dompetft.R
import com.teknikugm.dompetft.API.ApiClient
import com.teknikugm.dompetft.model.RegisterRequest
import com.teknikugm.dompetft.model.ResponseRegister
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Register : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        btn_signup.setOnClickListener(){
            val us = editusername_register.text.toString().trim()
            val em = editemail_register.text.toString().trim()
            val pas1 = editpass_register.text.toString()
            val pas2 = editconfpass_register.text.toString()

            if (us.isEmpty()){
                editusername_register.error = "Mohon diisi"
                editusername_register.requestFocus()
                return@setOnClickListener
            }
            if (us.contains(" ",false)){
                editusername_register.error = "Jangan gunakan spasi"
                editusername_register.requestFocus()
                return@setOnClickListener
            }
            if (em.isEmpty()){
                editemail_register.error = "Mohon diisi"
                editemail_register.requestFocus()
                return@setOnClickListener
            }
            if (pas1.isEmpty()){
                editpass_register.error = "Mohon diisi"
                editpass_register.requestFocus()
                return@setOnClickListener
            }
            if (pas2.isEmpty()){
                editconfpass_register.error = "Mohon diisi"
                editconfpass_register.requestFocus()
                return@setOnClickListener
            }
            if (pas2!=pas1){
                editconfpass_register.error = "Password tidak sama"
                editconfpass_register.requestFocus()
                return@setOnClickListener
            }
            if (pas1.length < 8){
                editpass_register.error = "Minimal 8 karakter, gunakan kombinasi huruf dan angka"
                editpass_register.requestFocus()
                return@setOnClickListener
            }
            register(us,em,pas1,pas2)
        }

        txtlogin.setOnClickListener(){
            startActivity(Intent(this, Login::class.java))
        }
    }

    private fun register(Username: String, Email: String, Pass1: String, Pass2: String){
        val intent = Intent(this, Login::class.java)

        val apiClient = ApiClient()
        val userInfo = RegisterRequest(username = Username, email = Email, password1 = Pass1, password2 = Pass2)

        apiClient.getApiService(this).addUser(userInfo)
            .enqueue(object : Callback<ResponseRegister> {
                override fun onFailure(call: Call<ResponseRegister>, t: Throwable) {
                    Toast.makeText(this@Register,t.message, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<ResponseRegister>, response: Response<ResponseRegister>) {
                    val registerResponse = response.body()

                    if (registerResponse?.key != null) {
                        Toast.makeText(this@Register,"Akun berhasil didaftarkan", Toast.LENGTH_SHORT).show()
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@Register,"Username / Email telah terdaftar", Toast.LENGTH_SHORT).show()
                    }
                }
            })
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