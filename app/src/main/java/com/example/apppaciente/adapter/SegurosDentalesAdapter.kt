package com.example.apppaciente.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.apppaciente.R
import com.example.apppaciente.databinding.CardviewSeguroDentalBinding
import com.example.apppaciente.model.SegurosDentalesResponse


class SegurosDentalesAdapter(
    private val context: Context,
    private val segurosDentalesList: List<SegurosDentalesResponse.Data>,
    private val listener: SegurosDentalesAdapterListener
): RecyclerView.Adapter<SegurosDentalesAdapter.SeguroDentalViewHolder>() {

    interface  SegurosDentalesAdapterListener {
        fun onVerSeguroDental(seguro: SegurosDentalesResponse.Data)
        fun onEliminarSeguroDental(seguro: SegurosDentalesResponse.Data)
    }

    inner class SeguroDentalViewHolder(val binding: CardviewSeguroDentalBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeguroDentalViewHolder {
        val binding = CardviewSeguroDentalBinding.inflate(LayoutInflater.from(context), parent, false)
        return SeguroDentalViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SeguroDentalViewHolder, position: Int) {
        val seguro_dental = segurosDentalesList[position]
        holder.binding.tvCompaniaSeguroValue.text = seguro_dental.nombre_compania
        holder.binding.tvCoberturaSeguroValue.text = seguro_dental.tipo_cobertura
        holder.binding.tvTelefonoSeguroValue.text = seguro_dental.telefono_compania


        holder.binding.root.setOnClickListener {
            listener.onVerSeguroDental(seguro_dental)
        }

        holder.binding.btnOpciones.setOnClickListener {
            showPopupMenu(holder.binding.btnOpciones, seguro_dental)
        }
    }

    private fun showPopupMenu(view: View, seguro_dental: SegurosDentalesResponse.Data){
        val popup = PopupMenu(context, view)
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.menu_seguro_dental_opciones, popup.menu)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId){
                R.id.action_eliminar ->{
                    listener.onEliminarSeguroDental(seguro_dental)
                    true
                }
                else -> false
            }
        }
        popup.show()
    }

    override fun getItemCount(): Int = segurosDentalesList.size
}