package com.example.apppaciente.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.apppaciente.R
import com.example.apppaciente.databinding.CardviewCitaProgramadaBinding
import com.example.apppaciente.databinding.CardviewNotificacionBinding
import com.example.apppaciente.databinding.FragmentNotificacionBinding
import com.example.apppaciente.model.CitasResponse
import com.example.apppaciente.model.LoginResponse
import com.example.apppaciente.model.NotificacionResponse
import com.example.apppaciente.view.NotificacionFragment


class NotificacionAdapter (
    private val context: Context,
    private val notificacion_list: List<NotificacionResponse.Data>,
    private val listener: NotificacionAdapter.NotificacionAdapterListener,
) : RecyclerView.Adapter<NotificacionAdapter.NotificacionViewHolder>() {

    inner class NotificacionViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val txtFecha: TextView = view.findViewById(R.id.txtFecha)
        val txtMensaje: TextView = view.findViewById(R.id.txtMensaje)
        val txtEstado: TextView = view.findViewById(R.id.txtEstado)
        val leer: LinearLayout = view.findViewById(R.id.leer)
        val visto: ImageView = view.findViewById(R.id.imgVisto)

        val noti_estado: CheckBox = view.findViewById(R.id.checknotificacion)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificacionAdapter.NotificacionViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.cardview_notificacion, parent, false)
        return NotificacionViewHolder(view)
    }
    override fun onBindViewHolder(holder: NotificacionViewHolder, position: Int) {
        val noti = notificacion_list[position]


        holder.txtFecha.text = noti.fecha
        holder.txtMensaje.text = noti.mensaje
        if (noti.leida == 0){
            holder.txtEstado.text = "No Leída"
            holder.leer.setBackgroundColor(context.getColor(R.color.Salmon))
            holder.visto.setImageDrawable(context.getDrawable(R.drawable.novisto))

        }else{
            holder.txtEstado.text = "Leída"
            holder.leer.setBackgroundColor(context.getColor(R.color.LightGreen))

        }

        holder.leer.setOnClickListener {
            listener.leerNootificacion(noti)
        }

        holder.noti_estado.setOnClickListener {
            listener.cambiar_estado_notificacion(noti)
        }


        holder.itemView.setOnClickListener {
            listener.onCardViewClicked(noti)
        }
    }




    override fun getItemCount(): Int {
        return notificacion_list.size
    }



    interface NotificacionAdapterListener {
        fun onItemSelectionChanged(selectedItems: Set<Int>)
        fun onCardViewClicked(noti: NotificacionResponse.Data)
        fun leerNootificacion(noti: NotificacionResponse.Data)
        fun cambiar_estado_notificacion(noti: NotificacionResponse.Data)

    }
}



