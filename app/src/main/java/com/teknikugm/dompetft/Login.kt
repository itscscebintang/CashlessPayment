package com.teknikugm.dompetft

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.edit
import com.teknikugm.dompetft.retrofit.*
import kotlinx.android.synthetic.main.activity_login2.*
import retrofit2.Response

class Login : AppCompatActivity() {

    lateinit var myAPI: API
    lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login2)

        txtsignup.setOnClickListener(){
            startActivity(Intent(this, Register::class.java))
        }

        preferences = getSharedPreferences(Constant.PREFS_NAME, Context.MODE_PRIVATE)

        val retrofit = RetrofitClient.instance
        myAPI = retrofit.create(API::class.java)

        preferences = getSharedPreferences(Constant.PREFS_NAME, Context.MODE_PRIVATE)

        btn_login.setOnClickListener {
            if (editusername_login.text.toString().isEmpty()) {
                editusername_login.error = "Masukkan Username"
                editusername_login.requestFocus()
                return@setOnClickListener
            } else if (editpassword_login.text.toString().isEmpty()) {
                editpassword_login.error = "Masukkan Password"
                editpassword_login.requestFocus()
                return@setOnClickListener
            } else
                cekLogin(editusername_login.text.toString(), editpassword_login.text.toString())
//                Kosongkan_teks()
        }
    }

    private fun cekLogin(username: String, password: String) {

        myAPI.loginUser(username, password).enqueue(object : retrofit2.Callback<ResponseLogin> {

                override fun onFailure(call: retrofit2.Call<ResponseLogin>, t: Throwable) {
                    Toast.makeText(
                        this@Login,
                        "Username dan Password salah !",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onResponse(
                    call: retrofit2.Call<ResponseLogin>,
                    response: Response<ResponseLogin>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body()?.data
                        preferences.edit(true) {
                            putString(Constant.name, data?.namaUser)
                            putString(Constant.password, data?.passwordUser)
                            putString(Constant.username, data?.kodeUser)
                            putString(Constant.balance, data?.saldo)
                        }
                        val main = Intent(this@Login, MainActivity::class.java)
                        startActivity(main)
                    } else {
                        Toast.makeText(this@Login, response.message(), Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            })
    }
}