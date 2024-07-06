package com.example.apppaciente.model

class NotificacionResponse {
    var status = false
    var message: String? = null
    var data: List<Data>? = null


    class Data {
        val id: Int = 0
        val usuario_id: Int? = null
        val mensaje: String? = null
        val fecha: String? = null
        val leida: Int? = null


    }
}