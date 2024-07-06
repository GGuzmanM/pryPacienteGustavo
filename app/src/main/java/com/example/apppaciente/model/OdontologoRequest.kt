package com.example.apppaciente.model

data class Odontologo(
    val id: Int,
    val nombre_completo: String
) {
    override fun toString(): String {
        return nombre_completo
    }
}
