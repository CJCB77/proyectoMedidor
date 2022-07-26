package com.example.proyectomedidor.Models

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectomedidor.R

data class Tarea(
    val titulo:String,
    val descripcion:String,

)

class AdaptableTarea ( private val datos: List<Tarea>,
private val clickListener: (Tarea) -> Unit): RecyclerView.Adapter<AdaptableTarea.TareaViewHolder>()
{

    class TareaViewHolder(val item: View): RecyclerView.ViewHolder(item){
        val lblTitulo = item.findViewById(R.id.lblTareaTitulo) as TextView
        val lblDescripcion = item.findViewById(R.id.lblTareaDescripcion) as TextView

        fun bindTarea(objTarea: Tarea){
            lblTitulo.text = objTarea.titulo
            lblDescripcion.text = objTarea.descripcion

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TareaViewHolder {
        val item = LayoutInflater.from(parent.context).
        inflate(R.layout.tareas_items, parent, false) as LinearLayout
        return TareaViewHolder(item)
    }

    override fun getItemCount()= datos.size

    override fun onBindViewHolder(holder: TareaViewHolder, position: Int) {
        val tarea = datos[position]
        holder.bindTarea(tarea)
        holder.item.setOnClickListener{clickListener(tarea)};
    }
}

