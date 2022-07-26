package com.example.proyectomedidor

import android.content.Context
import com.example.proyectomedidor.Utils.SessionManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(context: Context): Interceptor {
    private val sessionManager = SessionManager(context)

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        //Si el token esta guardado, lo agregamos a las request
        sessionManager.fetchAuthToken()?.let {
            requestBuilder.addHeader("authorization","$it")
        }

        return chain.proceed(requestBuilder.build())
    }
}