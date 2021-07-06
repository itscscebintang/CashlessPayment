package com.teknikugm.dompetft.utama

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.teknikugm.dompetft.R
import com.teknikugm.dompetft.pembayaran.Scanner
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var doubleBackToExitPressedOnce: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ContextCompat.checkSelfPermission(this@MainActivity,
                Manifest.permission.CAMERA) !==
            PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this@MainActivity,
                    Manifest.permission.CAMERA)) {
                ActivityCompat.requestPermissions(this@MainActivity,
                    arrayOf(Manifest.permission.CAMERA), 1)
            } else {
                ActivityCompat.requestPermissions(this@MainActivity,
                    arrayOf(Manifest.permission.CAMERA), 1)
            }
        }

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
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }
        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show()
        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                    if ((ContextCompat.checkSelfPermission(this@MainActivity,
                            Manifest.permission.CAMERA) ===
                                PackageManager.PERMISSION_GRANTED)) {
                    }
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }

}