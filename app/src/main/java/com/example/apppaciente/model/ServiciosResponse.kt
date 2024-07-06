package com.example.apppaciente.model

class ServiciosResponse {
    var status = false
    var message: String? = null
    var data: List<Data>? = null

    override fun toString(): String {
        return "ServiciosResponse(status=$status, message=$message, data=$data)"
    }

    class Data {
        var atencion_tratamiento_id: Int? = null
        var cita_id: Int = 0
        var costo: Double = 0.0
        var detalle_pago_id: Int? = null
        var estado: String? = null
        var pago_id: Int? = null
        var servicio: String? = null

        override fun toString(): String {
            return "Data(atencion_tratamiento_id=$atencion_tratamiento_id, cita_id=$cita_id, costo=$costo, detalle_pago_id=$detalle_pago_id, estado=$estado, pago_id=$pago_id, servicio=$servicio)"
        }
    }
}
