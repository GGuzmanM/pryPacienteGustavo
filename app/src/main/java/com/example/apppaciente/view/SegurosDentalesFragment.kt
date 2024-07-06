package com.example.apppaciente.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apppaciente.R
import com.example.apppaciente.adapter.SegurosDentalesAdapter
import com.example.apppaciente.api.ApiClient
import com.example.apppaciente.databinding.FragmentSegurosDentalesBinding
import com.example.apppaciente.model.CitasResponse
import com.example.apppaciente.model.EliminarSeguroDentalResponse
import com.example.apppaciente.model.SegurosDentalesResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SegurosDentalesFragment : Fragment(), SegurosDentalesAdapter.SegurosDentalesAdapterListener {

    private var _binding: FragmentSegurosDentalesBinding? = null
    private val binding get() = _binding!!

    private lateinit var segurosDentalesAdapter: SegurosDentalesAdapter

    private var segurosDentalesList: List<SegurosDentalesResponse.Data> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSegurosDentalesBinding.inflate(inflater, container, false)

        binding.btnAgregarSeguroDental.setOnClickListener {
            findNavController().navigate(R.id.action_segurosDentalesFragment_to_agregarSeguroDentalFragment)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val pacienteId = sharedPreferences.getInt("paciente_id", -1)

        obtenerSegurosDentalesProgramadas(pacienteId)
    }

    private fun obtenerSegurosDentalesProgramadas(pacienteId: Int) {
        val apiService = ApiClient.apiService
        val call = apiService.getSegurosDentales(pacienteId)
        call.enqueue(object : Callback<SegurosDentalesResponse> {
            override fun onResponse(
                call: Call<SegurosDentalesResponse>,
                response: Response<SegurosDentalesResponse>
            ) {
                if (response.isSuccessful && response.body() != null){
                    val segurosDentalesResponse = response.body()!!
                    if (segurosDentalesResponse.status) {
                        segurosDentalesList = segurosDentalesResponse.data ?: emptyList()
                        segurosDentalesAdapter = SegurosDentalesAdapter(requireContext(), segurosDentalesList, this@SegurosDentalesFragment)

                        binding.recyclerViewSeguroDental.apply {
                            layoutManager = LinearLayoutManager(context)
                            adapter = segurosDentalesAdapter
                        }
                    } else {
                        // sin registros agregar validación
                        Toast.makeText(context, "${segurosDentalesResponse.message}", Toast.LENGTH_SHORT).show()
                    }
                }else {
                    Toast.makeText(context, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<SegurosDentalesResponse>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    override fun onVerSeguroDental(seguro: SegurosDentalesResponse.Data) {
        // action = SegurosDentalesProgramadasFra
    }

    override fun onEliminarSeguroDental(seguro: SegurosDentalesResponse.Data) {
        val apiService = ApiClient.apiService
        val eliminarSeguroDentalCall = apiService.eliminarSeguroDental(seguro.seguro_dental_id)

        eliminarSeguroDentalCall.enqueue(object : Callback<EliminarSeguroDentalResponse>{
            override fun onResponse(
                call: Call<EliminarSeguroDentalResponse>,
                response: Response<EliminarSeguroDentalResponse>
            ) {
                Log.d("Eliminar", "Mensaje 1: ${seguro.seguro_dental_id}")
                Log.d("Eliminar", "Mensaje 2: ${response.isSuccessful}")
                Log.d("Eliminar", "Mensaje 3: ${response.body()}")
                if (response.isSuccessful && response.body() != null){
                    val eliminarSeguroDentalResponse = response.body()!!
                    if (eliminarSeguroDentalResponse.status){
                        Toast.makeText(context, "Seguro dental eliminado exitosamente", Toast.LENGTH_SHORT).show()
                        val sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                        val pacienteId = sharedPreferences.getInt("paciente_id", -1)
                        obtenerSegurosDentalesProgramadas(pacienteId)
                    } else {
                        Toast.makeText(context, "Error segundo: ${eliminarSeguroDentalResponse.message}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Error primero: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<EliminarSeguroDentalResponse>, t: Throwable) {
                Toast.makeText(context, "Error ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}