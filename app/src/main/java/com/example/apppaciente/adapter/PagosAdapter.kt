package com.example.apppaciente.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.apppaciente.R
import com.example.apppaciente.model.PagosResponse

class PagosAdapter(
    private val context: Context,
    private val citasList: List<PagosResponse.Data>,
    private val listener: PagosAdapterListener
) : RecyclerView.Adapter<PagosAdapter.PagosViewHolder>() {

    private val selectedItems = mutableSetOf<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagosViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.cardview_pago_pendiente, parent, false)
        return PagosViewHolder(view)
    }

    override fun onBindViewHolder(holder: PagosViewHolder, position: Int) {
        val cita = citasList[position]

        holder.tituloValue.text = "Cita realizada"
        holder.tvFechaValue.text = cita.fecha
        holder.tvHoraValue.text = cita.hora
        holder.tvNombreOdontologoValue.text = cita.nombre_odontologo
        holder.tvMotivoConsulta.text = cita.motivo_consulta
        holder.tvCostoTotal.text = cita.costo.toString()
        holder.tvTratamientos.text = cita.cantidad_tratamiento.toString()

        holder.itemView.setOnClickListener {
            listener.onCardViewClicked(cita)
        }
    }

    override fun getItemCount(): Int {
        return citasList.size
    }

    fun getSelectedItems(): List<PagosResponse.Data> {
        return selectedItems.map { citasList[it] }
    }

    fun selectAllItems() {
        selectedItems.clear()
        for (i in citasList.indices) {
            selectedItems.add(i)
        }
        notifyDataSetChanged()
    }

    fun clearSelection() {
        selectedItems.clear()
        notifyDataSetChanged()
    }

    inner class PagosViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tituloValue: TextView = view.findViewById(R.id.tituloValue)
        val tvFechaValue: TextView = view.findViewById(R.id.tvFechaValue)
        val tvHoraValue: TextView = view.findViewById(R.id.tvHoraValue)
        val tvNombreOdontologoValue: TextView = view.findViewById(R.id.tvNombreOdontologoValue)
        val tvMotivoConsulta: TextView = view.findViewById(R.id.tvMotivoConsulta)
        val tvCostoTotal: TextView = view.findViewById(R.id.tvCostoTotal)
        val tvTratamientos: TextView = view.findViewById(R.id.tvTratamientos)
    }

    interface PagosAdapterListener {
        fun onItemSelectionChanged(selectedItems: Set<Int>)
        fun onCardViewClicked(cita: PagosResponse.Data)
    }
}
