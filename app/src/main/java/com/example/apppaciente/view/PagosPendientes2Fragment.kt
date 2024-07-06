package com.example.apppaciente.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apppaciente.adapter.PendientesAdapter
import com.example.apppaciente.api.ApiClient
import com.example.apppaciente.databinding.FragmentPagosPendientes2Binding
import com.example.apppaciente.model.PagosPendientesResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PagosPendientes2Fragment :Fragment(), PendientesAdapter.PendientesAdapterListener{
    private var _binding: FragmentPagosPendientes2Binding? = null
    private val binding get() = _binding!!

    private lateinit var pendientesAdapter: PendientesAdapter
    private var pendientesList: List<PagosPendientesResponse.Data> = emptyList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPagosPendientes2Binding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val pacienteId = sharedPreferences.getInt("paciente_id", -1)

        obtenerPendientes(pacienteId)
    }

    private fun obtenerPendientes(pacienteId: Int) {
        val apiService = ApiClient.apiService
        val call = apiService.getPendientes(pacienteId)
        call.enqueue(object : Callback<PagosPendientesResponse> {
            override fun onResponse(call: Call<PagosPendientesResponse>, response: Response<PagosPendientesResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val pendientesResponse = response.body()!!
                    if (pendientesResponse.status) {
                        pendientesList = pendientesResponse.data ?: emptyList()
                        pendientesAdapter = PendientesAdapter(requireContext(), pendientesList, this@PagosPendientes2Fragment)
                        binding.recyclerViewPagos.apply {
                            layoutManager = LinearLayoutManager(context)
                            adapter = pendientesAdapter
                        }
                    }
                    else {
                        Toast.makeText(context, "Error: ${pendientesResponse.message}", Toast.LENGTH_SHORT).show()
                    }
                }else {
                    Toast.makeText(context, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<PagosPendientesResponse>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onItemSelectionChanged(selectedItems: Set<Int>) {
    }

    override fun onButtonClicked(cita: PagosPendientesResponse.Data){
        val action = PagosPendientesFragmentDirections.actionPagoPendientesFragmentToPagoServiciosFragment(
            citaId = cita.cita_id
        )

    }
}