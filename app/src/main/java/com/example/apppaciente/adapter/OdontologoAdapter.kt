package com.example.apppaciente.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.apppaciente.R
import com.example.apppaciente.model.Odontologo

class OdontologoAdapter(context: Context, odontologos: List<Odontologo>) :
    ArrayAdapter<Odontologo>(context, 0, odontologos) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val odontologo = getItem(position)
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_odontologo, parent, false)

        val nombreCompleto = view.findViewById<TextView>(R.id.nombreCompleto)
        nombreCompleto.text = odontologo?.nombre_completo

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getView(position, convertView, parent)
    }
}
