package com.example.proyectomedidor

import com.example.proyectomedidor.Models.Registro
import com.example.proyectomedidor.Models.Tarea
import com.example.proyectomedidor.Requests.LoginRequest
import com.example.proyectomedidor.Response.LoginResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("registros/")
    fun getAllRegistros(@Query("id") id:Int): Call<List<Registro>>

    @GET("tareas/")
    fun getAllTareas(@Query("id") id: Int): Call<List<Tarea>>

    @Multipart
    @POST("registros/add")
    fun createRegistro(@Part imagen:MultipartBody.Part,
                       @Part("id_usuario") idUsuario:RequestBody,
                       @Part("codigo_vivienda") codigoVivienda:RequestBody,
                       @Part("gps") gps: RequestBody,
                       ):Call<Registro>


    @POST("auth/login/")
    fun loginUser(@Body user: LoginRequest?): Call<LoginResponse>

}