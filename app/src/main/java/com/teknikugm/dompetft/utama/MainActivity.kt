package com.teknikugm.dompetft.utama

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.teknikugm.dompetft.R
import com.teknikugm.dompetft.API.ApiClient
import com.teknikugm.dompetft.API.SessionManager
import com.teknikugm.dompetft.model.DataUser
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var apiClient: ApiClient
    private lateinit var sessionManager: SessionManager
    var profilResponse: DataUser? = DataUser("","",null,null)
    private var i = 0
    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pbmain.visibility = View.VISIBLE
        i = pbmain.progress
        Thread(Runnable {
            // this loop will run until the value of i becomes 99
            while (i < 100) {
                i += 1
                // Update the progress bar and display the current value
                handler.post(Runnable {
                    pbmain.progress = i
                    // setting current progress to the textview
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, Home())
                        .commit()
                })
                try {
                    Thread.sleep(100)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
            pbmain.visibility = View.INVISIBLE

        }).start()

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
            startActivity(Intent(this, Scanner::class.java))
        }

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

    //REVISI
    fun getProfile(): DataUser? {
        apiClient = ApiClient()
        sessionManager = SessionManager(this)

        apiClient.getApiService(this).getProfile()
            .enqueue(object : Callback<DataUser> {
                override fun onFailure(call: Call<DataUser>, t: Throwable) {}

                override fun onResponse(call: Call<DataUser>, response: Response<DataUser>) {
                    profilResponse = response.body()
                    sessionManager.saveUsername(profilResponse?.username)
                    sessionManager.saveProfile(profilResponse)
                }
            })
        return profilResponse
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