package com.example.apppaciente.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.apppaciente.model.LoginResponse
import com.example.apppaciente.repository.Repository

class LoginViewModel : ViewModel() {
    private val repository = Repository()
    private val _loginResponse = MutableLiveData<LoginResponse?>()
    val loginResponse: LiveData<LoginResponse?> = _loginResponse

    fun login(email: String, clave: String) {
        repository.login(email, clave).observeForever {
            _loginResponse.value = it
        }
    }
}
