package com.example.apppaciente.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apppaciente.R
import com.example.apppaciente.adapter.PagoServiciosAdapter
import com.example.apppaciente.api.ApiClient
import com.example.apppaciente.databinding.FragmentPagoServiciosBinding
import com.example.apppaciente.model.ServiciosResponse
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PagoServiciosFragment : Fragment(), PagoServiciosAdapter.PagoServiciosAdapterListener {

    private var _binding: FragmentPagoServiciosBinding? = null
    private val binding get() = _binding!!

    private lateinit var pagoServiciosAdapter: PagoServiciosAdapter
    private var serviciosList: List<ServiciosResponse.Data> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPagoServiciosBinding.inflate(inflater, container, false)

        binding.checkBoxSelectAll.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                pagoServiciosAdapter.selectAllItems()
            } else {
                pagoServiciosAdapter.clearSelection()
            }
        }

        binding.btnPagarServicios.setOnClickListener {
            procesarPagos()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = PagoServiciosFragmentArgs.fromBundle(requireArguments())
        val citaId = args.citaId

        obtenerServiciosPendientes(citaId)
    }

    private fun obtenerServiciosPendientes(citaId: Int) {
        val apiService = ApiClient.apiService
        val call = apiService.getServiciosPendientes(citaId)
        call.enqueue(object : Callback<ServiciosResponse> {
            override fun onResponse(call: Call<ServiciosResponse>, response: Response<ServiciosResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val serviciosResponse = response.body()!!
                    if (serviciosResponse.status) {
                        serviciosList = serviciosResponse.data ?: emptyList()
                        pagoServiciosAdapter = PagoServiciosAdapter(requireContext(), serviciosList, this@PagoServiciosFragment)
                        binding.recyclerViewServicios.apply {
                            layoutManager = LinearLayoutManager(context)
                            adapter = pagoServiciosAdapter
                        }
                    } else {
                        Toast.makeText(context, "Segundo Error: ${serviciosResponse.message}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Primer Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ServiciosResponse>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun procesarPagos() {
        val selectedItems = pagoServiciosAdapter.getSelectedItems()
        if (selectedItems.isEmpty()) {
            Toast.makeText(context, "No hay servicios seleccionados para pagar", Toast.LENGTH_SHORT).show()
            return
        }

        val detalle_pago: MutableList<Map<String, String>> = mutableListOf()

        for (item in selectedItems) {
            val detalle = mapOf(
                "servicio" to (item.servicio ?: ""),
                "estado" to (item.estado ?: ""),
                "pago_id" to (item.pago_id?.toString() ?: ""),
                "detalle_pago_id" to (item.detalle_pago_id?.toString() ?: ""),
                "costo" to item.costo.toString()
            )
            detalle_pago.add(detalle)
        }

        Toast.makeText(context, "Pagos procesados: ${selectedItems.size}", Toast.LENGTH_SHORT).show()

        val gson = Gson()
        val detallePagoJson = gson.toJson(detalle_pago)

        Log.d("Pago", "JSON enviado: $detallePagoJson")

        val apiService = ApiClient.apiService
        val call = apiService.registrarDetallePago(detallePagoJson)

        call.enqueue(object : Callback<ServiciosResponse> {
            override fun onResponse(call: Call<ServiciosResponse>, response: Response<ServiciosResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val serviciosResponse = response.body()!!
                    if (serviciosResponse.status) {
                        Toast.makeText(context, serviciosResponse.message, Toast.LENGTH_SHORT).show()
                        // Navegar a PagosPendientesFragment después de un pago exitoso
                        findNavController().navigate(R.id.action_pagoServiciosFragment_to_pagoPendientesFragment)
                    } else {
                        Toast.makeText(context, "Error en la respuesta: ${serviciosResponse.message}", Toast.LENGTH_SHORT).show()
                        Log.d("Pago", "Error en la respuesta: ${serviciosResponse.message}")
                    }
                } else {
                    Toast.makeText(context, "Error en la solicitud: ${response.message()}", Toast.LENGTH_SHORT).show()
                    Log.d("Pago", "Error en la solicitud: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ServiciosResponse>, t: Throwable) {
                Toast.makeText(context, "Error en la solicitud: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.d("Pago", "Error en la solicitud: ${t.message}")
            }
        })
    }

    override fun onItemSelectionChanged(selectedItems: Set<Int>) {
        // Implementar la lógica de actualización cuando cambie la selección de ítems
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
