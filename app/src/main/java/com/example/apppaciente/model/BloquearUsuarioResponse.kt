package com.example.apppaciente.model

class BloquearUsuarioResponse (
    val status: Boolean,
    val message: String,
    val data: Data?
) {
    data class Data(
        val email: String
    )
}