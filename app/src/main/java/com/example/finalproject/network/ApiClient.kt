package com.example.finalproject.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {
    companion object{
        fun getRetrofit():Retrofit{
            return Retrofit.Builder()
                .baseUrl("http://192.168.43.129:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}
