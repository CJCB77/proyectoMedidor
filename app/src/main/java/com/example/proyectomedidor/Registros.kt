package com.example.proyectomedidor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectomedidor.Models.AdaptableRegistro
import com.example.proyectomedidor.Models.Registro
import com.example.proyectomedidor.Utils.SessionManager
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Registros : AppCompatActivity() {

    private lateinit var recView: RecyclerView
    private lateinit var adaptable: AdaptableRegistro
    private lateinit var apiClient: ApiClient
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registros)

        sessionManager = SessionManager(this)

        recView = findViewById(R.id.recViewRegistro)

        apiClient = ApiClient()

        recView.setHasFixedSize(true)
        recView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        recView.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )

        recView.itemAnimator = DefaultItemAnimator()

        getAllRegistros()

    }
    private fun getAllRegistros(){
        //Recibimos todas las persona
        apiClient.getApiService(this).getAllRegistros(sessionManager.fetchUser()!!).enqueue(object: Callback<List<Registro>> {
            override fun onResponse(
                call: Call<List<Registro>>,
                response: Response<List<Registro>>
            ) {
                val lista_personas = response?.body()
                Log.i("ListarPersonas", Gson().toJson(lista_personas))

                adaptable = AdaptableRegistro(lista_personas!!) {

                }

                recView.adapter = adaptable

            }
            override fun onFailure(call: Call<List<Registro>>, t: Throwable) {
                t?.printStackTrace()
            }
        })
    }

    fun menuHome(item: MenuItem){
        when(item.itemId){
            R.id.navigation_new -> {
             val nuevoRegistroActivity = Intent(this@Registros, NuevoRegistroActivity::class.java)
             startActivity(nuevoRegistroActivity)
            }
            R.id.navigation_home -> {
                Log.i("Salir", "Saliendo")
            }
            R.id.navigation_tareas -> {
                val tareasActivity = Intent(this@Registros, TareasActivity::class.java)
                startActivity(tareasActivity)
            }
        }
    }



}