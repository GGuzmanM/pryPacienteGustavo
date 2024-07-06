package com.example.apppaciente.model

class CitasResponse {
    var status = false
    var message: String? = null
    var data: List<Data>? = null

    override fun toString(): String {
        return "CitasResponse(isStatus=$status, message=$message, data=$data)"
    }

    class Data {
        var anotacion: String? = null
        var cita_id = 0
        var costo: String? = null
        var diagnostico: String? = null
        var estado: String? = null
        var fecha: String? = null
        var hora: String? = null
        var motivo_consulta: String? = null
        var nombre_odontologo: String? = null
        var nombre_paciente: String? = null

        override fun toString(): String {
            return "Data(anotacion=$anotacion, cita_id=$cita_id, costo=$costo, diagnostico=$diagnostico, estado=$estado, fecha=$fecha, hora=$hora, motivo_consulta=$motivo_consulta, nombre_odontologo=$nombre_odontologo, nombre_paciente=$nombre_paciente)"
        }
    }
}
