package com.example.proyectomedidor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectomedidor.Models.AdaptableRegistro
import com.example.proyectomedidor.Models.AdaptableTarea
import com.example.proyectomedidor.Models.Registro
import com.example.proyectomedidor.Models.Tarea
import com.example.proyectomedidor.Utils.SessionManager
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TareasActivity : AppCompatActivity() {

    private lateinit var recView: RecyclerView
    private lateinit var adaptable: AdaptableTarea
    private lateinit var apiClient: ApiClient
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tareas)

        recView = findViewById(R.id.recViewTareas)

        apiClient = ApiClient()

        recView.setHasFixedSize(true)
        recView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        recView.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )

        sessionManager = SessionManager(this)

        recView.itemAnimator = DefaultItemAnimator()

        getAllTareas()
    }

    private fun getAllTareas(){
        //Recibimos todas las persona
        apiClient.getApiService(this).getAllTareas(sessionManager.fetchUser()!!).enqueue(object:
            Callback<List<Tarea>> {
            override fun onResponse(
                call: Call<List<Tarea>>,
                response: Response<List<Tarea>>
            ) {
                val lista_tareas = response?.body()
                Log.i("ListarTareas", Gson().toJson(lista_tareas))

                adaptable = AdaptableTarea(lista_tareas!!) {

                }

                recView.adapter = adaptable

            }
            override fun onFailure(call: Call<List<Tarea>>, t: Throwable) {
                t?.printStackTrace()
            }
        })
    }
}