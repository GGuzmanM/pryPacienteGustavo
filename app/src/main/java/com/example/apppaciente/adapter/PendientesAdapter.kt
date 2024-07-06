package com.example.apppaciente.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.apppaciente.R
import com.example.apppaciente.model.PagosPendientesResponse
import com.example.apppaciente.model.PagosResponse

class PendientesAdapter (
    private val context: Context,
    private val pendienteList: List<PagosPendientesResponse.Data>,
    private val listener: PendientesAdapterListener
    ) : RecyclerView.Adapter<PendientesAdapter.PendientesViewHolder>() {
    private val selectedItems = mutableSetOf<Int>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendientesAdapter.PendientesViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.cardview_pagos_pendientes, parent, false)
        return PendientesViewHolder(view)
    }

    override fun onBindViewHolder(holder: PendientesAdapter.PendientesViewHolder, position: Int) {
        val pendiente = pendienteList[position]

        holder.fechaValue.text = pendiente.fecha
        holder.horaValue.text = pendiente.hora
        holder.odontologoValue.text = pendiente.nombre_odontologo
        holder.motivoConsulta.text = pendiente.motivo_consulta
        holder.costoTotal.text = pendiente.costo.toString()

        holder.botonPagar.setOnClickListener {
            listener.onButtonClicked(pendiente)
        }
    }
    override fun getItemCount(): Int {
        return pendienteList.size
    }
    fun getSelectedItems(): List<PagosPendientesResponse.Data> {
        return selectedItems.map { pendienteList[it] }
    }
    fun selectAllItems() {
        selectedItems.clear()
        for (i in pendienteList.indices) {
            selectedItems.add(i)
        }
        notifyDataSetChanged()
    }

    fun clearSelection() {
        selectedItems.clear()
        notifyDataSetChanged()
    }

    inner class PendientesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val fechaValue: TextView = view.findViewById(R.id.FechaPendiente)
        val horaValue: TextView = view.findViewById(R.id.HoraPendiente)
        val odontologoValue: TextView = view.findViewById(R.id.OdontologoPendiente)
        val motivoConsulta: TextView = view.findViewById(R.id.MotivoPendiente)
        val costoTotal: TextView = view.findViewById(R.id.CostoPendiente)
        val botonPagar: Button = view.findViewById(R.id.btnPagarPendiente)
    }

    interface PendientesAdapterListener {
        fun onItemSelectionChanged(selectedItems: Set<Int>)
        fun onButtonClicked(cita: PagosPendientesResponse.Data)
    }

    }
