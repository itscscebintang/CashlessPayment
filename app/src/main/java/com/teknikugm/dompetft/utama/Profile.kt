package com.teknikugm.dompetft.utama

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.teknikugm.dompetft.R
import com.teknikugm.dompetft.API.ApiClient
import com.teknikugm.dompetft.API.SessionManager
import kotlinx.android.synthetic.main.activity_profile.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Profile : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    private lateinit var apiClient: ApiClient
    private lateinit var sessionManager: SessionManager

    private var PREFS_NAME = MainActivity::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? { return inflater.inflate(R.layout.activity_profile, container, false) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        apiClient = ApiClient()
        sessionManager = SessionManager(this.context!!)

        if (sessionManager.fetchAuthToken() == null) {
        }
        else {
            val activity: MainActivity = activity as MainActivity
            val profile = activity.getProfile()

            txtusername_profile.text = profile?.username
            txtid_profile.text = profile?.id.toString()
            txtemail_profile.text = profile?.email.toString()
            btn_logout.visibility = View.VISIBLE
        }

        btn_logout.setOnClickListener(){
            AlertDialog.Builder(context)
                .setTitle("Logout")
                .setMessage("Keluar dari Aplikasi DompetFT?")
                .setPositiveButton("Ya") { dialog, whichButton ->
                    signout()
                }
                .setNegativeButton("Tidak") { dialog, whichButton ->
                    startActivity(Intent(context, MainActivity::class.java))
                }
                .show()
        }

        img_myqr.setOnClickListener(){
            startActivity(Intent(context, MyQR::class.java))
        }
    }

    private fun signout(){
        sessionManager.saveAuthToken(null)
        sessionManager.saveDeviceId(0)
        startActivity(Intent(this.context,Login::class.java))
        this.activity?.finish()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Profile().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}