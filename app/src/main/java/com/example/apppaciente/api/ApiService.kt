package com.example.apppaciente.api

import com.example.apppaciente.model.AgregarPacienteResponse
import com.example.apppaciente.model.CancelarCitaResponse
import com.example.apppaciente.model.CitasResponse
import com.example.apppaciente.model.EliminarSeguroDentalResponse
import com.example.apppaciente.model.LeerNotificacionResponse
import com.example.apppaciente.model.LoginResponse
import com.example.apppaciente.model.NotificacionResponse
import com.example.apppaciente.model.OdontologosResponse
import com.example.apppaciente.model.PagosPendientesResponse
import com.example.apppaciente.model.PagosResponse
import com.example.apppaciente.model.RegistrarCitaResponse
import com.example.apppaciente.model.RegistrarSeguroDentalResponse
import com.example.apppaciente.model.ReprogramarCitaResponse
import com.example.apppaciente.model.SegurosDentalesResponse
import com.example.apppaciente.model.ServiciosResponse
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService {
    @FormUrlEncoded
    @POST("/login")
    fun login(
        @Field("email") email: String?,
        @Field("clave") clave: String?
    ): Call<LoginResponse>


    @GET("/atencion/citas-paciente/{pacienteId}")
    fun getCitasProgramadas(@Path("pacienteId") pacienteId: Int): Call<CitasResponse>

    @GET("/atencion/odontologos")
    fun getOdontologos(): Call<OdontologosResponse>


    @FormUrlEncoded
    @POST("/atencion/cita/registrar")
    fun registrarCita(
        @Field("paciente_id") pacienteId: Int,
        @Field("odontologo_id") odontologoId: Int,
        @Field("fecha") fecha: String,
        @Field("hora") hora: String,
        @Field("motivo_consulta") motivoConsulta: String
    ): Call<RegistrarCitaResponse>


    @FormUrlEncoded
    @PUT("/atencion/cita/cancelar")
    fun cancelarCita(
        @Field("cita_id") citaID: Int
    ): Call<CancelarCitaResponse>
    @FormUrlEncoded
    @POST("/notificacion/actualizar_estado")
    fun leerNotificacion(
        @Field("notificacion_id") notificacion_id: Int,
        @Field("estado") estado: Int
    ): Call<LeerNotificacionResponse>

    @FormUrlEncoded
    @POST("/usuario/actualizar/notificacion/frank")
    fun estado_notificacion(
        @Field("usuario_id") usuario_id: Int?,
        @Field("notificacion") notificacion: Int
    ): Call<LoginResponse>


    @FormUrlEncoded
    @PUT("/atencion/cita/reprogramar")
    fun reprogramarCita(
        @Field("cita_id") citaID: Int,
        @Field("fecha") fecha: String,
        @Field("hora") hora: String
    ): Call<ReprogramarCitaResponse>

    @FormUrlEncoded
    @POST("usuario/agregar/paciente")
    fun agregarPaciente(
        @Field("nombreUsuario") nombreUsuario: String,
        @Field("contrasena") contrasena: String,
        @Field("email") email: String,
        @Field("estado") estado: Int,
        @Field("nombre") nombre: String,
        @Field("apeCompleto") apeCompleto: String,
        @Field("fechaNac") fechaNac: String,
        @Field("documento") documento: String,
        @Field("tipo_documento_id") tipoDocumentoId: Int,
        @Field("sexo") sexo: Int,
        @Field("direccion") direccion: String,
        @Field("telefono") telefono: String
    ): Call<AgregarPacienteResponse>

    @GET("/pago/pendientes/{pacienteId}")
    fun getPagosPendientes(@Path("pacienteId") pacienteId: Int): Call<PagosResponse>

    @GET("notificacion/paciente/{paciente_id}")
    fun getNotificacionesPaciente(@Path("paciente_id") paciente_id: Int): Call<NotificacionResponse>



    @GET("/pago/detalle/{citaId}")
    fun getServiciosPendientes(@Path("citaId") citaId: Int): Call<ServiciosResponse>


    @FormUrlEncoded
    @POST("/pago/detalle/registrar")
    fun registrarDetallePago(
        @Field("data") data: String
    ): Call<ServiciosResponse>


    @GET("/seguro-dental/{pacienteId}")
    fun getSegurosDentales(@Path("pacienteId") pacienteId: Int): Call<SegurosDentalesResponse>


    @FormUrlEncoded
    @POST("/seguro-dental/agregar")
    fun registrarSeguroDental(
        @Field("paciente_id") pacienteId: Int,
        @Field("nombre_compania") nombreCompania: String,
        @Field("telefono_compania") telefonoCompania: String,
        @Field("tipo_cobertura") tipoCobertura: String
    ): Call<RegistrarSeguroDentalResponse>


    @DELETE("/seguro-dental/eliminar")
    fun eliminarSeguroDental(
        @Query("id") seguroDentalId: Int
    ): Call<EliminarSeguroDentalResponse>

    @GET("/pago/pendientes/{pacienteId}")
    fun getPendientes(@Path("pacienteId") pacienteId: Int): Call<PagosPendientesResponse>
}
