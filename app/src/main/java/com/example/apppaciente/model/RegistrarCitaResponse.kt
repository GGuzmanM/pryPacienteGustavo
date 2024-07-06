package com.example.apppaciente.model

data class RegistrarCitaResponse(
    val status: Boolean,
    val message: String,
    val data: Data?
) {
    data class Data(
        val atencion_id: Int
    )
}
