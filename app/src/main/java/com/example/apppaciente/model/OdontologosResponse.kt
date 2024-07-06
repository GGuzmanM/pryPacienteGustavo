package com.example.apppaciente.model

data class OdontologosResponse(
    val status: Boolean,
    val message: String?,
    val data: List<Data>?
) {
    data class Data(
        val id: Int,
        val nombre_completo: String
    )
}
