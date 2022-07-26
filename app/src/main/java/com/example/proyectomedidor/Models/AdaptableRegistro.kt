package com.example.proyectomedidor.Models

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectomedidor.R

data class Registro(
    val id: Int?,
    val id_usuario:Int,
    val codigo_vivienda:String,
    val imagen:String?,
    val lectura:String?,
    val gps:String,
    val fecha_creacion:String?
)

class AdaptableRegistro (
    private val datos: List<Registro>,
    private val clickListener: (Registro) -> Unit): RecyclerView.Adapter<AdaptableRegistro.PersonViewHolder>()
{

    class PersonViewHolder(val item: View): RecyclerView.ViewHolder(item){
        val lblCodigo = item.findViewById(R.id.lblCodigoViv) as TextView
        val lblLectura = item.findViewById(R.id.lblLectura) as TextView
        val lblFecha = item.findViewById(R.id.lblFecha) as TextView

        fun bindUser(objUser: Registro){
            lblCodigo.text = objUser.codigo_vivienda
            lblLectura.text = objUser.lectura
            lblFecha.text = objUser.fecha_creacion
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val item = LayoutInflater.from(parent.context).
        inflate(R.layout.registro_items, parent, false) as LinearLayout
        return PersonViewHolder(item)
    }

    override fun getItemCount()= datos.size

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        val user = datos[position]
        holder.bindUser(user)
        holder.item.setOnClickListener{clickListener(user)};
    }
}