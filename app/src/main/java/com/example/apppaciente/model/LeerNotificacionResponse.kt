package com.example.apppaciente.model

class LeerNotificacionResponse (
    val status: Boolean,
    val message: String,
    val data: Data?
) {
    data class Data(
        val notificacion_id: Int
    )
}