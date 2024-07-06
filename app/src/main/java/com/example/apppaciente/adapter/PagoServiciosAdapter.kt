package com.example.apppaciente.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.apppaciente.R
import com.example.apppaciente.model.ServiciosResponse

class PagoServiciosAdapter(
    private val context: Context,
    private val serviciosList: List<ServiciosResponse.Data>,
    private val listener: PagoServiciosAdapterListener
) : RecyclerView.Adapter<PagoServiciosAdapter.PagoServiciosViewHolder>() {

    private val selectedItems = mutableSetOf<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagoServiciosViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.cardview_pago_detalle, parent, false)
        return PagoServiciosViewHolder(view)
    }

    override fun onBindViewHolder(holder: PagoServiciosViewHolder, position: Int) {
        val servicio = serviciosList[position]

        holder.tvServicio.text = servicio.servicio
        holder.tvCosto.text = servicio.costo.toString()
        holder.tvEstado.text = servicio.estado

        holder.checkBoxPago.isChecked = selectedItems.contains(position)
        holder.checkBoxPago.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedItems.add(position)
            } else {
                selectedItems.remove(position)
            }
            listener.onItemSelectionChanged(selectedItems)
        }
    }

    override fun getItemCount(): Int {
        return serviciosList.size
    }

    fun getSelectedItems(): List<ServiciosResponse.Data> {
        return selectedItems.map { serviciosList[it] }
    }

    fun selectAllItems() {
        selectedItems.clear()
        for (i in serviciosList.indices) {
            selectedItems.add(i)
        }
        notifyDataSetChanged()
    }

    fun clearSelection() {
        selectedItems.clear()
        notifyDataSetChanged()
    }

    inner class PagoServiciosViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvServicio: TextView = view.findViewById(R.id.tvServicio)
        val tvCosto: TextView = view.findViewById(R.id.tvCosto)
        val tvEstado: TextView = view.findViewById(R.id.tvEstado)
        val checkBoxPago: CheckBox = view.findViewById(R.id.checkBoxPago)
    }

    interface PagoServiciosAdapterListener {
        fun onItemSelectionChanged(selectedItems: Set<Int>)
    }
}
