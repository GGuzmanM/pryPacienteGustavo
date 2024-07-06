package com.example.apppaciente.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.apppaciente.R
import com.example.apppaciente.databinding.CardviewCitaProgramadaBinding
import com.example.apppaciente.model.CitasResponse

class CitasAdapter(
    private val context: Context,
    private val citasList: List<CitasResponse.Data>,
    private val listener: CitasAdapterListener
) : RecyclerView.Adapter<CitasAdapter.CitaViewHolder>() {

    interface CitasAdapterListener {
        fun onVerDetalle(cita: CitasResponse.Data)
        fun onReprogramarCita(cita: CitasResponse.Data)
        fun onCancelarCita(cita: CitasResponse.Data)
    }

    inner class CitaViewHolder(val binding: CardviewCitaProgramadaBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitaViewHolder {
        val binding = CardviewCitaProgramadaBinding.inflate(LayoutInflater.from(context), parent, false)
        return CitaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CitaViewHolder, position: Int) {
        val cita = citasList[position]
        holder.binding.tvFechaValue.text = cita.fecha
        holder.binding.tvHoraValue.text = cita.hora
        holder.binding.tvNombreOdontologoValue.text = cita.nombre_odontologo
        holder.binding.tvMotivoConsulta.text = cita.motivo_consulta

        holder.binding.root.setOnClickListener {
            listener.onVerDetalle(cita)
        }

        holder.binding.btnOpciones.setOnClickListener {
            showPopupMenu(holder.binding.btnOpciones, cita)
        }
    }

    private fun showPopupMenu(view: View, cita: CitasResponse.Data) {
        val popup = PopupMenu(context, view)
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.menu_cita_opciones, popup.menu)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_reprogramar -> {
                    listener.onReprogramarCita(cita)
                    true
                }
                R.id.action_cancelar -> {
                    listener.onCancelarCita(cita)
                    true
                }
                else -> false
            }
        }
        popup.show()
    }

    override fun getItemCount(): Int = citasList.size
}
