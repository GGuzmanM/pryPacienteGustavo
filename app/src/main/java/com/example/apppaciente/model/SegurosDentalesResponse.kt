package com.example.apppaciente.model

class SegurosDentalesResponse {
    var status = false
    var message: String? = null
    var data: List<Data>? = null

    class Data{
        var seguro_dental_id = 0
        var nombre_compania: String? = null
        var telefono_compania: String? = null
        var tipo_cobertura: String? = null

    }
}