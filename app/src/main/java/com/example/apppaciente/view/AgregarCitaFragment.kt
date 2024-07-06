package com.example.apppaciente.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.apppaciente.R
import com.example.apppaciente.adapter.OdontologoAdapter
import com.example.apppaciente.api.ApiClient
import com.example.apppaciente.databinding.FragmentAgregarCitaBinding
import com.example.apppaciente.model.Odontologo
import com.example.apppaciente.model.OdontologosResponse
import com.example.apppaciente.model.RegistrarCitaResponse
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class AgregarCitaFragment : Fragment() {

    private var _binding: FragmentAgregarCitaBinding? = null
    private val binding get() = _binding!!
    private var odontologoList: List<Odontologo> = emptyList()
    private var selectedOdontologoId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAgregarCitaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val calendar = Calendar.getInstance()

        binding.inputFecha.setOnClickListener {
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                calendar.set(selectedYear, selectedMonth, selectedDay)
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                binding.inputFecha.text = dateFormat.format(calendar.time)
            }, year, month, day)
            datePickerDialog.show()
        }

        binding.inputHora.setOnClickListener {
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)
            val timePickerDialog = TimePickerDialog(requireContext(), { _, selectedHour, selectedMinute ->
                binding.inputHora.text = String.format("%02d:%02d", selectedHour, selectedMinute)
            }, hour, minute, true)
            timePickerDialog.show()
        }

        val apiService = ApiClient.apiService
        val call = apiService.getOdontologos()
        call.enqueue(object : Callback<OdontologosResponse> {
            override fun onResponse(
                call: Call<OdontologosResponse>,
                response: Response<OdontologosResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val odontologoResponse = response.body()!!
                    if (odontologoResponse.status) {
                        odontologoList = odontologoResponse.data?.map {
                            Odontologo(it.id, it.nombre_completo)
                        } ?: emptyList()

                        val adapter = OdontologoAdapter(requireContext(), odontologoList)
                        binding.inputOdontologo.setAdapter(adapter)
                        binding.inputOdontologo.setOnClickListener {
                            binding.inputOdontologo.showDropDown()
                        }

                        // Configura el listener para manejar la selección
                        binding.inputOdontologo.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
                            val selectedOdontologo = parent.getItemAtPosition(position) as Odontologo
                            selectedOdontologoId = selectedOdontologo.id
                        }
                    } else {
                        Toast.makeText(context, "Error: ${odontologoResponse.message}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<OdontologosResponse>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })

        binding.btnGuardarCita.setOnClickListener {
            val sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            val pacienteId = sharedPreferences.getInt("paciente_id", -1)

            val fecha = binding.inputFecha.text.toString()
            val hora = binding.inputHora.text.toString()
            val motivoConsulta = binding.inputMotivoConsulta.text.toString()
            val odontologoId = selectedOdontologoId

            Log.d("AgregarCitaFragment", "Enviando datos -> pacienteId: $pacienteId, odontologoId: $odontologoId, fecha: $fecha, hora: $hora, motivoConsulta: $motivoConsulta")

            if (odontologoId != null && pacienteId != -1) {
                val registrarCitaCall = apiService.registrarCita(
                    pacienteId,
                    odontologoId,
                    fecha,
                    hora,
                    motivoConsulta
                )
                registrarCitaCall.enqueue(object : Callback<RegistrarCitaResponse> {
                    override fun onResponse(
                        call: Call<RegistrarCitaResponse>,
                        response: Response<RegistrarCitaResponse>
                    ) {
                        Log.d("AgregarCitaFragment", "Response: ${response.isSuccessful}, Body: ${response.body()}, Message: ${response.message()}, ErrorBody: ${response.errorBody()?.string()}")
                        if (response.isSuccessful && response.body() != null) {
                            val registrarCitaResponse = response.body()!!
                            if (registrarCitaResponse.status) {
                                findNavController().navigate(R.id.action_agregarCitaFragment_to_citasProgramadasFragment2)
                                Toast.makeText(context, "Cita registrada exitosamente", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Error: ${registrarCitaResponse.message}", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(context, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<RegistrarCitaResponse>, t: Throwable) {
                        Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(context, "Por favor selecciona un odontólogo y asegúrate de que el ID del paciente es válido", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
