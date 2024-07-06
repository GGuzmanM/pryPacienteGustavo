package com.example.apppaciente.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.apppaciente.R
import com.example.apppaciente.api.ApiClient
import com.example.apppaciente.databinding.FragmentAgregarSeguroDentalBinding
import com.example.apppaciente.model.RegistrarCitaResponse
import com.example.apppaciente.model.RegistrarSeguroDentalResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AgregarSeguroDentalFragment : Fragment() {

    private var _binding: FragmentAgregarSeguroDentalBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAgregarSeguroDentalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val apiService = ApiClient.apiService

        binding.btnGuardarSeguroDental.setOnClickListener {
            val sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            val pacienteId = sharedPreferences.getInt("paciente_id", -1)

            val nombreCompania = binding.inputNombreCompania.text.toString()
            val telefonoCompania = binding.inputTelefonoCompania.text.toString()
            val tipoCobertura = binding.inputTipoCobertura.text.toString()


            if (nombreCompania.isNotEmpty() && telefonoCompania.isNotEmpty() && tipoCobertura.isNotEmpty()){
                val registrarSeguroDentalCall = apiService.registrarSeguroDental(
                    pacienteId,
                    nombreCompania,
                    telefonoCompania,
                    tipoCobertura
                )
                registrarSeguroDentalCall.enqueue(object : Callback<RegistrarSeguroDentalResponse> {
                    override fun onResponse(
                        call: Call<RegistrarSeguroDentalResponse>,
                        response: Response<RegistrarSeguroDentalResponse>
                    ) {
                        if (response.isSuccessful && response.body() != null){
                            val registrarSeguroDentalResponse = response.body()!!
                            if (registrarSeguroDentalResponse.status) {
                                findNavController().navigate(R.id.action_agregarSeguroDentalFragment_to_segurosDentalesFragment)
                                Toast.makeText(context, "Seguro dental registrada exitosamente", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Error: ${registrarSeguroDentalResponse.message}", Toast.LENGTH_SHORT).show()
                            }
                        }else{
                            Toast.makeText(context, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(
                        call: Call<RegistrarSeguroDentalResponse>,
                        t: Throwable
                    ) {
                        Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(context, "Por favor ingrese todos los campos para seguro dental.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}