package com.example.apppaciente.model

 class LoginResponse(
    val data: UserData,
    val message: String,
    val status: Boolean,
)

  class UserData(
    val ape_completo: String,
    val contrasena: String,
    val direccion: String,
    val documento: String,
    val email: String,
    val estado: Int,
    val estado_token: Int,
    val fecha_nac: String,
    val foto: String,
    val id: Int,
    val nombre: String,
    val nombre_usuario: String,
    val rol_id: Int,
    val sexo: Int,
    val telefono: String,
    val tipo_documento_id: Int,
    val token: String,
    val notificacion: Int
)

