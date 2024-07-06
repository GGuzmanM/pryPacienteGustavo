package com.example.apppaciente.model

class CancelarCitaResponse (
    val status: Boolean,
    val message: String,
    val data: Data?
) {
    data class Data(
        val cita_id: Int
    )
}