package com.teknikugm.dompetft.utama

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.budiyev.android.codescanner.*
import com.teknikugm.dompetft.R
import kotlinx.android.synthetic.main.activity_scanner__transfer.*

class Scanner_Transfer : AppCompatActivity() {

    private lateinit var codeScanner : CodeScanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner__transfer)

        scanner()

        panah_scanner.setOnClickListener(){
            finish()
        }

        using_username.setOnClickListener(){
            startActivity(Intent(this, TransferSaldo::class.java))
        }
    }

    fun scanner(){

        val scannerView = findViewById<CodeScannerView>(R.id.scanner_view)

        codeScanner = CodeScanner(this, scannerView)

        // Parameters (default values)
        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
            formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
            // ex. listOf(BarcodeFormat.QR_CODE)
            autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
            scanMode = ScanMode.CONTINUOUS // or CONTINUOUS or PREVIEW
            isAutoFocusEnabled = true // Whether to enable auto focus or not
            isFlashEnabled = false // Whether to enable flash or not
        }

        // CallbackscodeS
        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                try {
                    val a = it.text
                    if (a!= null && a!==""){
                        val i = Intent(this, TransferSaldoScan::class.java)
                        i.putExtra("SCAN", a)
                        startActivity(i)
                    }
                } catch (e: Exception){
                    e.printStackTrace()
                    Toast.makeText(application, "ERROR, TRY AGAIN !", Toast.LENGTH_SHORT).show()
                }
            }

            finish()
        }

        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            runOnUiThread {
                Toast.makeText(
                    this, "Camera initialization error: ${it.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }
}