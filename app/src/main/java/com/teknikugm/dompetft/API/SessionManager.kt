package com.teknikugm.dompetft.API

import android.content.Context
import android.content.SharedPreferences
import com.teknikugm.dompetft.R
import com.teknikugm.dompetft.model.DataUser

class SessionManager(context: Context) {

    private var prefs: SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    companion object {
        const val USER_TOKEN = "Token"
        const val DEVICE_ID = "id"
    }

    // untuk nyimpan token
    fun saveAuthToken(token: String?) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    // untuk ambil token
    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    // untuk nyimpan data user
    fun saveProfile(profileM: DataUser?) {
        val editor = prefs.edit()
        editor.putString("id", profileM?.id)
        editor.putString("username", profileM?.username)
        editor.putString("saldo", profileM?.saldo)
        editor.apply()
    }

    // untuk ambil data user
    fun getProfile(): DataUser{
        val profileM = DataUser()
        profileM.id = prefs.getString("id", "")
        profileM.username = prefs.getString("username", "")
        profileM.saldo = prefs.getString("saldo", "")

        return profileM
    }

    // untuk nyimpan id device
    fun saveDeviceId(id: Int?) {
        val editor = prefs.edit()
        if (id != null) {
            editor.putInt(DEVICE_ID, id)
        }
        editor.apply()
    }

    fun saveUsername(username: String?) {
        val editor = prefs.edit()
        editor.putString("username", username)
        editor.apply()
    }

}