package com.example.apppaciente.model

data class AgregarPacienteResponse(
    val status: Boolean,
    val message: String,
    val data: Data?
) {
    data class Data(
        val pacienteId: Int
    )
}