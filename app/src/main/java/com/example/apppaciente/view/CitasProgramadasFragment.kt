package com.example.apppaciente.view

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
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
import com.example.apppaciente.adapter.CitasAdapter
import com.example.apppaciente.api.ApiClient
import com.example.apppaciente.databinding.FragmentCitasProgramadasBinding
import com.example.apppaciente.model.CancelarCitaResponse
import com.example.apppaciente.model.CitasResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import android.widget.TextView
import android.widget.Button
import com.example.apppaciente.model.ReprogramarCitaResponse

class CitasProgramadasFragment : Fragment(), CitasAdapter.CitasAdapterListener {

    private var _binding: FragmentCitasProgramadasBinding? = null
    private val binding get() = _binding!!

    private lateinit var citasAdapter: CitasAdapter
    private var citasList: List<CitasResponse.Data> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCitasProgramadasBinding.inflate(inflater, container, false)

        binding.btnAgregarCita.setOnClickListener {
            findNavController().navigate(R.id.action_citasProgramadasFragment_to_agregarCitaFragment)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val pacienteId = sharedPreferences.getInt("paciente_id", -1)

        obtenerCitasProgramadas(pacienteId)
    }

    private fun obtenerCitasProgramadas(pacienteId: Int) {
        val apiService = ApiClient.apiService
        val call = apiService.getCitasProgramadas(pacienteId)
        call.enqueue(object : Callback<CitasResponse> {
            override fun onResponse(call: Call<CitasResponse>, response: Response<CitasResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val citasResponse = response.body()!!
                    if (citasResponse.status) {
                        citasList = citasResponse.data ?: emptyList()
                        citasAdapter = CitasAdapter(requireContext(), citasList, this@CitasProgramadasFragment)
                        binding.recyclerViewCitas.apply {
                            layoutManager = LinearLayoutManager(context)
                            adapter = citasAdapter
                        }
                    } else {
                        Log.d("MiApp", "Estado: ${citasResponse.status}, Mensaje: ${citasResponse.message}, Datos: ${citasResponse.data}")
                        Toast.makeText(context, "Error: ${citasResponse.message}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CitasResponse>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onVerDetalle(cita: CitasResponse.Data) {
        val action = CitasProgramadasFragmentDirections.actionCitasProgramadasFragmentToDetalleCitaFragment(
            estado = cita.estado ?: "",
            fecha = cita.fecha ?: "",
            hora = cita.hora ?: "",
            motivoConsulta = cita.motivo_consulta ?: "",
            nombreOdontologo = cita.nombre_odontologo ?: "",
            diagnostico = cita.diagnostico ?: "No registrado",
            anotacion = cita.anotacion ?: "No registrado",
            costo = cita.costo ?: "No disponible"
        )
        findNavController().navigate(action)
    }

    override fun onReprogramarCita(cita: CitasResponse.Data) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_reprogramar_cita, null)

        val tvFechaRegistrada: TextView = dialogView.findViewById(R.id.tvFechaRegistrada)
        val tvHoraRegistrada: TextView = dialogView.findViewById(R.id.tvHoraRegistrada)
        val etNuevaFecha: TextView = dialogView.findViewById(R.id.etNuevaFecha)
        val etNuevaHora: TextView = dialogView.findViewById(R.id.etNuevaHora)
        val btnReprogramar: Button = dialogView.findViewById(R.id.btnReprogramar)

        tvFechaRegistrada.text = cita.fecha
        tvHoraRegistrada.text = cita.hora

        etNuevaFecha.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                etNuevaFecha.text = dateFormat.format(calendar.time)
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
            datePickerDialog.show()
        }

        etNuevaHora.setOnClickListener {
            val calendar = Calendar.getInstance()
            val timePickerDialog = TimePickerDialog(requireContext(), { _, hourOfDay, minute ->
                etNuevaHora.text = String.format("%02d:%02d", hourOfDay, minute)
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true)
            timePickerDialog.show()
        }

        val alertDialog = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
            .setView(dialogView)
            .setNegativeButton("Cancelar", null)
            .create()

        btnReprogramar.setOnClickListener {
            val nuevaFecha = etNuevaFecha.text.toString()
            val nuevaHora = etNuevaHora.text.toString()

            if (nuevaFecha.isEmpty() || nuevaHora.isEmpty()) {
                Toast.makeText(requireContext(), "Debe ingresar la nueva fecha y hora", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Implementa la lógica para reprogramar la cita con la nueva fecha y hora
            reprogramarCita(cita.cita_id, nuevaFecha, nuevaHora)
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    private fun reprogramarCita(citaId: Int, nuevaFecha: String, nuevaHora: String) {
        val apiService = ApiClient.apiService
        val reprogramarCitaCall = apiService.reprogramarCita(citaId, nuevaFecha, nuevaHora)

        reprogramarCitaCall.enqueue(object : Callback<ReprogramarCitaResponse> {
            override fun onResponse(call: Call<ReprogramarCitaResponse>, response: Response<ReprogramarCitaResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val reprogramarCitaResponse = response.body()!!
                    if (reprogramarCitaResponse.status) {
                        Toast.makeText(context, "Cita reprogramada exitosamente", Toast.LENGTH_SHORT).show()
                        // Actualizar la lista de citas programadas
                        val sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                        val pacienteId = sharedPreferences.getInt("paciente_id", -1)
                        obtenerCitasProgramadas(pacienteId)
                    } else {
                        Toast.makeText(context, "Error: ${reprogramarCitaResponse.message}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ReprogramarCitaResponse>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    override fun onCancelarCita(cita: CitasResponse.Data) {
        val apiService = ApiClient.apiService
        val cancelarCitaCall = apiService.cancelarCita(cita.cita_id)

        cancelarCitaCall.enqueue(object : Callback<CancelarCitaResponse> {
            override fun onResponse(call: Call<CancelarCitaResponse>, response: Response<CancelarCitaResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val cancelarCitaResponse = response.body()!!
                    if (cancelarCitaResponse.status) {
                        Toast.makeText(context, "Cita cancelada exitosamente", Toast.LENGTH_SHORT).show()
                        // Llamar a la función para actualizar la lista de citas programadas
                        val sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                        val pacienteId = sharedPreferences.getInt("paciente_id", -1)
                        obtenerCitasProgramadas(pacienteId)
                    } else {
                        Toast.makeText(context, "Error: ${cancelarCitaResponse.message}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CancelarCitaResponse>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

