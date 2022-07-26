package com.example.proyectomedidor

import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient {
    private lateinit var apiService: ApiService

    fun getApiService(context: Context): ApiService {
        //Inicializamos el apiService
        if (!::apiService.isInitialized) {
            val retrofit = Retrofit.Builder()
                .baseUrl("http://192.168.100.62:3000/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okhttpClient(context))
                .build()

            apiService = retrofit.create(ApiService::class.java)
        }

        return apiService
    }

    /*
    Inicializar OkHttpClient con nuestro interceptor
     */

    private fun okhttpClient(context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context))
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()
    }



}