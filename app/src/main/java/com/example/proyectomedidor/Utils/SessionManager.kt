package com.example.proyectomedidor.Utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private var prefs: SharedPreferences = context.getSharedPreferences("authtoken", Context.MODE_PRIVATE)

    //Save user id
    fun saveUser(userId: Int) {
        val editor = prefs.edit()
        editor.putInt("user",userId)
        editor.commit()
    }
    //Fetch user id
    fun fetchUser(): Int? {
        return prefs.getInt("user",0)
    }

    //Save token
    fun saveAuthToken(token: String) {
        val editor = prefs.edit()
        editor.putString("token",token)
        editor.commit()
    }

    //Fetch token
    fun fetchAuthToken(): String? {
        return prefs.getString("token",null)
    }
}