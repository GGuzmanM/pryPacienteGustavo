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
import com.example.apppaciente.adapter.PagosAdapter
import com.example.apppaciente.api.ApiClient
import com.example.apppaciente.databinding.FragmentPagosPendientesBinding
import com.example.apppaciente.model.PagosResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PagosPendientesFragment : Fragment(), PagosAdapter.PagosAdapterListener {

    private var _binding: FragmentPagosPendientesBinding? = null
    private val binding get() = _binding!!

    private lateinit var pagosAdapter: PagosAdapter
    private var citasList: List<PagosResponse.Data> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPagosPendientesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val pacienteId = sharedPreferences.getInt("paciente_id", -1)

        obtenerCitasPendientes(pacienteId)
    }

    private fun obtenerCitasPendientes(pacienteId: Int) {
        val apiService = ApiClient.apiService
        val call = apiService.getPagosPendientes(pacienteId)
        call.enqueue(object : Callback<PagosResponse> {
            override fun onResponse(call: Call<PagosResponse>, response: Response<PagosResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val pagosResponse = response.body()!!
                    if (pagosResponse.status) {
                        citasList = pagosResponse.data ?: emptyList()
                        pagosAdapter = PagosAdapter(requireContext(), citasList, this@PagosPendientesFragment)
                        binding.recyclerViewPagos.apply {
                            layoutManager = LinearLayoutManager(context)
                            adapter = pagosAdapter
                        }
                    } else {
                        Toast.makeText(context, "Error: ${pagosResponse.message}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PagosResponse>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemSelectionChanged(selectedItems: Set<Int>) {
        // Implementar la lógica de actualización cuando cambie la selección de ítems
    }

    override fun onCardViewClicked(cita: PagosResponse.Data) {
        val action = PagosPendientesFragmentDirections.actionPagoPendientesFragmentToPagoServiciosFragment(
            citaId = cita.cita_id
        )
        findNavController().navigate(action)
    }
}
