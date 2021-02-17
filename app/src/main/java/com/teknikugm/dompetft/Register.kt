package com.teknikugm.dompetft

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.teknikugm.dompetft.retrofit.API
import com.teknikugm.dompetft.retrofit.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_register.*

class Register : AppCompatActivity() {

    lateinit var myAPI: API
    var compositeDisposable = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val retrofit = RetrofitClient.instance
        myAPI = retrofit.create(API::class.java)

        btn_signup.setOnClickListener(){
            registerData(
                editname_register.text.toString(),
                editusername_register.text.toString(),
                editpass_register.text.toString()
            )
        }

    }

    private fun registerData(username: String, name: String, password: String) {
        compositeDisposable.add(myAPI.registeruser(username, name,password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { pesan ->
                Toast.makeText(this@Register, pesan, Toast.LENGTH_LONG).show()
            })
//        Kosongkan_teks()
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