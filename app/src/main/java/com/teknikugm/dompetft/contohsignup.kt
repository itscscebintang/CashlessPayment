package com.teknikugm.dompetft

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.teknikugm.dompetft.contoh.APIsignup
import com.teknikugm.dompetft.contoh.RetrofitRegister
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_contohsignup.*

class contohsignup : AppCompatActivity() {

    lateinit var myAPIR : APIsignup
    var compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contohsignup)

        val retrofit = RetrofitRegister.instance
        myAPIR = retrofit.create(APIsignup::class.java)

        btn_signupcontoh.setOnClickListener(){
            registerAkun(
                editnama_contoh.text.toString(),
                editusername_contoh.text.toString(),
                editpass_contoh.text.toString()
            )
        }
    }

    fun registerAkun (username : String, name : String, password : String) {
        compositeDisposable.add(myAPIR.registeruser(username, name,password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { pesan -> Toast.makeText(this@contohsignup, pesan, Toast.LENGTH_LONG).show()
            })
//        Kosongkan_teks()
    }
}
