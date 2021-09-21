package com.teknikugm.dompetft.utama

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.teknikugm.dompetft.API.ApiClient
import com.teknikugm.dompetft.API.SessionManager
import com.teknikugm.dompetft.R
import kotlinx.android.synthetic.main.activity_my_q_r.*

class MyQR : AppCompatActivity() {

    private lateinit var apiClient : ApiClient
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_q_r)

        apiClient = ApiClient()
        sessionManager = SessionManager(this)

        if(sessionManager.fetchAuthToken() == null){}
        else{
            val profile = sessionManager.getProfile()
            val username = profile.username

            if(username != null){
                val bitmap = generateQRCode(username)
                img_myqr.setImageBitmap(bitmap)
            }
        }

        panah_myqr.setOnClickListener(){
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private  val TAG = "MainActivity"
    private fun generateQRCode(text: String): Bitmap {
        val width = 500
        val height = 500
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val codeWriter = MultiFormatWriter()
        try {
            val bitMatrix = codeWriter.encode(text, BarcodeFormat.QR_CODE, width, height)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else resources.getColor(R.color.background))
                }
            }
        } catch (e: WriterException) { Log.d(TAG, "generateQRCode: ${e.message}") }
        return bitmap
    }
}