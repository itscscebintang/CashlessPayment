package com.teknikugm.dompetft.utama

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.teknikugm.dompetft.R
import com.teknikugm.dompetft.pembayaran.Scanner
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnscan.setOnClickListener(){
            startActivity(Intent(this,Scanner::class.java))
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, Home())
            .commit()

        bottomnav.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, Home())
                        .commit()
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.profile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, Profile())
                        .commit()
                    return@setOnNavigationItemSelectedListener true
                }

            }
            false
        }

    }

    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setMessage("Tutup Aplikasi DompetFT?")
            .setPositiveButton("Ya") { dialog, whichButton ->
                finishAffinity()
            }
            .setNegativeButton("Batal") { dialog, whichButton ->

            }
            .show()
    }

}