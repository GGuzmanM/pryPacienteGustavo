package com.example.apppaciente.model

class RegistrarSeguroDentalResponse(
    val status: Boolean,
    val message: String,
    val data: Data?

) {

    data class Data(
        val seguro_id: Int
    )
}