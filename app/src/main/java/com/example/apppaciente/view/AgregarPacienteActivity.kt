package com.example.apppaciente.view

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.apppaciente.R
import com.example.apppaciente.api.ApiClient
import com.example.apppaciente.databinding.ActivityAgregarPacienteBinding
import com.example.apppaciente.model.AgregarPacienteResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class AgregarPacienteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAgregarPacienteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgregarPacienteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val calendar = Calendar.getInstance()

        binding.inputFecha.setOnClickListener {
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                calendar.set(selectedYear, selectedMonth, selectedDay)
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                binding.inputFecha.setText(dateFormat.format(calendar.time))
            }, year, month, day)
            datePickerDialog.show()
        }

        binding.btnGuardarPaciente.setOnClickListener {
            val nombreUsuario = binding.inputNombreUsuario.text.toString()
            val nombre = binding.inputNombreCliente.text.toString()
            val apeCompleto = binding.inputApellidoCliente.text.toString()
            val email = binding.inputCorreoCliente.text.toString()
            val contrasena = binding.inputContrasena.text.toString()
            val estado = 1
            val fechaNac = binding.inputFecha.text.toString()
            val documento = binding.inputDocumentoCliente.text.toString()
            val tipoDocumentoId = 1
            val sexo = when (binding.radioGroupSexo.checkedRadioButtonId) {
                R.id.radioMasculino -> 2
                R.id.radioFemenino -> 1
                else -> 0
            }
            val direccion = binding.inputDireccionCliente.text.toString()
            val telefono = binding.inputTelefonoCliente.text.toString()

            Log.d("AgregarPacienteActivity", "Enviando datos -> nombreUsuario: $nombreUsuario, email: $email, contrasena: $contrasena, estado: $estado, nombre: $nombre, apeCompleto: $apeCompleto, fechaNac: $fechaNac, documento: $documento, tipoDocumentoId: $tipoDocumentoId, sexo: $sexo, direccion: $direccion, telefono: $telefono")

            val apiService = ApiClient.apiService
            val agregarPacienteCall = apiService.agregarPaciente(
                nombreUsuario,
                contrasena,
                email,
                estado,
                nombre,
                apeCompleto,
                fechaNac,
                documento,
                tipoDocumentoId,
                sexo,
                direccion,
                telefono
            )

            agregarPacienteCall.enqueue(object : Callback<AgregarPacienteResponse> {
                override fun onResponse(call: Call<AgregarPacienteResponse>, response: Response<AgregarPacienteResponse>) {
                    Log.d("AgregarPacienteActivity", "Response: ${response.isSuccessful}, Body: ${response.body()}, Message: ${response.message()}, ErrorBody: ${response.errorBody()?.string()}")
                    if (response.isSuccessful && response.body() != null) {
                        val agregarPacienteResponse = response.body()!!
                        if (agregarPacienteResponse.status) {
                            val intent = Intent(this@AgregarPacienteActivity, LoginActivity::class.java)
                            startActivity(intent)
                            Toast.makeText(this@AgregarPacienteActivity, "Paciente agregado exitosamente", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@AgregarPacienteActivity, "Error: ${agregarPacienteResponse.message}", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@AgregarPacienteActivity, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<AgregarPacienteResponse>, t: Throwable) {
                    Toast.makeText(this@AgregarPacienteActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
