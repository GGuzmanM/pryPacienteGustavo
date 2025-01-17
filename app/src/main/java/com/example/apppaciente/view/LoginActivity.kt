package com.example.apppaciente.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.apppaciente.api.ApiClient
import com.example.apppaciente.api.ApiService
import com.example.apppaciente.databinding.ActivityLoginBinding
import com.example.apppaciente.model.BloquearUsuarioResponse
import com.example.apppaciente.model.LoginResponse
import com.example.apppaciente.viewmodel.LoginViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()
    private var intentos = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginViewModel.loginResponse.observe(this, Observer { response ->
            if (response != null && response.status) {
                val token: String = response.data.token
                ApiClient.API_TOKEN = token

                // Guardar el token y los datos del usuario en SharedPreferences
                val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putInt("paciente_id", response.data.id)
                editor.putString("username", response.data.nombre_usuario)
                editor.putString("token", token)
                editor.putString("nombreUsuario", response.data.nombre)
                editor.putString("ape_completo", response.data.ape_completo)
                editor.putString("fecha_nac", response.data.fecha_nac)
                editor.putString("documento", response.data.documento)
                editor.putString("email", response.data.email)
                editor.putString("foto", response.data.foto)
                editor.putString("direccion", response.data.direccion)
                editor.putString("telefono", response.data.telefono)
                editor.apply()

                // Navegar a HomeActivity
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish() // Opcional: finaliza la actividad de inicio de sesión si no deseas que el usuario pueda volver presionando el botón Atrás
            } else {
                intentos -= 1

                if (intentos == 0) {


                    val email = binding.txtIngresarUsuario.text.toString()
                    Toast.makeText(this, "Usuario bloqueado por limite de intentos", Toast.LENGTH_SHORT).show()

                    bloquearUsuario(email)
                } else {

                    Toast.makeText(this, "Contraseña incorrecta, te quedan $intentos intentos", Toast.LENGTH_SHORT).show()
                }
            }
        })

        binding.loginButton.setOnClickListener {
            val email = binding.txtIngresarUsuario.text.toString()
            val password = binding.txtIngresarClave.text.toString()

            val md5Password = md5(password)
            loginViewModel.login(email, md5Password)
        }

        binding.txtCrearCuenta.setOnClickListener {
            val intent = Intent(this, AgregarPacienteActivity::class.java)
            startActivity(intent)
        }
    }

    private fun md5(s: String): String {
        return try {
            // Crear un MessageDigest para el algoritmo MD5
            val digest = MessageDigest.getInstance("MD5")
            digest.update(s.toByteArray())
            val messageDigest = digest.digest()

            // Crear una cadena hexadecimal a partir del hash
            val hexString = StringBuilder()
            for (b in messageDigest) {
                val hex = Integer.toHexString(0xFF and b.toInt())
                if (hex.length == 1) {
                    hexString.append('0')
                }
                hexString.append(hex)
            }
            hexString.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            ""
        }
    }

    private fun bloquearUsuario(email: String) {
        val apiService = ApiClient.apiService
        val call = apiService.usuarioBloquear(email)
        call.enqueue(object : Callback<BloquearUsuarioResponse> {
            override fun onResponse(call: Call<BloquearUsuarioResponse>, response: Response<BloquearUsuarioResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@LoginActivity, "Usuario bloqueado por múltiples intentos fallidos", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@LoginActivity, "Error al bloquear el usuario", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<BloquearUsuarioResponse>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Fallo en la conexión: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
