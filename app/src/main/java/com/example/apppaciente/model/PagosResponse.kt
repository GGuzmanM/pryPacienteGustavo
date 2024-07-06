package com.example.apppaciente.model

class PagosResponse {
    var status = false
    var message: String? = null
    var data: List<Data>? = null


    class Data {
        var cantidad_tratamiento = 0
        var cita_id = 0
        var costo: String? = null
        var estado: String? = null
        var fecha: String? = null
        var hora: String? = null
        var motivo_consulta: String? = null
        var nombre_odontologo: String? = null
    }
}