package com.example.apppaciente.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.apppaciente.api.ApiClient
import com.example.apppaciente.model.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Repository {
    fun login(email: String, clave: String): LiveData<LoginResponse?> {
        val data = MutableLiveData<LoginResponse?>()
        ApiClient.apiService.login(email, clave).enqueue(object : Callback<LoginResponse?> {
            override fun onResponse(call: Call<LoginResponse?>, response: Response<LoginResponse?>) {
                if (response.isSuccessful) {
                    Log.d("Repository", "Response successful: ${response.body()}")
                    data.value = response.body()
                } else {
                    Log.e("Repository", "Response error: ${response.code()} - ${response.message()}")
                    data.value = null
                }
            }

            override fun onFailure(call: Call<LoginResponse?>, t: Throwable) {
                Log.e("Repository", "Request failed: ${t.message}")
                data.value = null
            }
        })
        return data
    }
}
