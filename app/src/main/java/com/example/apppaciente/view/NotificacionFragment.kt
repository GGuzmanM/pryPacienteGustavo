package com.example.apppaciente.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apppaciente.adapter.NotificacionAdapter
import com.example.apppaciente.api.ApiClient
import com.example.apppaciente.databinding.FragmentNotificacionBinding
import com.example.apppaciente.model.LeerNotificacionResponse
import com.example.apppaciente.model.LoginResponse
import com.example.apppaciente.model.NotificacionResponse


import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificacionFragment : Fragment(), NotificacionAdapter.NotificacionAdapterListener {

    private var _binding: FragmentNotificacionBinding? = null
    private val binding get() = _binding!!

    private lateinit var notificacionAdapter: NotificacionAdapter
    private var notificacionList: List<NotificacionResponse.Data> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificacionBinding.inflate(inflater, container, false)
        return binding.root
    }
    private fun cargarNotificaciones(paciente_id: Int) {
        val apiService = ApiClient.apiService
        val call = apiService.getNotificacionesPaciente(paciente_id)

        call.enqueue(object : Callback<NotificacionResponse> {
            override fun onResponse(call: Call<NotificacionResponse>, response: Response<NotificacionResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val notificaciones = response.body()!!
                    if (notificaciones.status) {
                        notificacionList = notificaciones.data ?: emptyList()
                        notificacionAdapter = NotificacionAdapter(requireContext(), notificacionList, this@NotificacionFragment)
                        binding.recyclerViewNotificacion.apply {
                            layoutManager = LinearLayoutManager(context)
                            adapter = notificacionAdapter
                        }
                    } else {
                        Toast.makeText(context, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("TratamientosFragment", "Error al obtener tratamientos: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<NotificacionResponse>, t: Throwable) {
                Log.e("TratamientosFragment", "Error en la solicitud: ${t.message}", t)
            }
        })
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val pacienteId = sharedPreferences.getInt("paciente_id", -1)

        cargarNotificaciones(pacienteId)
    }

    override fun leerNootificacion(noti: NotificacionResponse.Data) {
        val apiService = ApiClient.apiService
        if (noti.leida == 0){
            val cancelarCitaCall = apiService.leerNotificacion(noti.id, 1)
            cancelarCitaCall.enqueue(object : Callback<LeerNotificacionResponse> {
                override fun onResponse(call: Call<LeerNotificacionResponse>, response: Response<LeerNotificacionResponse>) {
                    if (response.isSuccessful && response.body() != null) {
                        val leerCitaResponse = response.body()!!
                        if (leerCitaResponse.status) {
                            Toast.makeText(context, "Notificacion marcada como leida", Toast.LENGTH_SHORT).show()
                            // Llamar a la función para actualizar la lista de citas programadas
                            val sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                            val pacienteId = sharedPreferences.getInt("paciente_id", -1)
                            cargarNotificaciones(pacienteId)
                        } else {
                            Toast.makeText(context, "Error: ${leerCitaResponse.message}", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LeerNotificacionResponse>, t: Throwable) {
                    Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }else{
            val cancelarCitaCall = apiService.leerNotificacion(noti.id, 0)
            cancelarCitaCall.enqueue(object : Callback<LeerNotificacionResponse> {
                override fun onResponse(call: Call<LeerNotificacionResponse>, response: Response<LeerNotificacionResponse>) {
                    if (response.isSuccessful && response.body() != null) {
                        val leerCitaResponse = response.body()!!
                        if (leerCitaResponse.status) {
                            Toast.makeText(context, "Notificacion marcada como no leida", Toast.LENGTH_SHORT).show()
                            // Llamar a la función para actualizar la lista de citas programadas
                            val sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                            val pacienteId = sharedPreferences.getInt("paciente_id", -1)
                            cargarNotificaciones(pacienteId)
                        } else {
                            Toast.makeText(context, "Error: ${leerCitaResponse.message}", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LeerNotificacionResponse>, t: Throwable) {
                    Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })

        }


    }

    override fun cambiar_estado_notificacion(noti: NotificacionResponse.Data) {
        val apiService = ApiClient.apiService
        if (noti.leida == 0){
            val cancelarCitaCall = apiService.estado_notificacion(noti.usuario_id, 1)
            cancelarCitaCall.enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful && response.body() != null) {
                        val leerCitaResponse = response.body()!!
                        if (leerCitaResponse.status) {
                            Toast.makeText(context, "Notificacion marcada como leida", Toast.LENGTH_SHORT).show()
                            // Llamar a la función para actualizar la lista de citas programadas
                            val sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                            val pacienteId = sharedPreferences.getInt("paciente_id", -1)
                            cargarNotificaciones(pacienteId)
                        } else {
                            Toast.makeText(context, "Error: ${leerCitaResponse.message}", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }else{
            val cancelarCitaCall = apiService.estado_notificacion(noti.usuario_id, 0)
            cancelarCitaCall.enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful && response.body() != null) {
                        val leerCitaResponse = response.body()!!
                        if (leerCitaResponse.status) {
                            Toast.makeText(context, "Notificacion marcada como leida", Toast.LENGTH_SHORT).show()
                            // Llamar a la función para actualizar la lista de citas programadas
                            val sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                            val pacienteId = sharedPreferences.getInt("paciente_id", -1)
                            cargarNotificaciones(pacienteId)
                        } else {
                            Toast.makeText(context, "Error: ${leerCitaResponse.message}", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })

        }


    }





    override fun onItemSelectionChanged(selectedItems: Set<Int>) {
        // Implementar la lógica de actualización cuando cambie la selección de ítems
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onCardViewClicked(noti: NotificacionResponse.Data) {

    }

}