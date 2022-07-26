package com.example.proyectomedidor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.proyectomedidor.Models.Registro
import com.example.proyectomedidor.Requests.LoginRequest
import com.example.proyectomedidor.Response.LoginResponse
import com.example.proyectomedidor.Utils.SessionManager
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var username: TextView
    private lateinit var password: TextView
    private lateinit var apiClient: ApiClient
    private lateinit var sessionManager: SessionManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        username = findViewById(R.id.inputUsername)
        password = findViewById(R.id.inputPassword)
        apiClient = ApiClient()
        sessionManager = SessionManager(this)
    }

    private fun loginUser(username:String, password:String){
        var objUser: LoginRequest? = LoginRequest(username,password)
        apiClient.getApiService(this).loginUser(objUser).enqueue(object: Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                val loginResponse = response?.body()
                Log.i("Body", Gson().toJson(loginResponse))

                if(loginResponse === null) {
                    Toast.makeText(
                        applicationContext,
                        "Usuario o contraseña incorrecta", Toast.LENGTH_SHORT
                    ).show()
                }else{
                    Toast.makeText(
                        applicationContext,
                        "Iniciando sesion...", Toast.LENGTH_SHORT
                    ).show()
                    val activityRegistros = Intent(this@LoginActivity, Registros::class.java)
                    sessionManager.saveAuthToken(loginResponse.token!!)
                    startActivity(activityRegistros)
                }

            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                t?.printStackTrace()
            }
        })
    }


    fun login(view: View){
        loginUser(username.text.toString(),password.text.toString())
    }


}