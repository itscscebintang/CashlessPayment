package com.teknikugm.dompetft.utama

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.teknikugm.dompetft.R
import com.teknikugm.dompetft.retrofit.API
import com.teknikugm.dompetft.retrofit.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class Register : AppCompatActivity() {

    lateinit var myAPI: API
    var compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val retrofit = RetrofitClient.instance
        myAPI = retrofit.create(API::class.java)

        btn_signup.setOnClickListener() {
            if (editname_register.text.toString().isEmpty()) {
                editname_register.error = "Masukkan Nama Anda"
                editname_register.requestFocus()
                return@setOnClickListener
            } else if (editusername_register.text.toString().isEmpty()) {
                editusername_register.error = "Masukkan Username Anda"
                editusername_register.requestFocus()
                return@setOnClickListener
            } else if(editemail_register.text.toString().isEmpty()) {
                editemail_register.error = "Masukkan E-mail Anda"
                editemail_register.requestFocus()
                return@setOnClickListener
            } else if(editktm_register.text.toString().isEmpty()){
                editktm_register.error = "Masukkan NIK / NIM Anda"
                editktm_register.requestFocus()
                return@setOnClickListener
            }else if(editpass_register.text.toString().isEmpty()){
              editpass_register.error = "Masukkan Password Anda"
              editpass_register.requestFocus()
              return@setOnClickListener
            }else
                registerData(
                editname_register.text.toString(),
                editusername_register.text.toString(),
                    editemail_register.text.toString(),
                    editktm_register.text.toString(),
                editpass_register.text.toString()
            )
            
        }
    }

    private fun registerData(username: String, name: String, password: String, email:String, nik:String) {
        compositeDisposable.add(myAPI.registeruser(name, username, email, nik, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { pesan ->
                Toast.makeText(this@Register, pesan, Toast.LENGTH_LONG).show()
                startActivity(Intent(this@Register, Login::class.java))
            })
    }

    override fun onStop() {
        compositeDisposable.clear()
        super.onStop()
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }


}