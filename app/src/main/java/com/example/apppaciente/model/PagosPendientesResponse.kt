package com.example.apppaciente.model

class PagosPendientesResponse {
    var status = false
    var message: String? = null
    var data: List<PagosPendientesResponse.Data>? = null
    class Data {
        var cita_id = 0
        var costo: String? = null
        var estado: String? = null
        var fecha: String? = null
        var hora: String? = null
        var motivo_consulta: String? = null
        var nombre_odontologo: String? = null
    }
}